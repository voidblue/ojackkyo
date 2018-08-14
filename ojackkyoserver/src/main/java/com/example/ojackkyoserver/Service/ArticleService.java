package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    TagArticleMapRepository tagArticleMapRepository;
    @Autowired
    TagRepository tagRepository;




    public Article get(@PathVariable Integer id){
        HttpServletResponse res = getCurrentResponse();

        Article article = null;
        Optional<Article> optArticle = articleRepository.findById(id);

        if(optArticle.isPresent()){
            article = optArticle.get();
            article.setViewed(article.getViewed() + 1);
            articleRepository.save(article);
        }else {
            System.out.println("id : " + id + " message : ");
            res.setStatus(404, "존재하지 않는 자원입니다.");
        }
        return article;
    }
    public Page<Article> getListByTag(String tag, Pageable pageable) {
        List<TagArticleMap> map = tagArticleMapRepository.findAllByTagName(tag);
        List<Integer> ids = new ArrayList<>();
        for(TagArticleMap e : map){
            ids.add(e.getArticle());
        }
        return articleRepository.findByIdIn(ids, pageable);
    }

    public Page<Article> getListByNickname(String authorsNickname, Pageable pageable) {
        Page<Article> articles = articleRepository.findAlLByAuthorsNickname(authorsNickname, pageable);
        return articles;
    }

    public Page<Article> getListByText(String text, Pageable pageable) {
        String[] words = text.split(" ");

        List<Article> Articles = articleRepository.findAll();
        ArrayList<Article> fullResult = new ArrayList();

        for (Article e : Articles){
            for (String w : words) {
                if (e.getTitle().contains(w)) {e.setSearchPriority(e.getSearchPriority()+2);}
                else if(e.getText().contains(w)) {e.setSearchPriority(e.getSearchPriority()+1);}
            }
            if(e.getSearchPriority()>=1){
                fullResult.add(e);
            }
        }

        fullResult.sort((Comparator) (o, t1) -> {
            if (((Article)o).getSearchPriority() < ((Article)t1).getSearchPriority() ){
                return 1;
            }else if (((Article)o).getSearchPriority() == ((Article)t1).getSearchPriority() ){
                return 0;
            }else {return -1;}
        });


        ArrayList<Integer> ids = new ArrayList();
        for (int i = pageable.getPageSize() * pageable.getPageNumber() ; i < (pageable.getPageSize()+1) * pageable.getPageNumber() ; i++){
            ids.add(fullResult.get(i).getId());
        }
        return articleRepository.findByIdIn(ids, pageable);
    }

    public Article create(@RequestBody Article article) {
        HttpServletRequest req = getCurrentRequest();
        HttpServletResponse res = getCurrentResponse();

        String token = req.getHeader("token");
        if(article.getTitle().equals("")){
            res.setStatus(404,"제목이 없습니다. ");
            return null;
        }
        article.setViewed(0);
        article.setId(null);
        TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(time);
        article.setTimeCreated(sdf.format(new Date()));

        final Article[] resultHolder = {null};
        AuthContext.askLoginedAndRun(token, res, ()->{
            System.out.println(article);
            article.setAuthorsNickname((String) AuthContext.decodeToken(token).getBody().get("nickname"));
            resultHolder[0] = (Article) articleRepository.save(article);
            ArrayList<Tag> tags = article.getTags();
            if(tags != null) {
                saveTagsToTagArticleMap(tags, resultHolder[0].getId());
            }
        });

        return resultHolder[0];
    }

    public Article update(Article article) {
        HttpServletRequest req = getCurrentRequest();
        HttpServletResponse res = getCurrentResponse();
        String token = req.getHeader("token");

        final Article[] resultHolder = {null};
        AuthContext.askAuthorityAndRun(article.getAuthorsNickname(), token, res, ()->{
            if(articleRepository.existsById(article.getId())){
                resultHolder[0] = (Article) articleRepository.save(article);
            }else{
                res.setStatus(404, "존재하지 않은 자원에 대한 수정입니다.");
            }
        });
        return resultHolder[0];
    }

    public void delete(Integer id) {
        HttpServletRequest req = getCurrentRequest();
        HttpServletResponse res = getCurrentResponse();
        String token = req.getHeader("token");

        Optional<Article> optArticle = articleRepository.findById(id);
        if(optArticle.isPresent()){
            Article article = optArticle.get();
            AuthContext.askAuthorityAndRun(article.getAuthorsNickname(), token, res, ()->{
                articleRepository.deleteById(id);
            });
        }else{
            res.setStatus(404, "이미 없는 자원입니다.");
        }
    }

    private void saveTagsToTagArticleMap(ArrayList<Tag> tags, Integer articleId){
        for (Tag e : tags) {
            if (tagRepository.existsByName(e.getName())) {
                Tag innerTag = tagRepository.findByName(e.getName());
                e.setId(innerTag.getId());
                e.setReferredTimes(innerTag.getReferredTimes() + 1);
                tagRepository.save(e);
            } else {
                e.setReferredTimes(1);
                e.setName(e.getName());
                tagRepository.save(e);
            }
        }
        if(tags != null) {
            ArrayList<Tag> tagsForCreate = new ArrayList<>();
            ArrayList<TagArticleMap> tams = new ArrayList<>();
            for (Object e : tags) {
                TagArticleMap tam = new TagArticleMap();
                tam.setArticle(articleId);
                tam.setTagName(((Tag) e).getName());
                tams.add(tam);
                tagsForCreate.add((Tag) e);
            }
            tagArticleMapRepository.saveAll(tams);
        }

    }

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
    public static HttpServletResponse getCurrentResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }
}
