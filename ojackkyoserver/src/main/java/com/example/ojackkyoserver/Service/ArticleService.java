package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.MalFormedResourceException;
import com.example.ojackkyoserver.Exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import com.example.ojackkyoserver.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON )
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;
    private TagArticleMapRepository tagArticleMapRepository;
    private TagRepository tagRepository;
    private AuthService authService;
    private UserRepository userRepository;

    public Article get(@PathVariable Integer id) throws NoResourcePresentException {
        Optional<Article> optArticle = articleRepository.findById(id);
        if(optArticle.isPresent()){
            Article article = optArticle.get();
            article.setViewed(article.getViewed() + 1);
            articleRepository.save(article);
            return article;
        }else {
            throw new NoResourcePresentException();
        }
    }
    public Page<Article> getListByTag(String tag, Pageable pageable) {
        List<TagArticleMap> map = tagArticleMapRepository.findAllByTagName(tag);
        List<Integer> ids = new ArrayList<>();
        for(TagArticleMap e : map){
            ids.add(e.getArticle());
        }
        System.out.println(articleRepository.findByIdIn(ids, pageable    ));
        return articleRepository.findByIdIn(ids, pageable);
    }

    public Page<Article> getListByNickname(String authorsNickname, Pageable pageable) {
        System.out.println(authorsNickname);
        User author = userRepository.findByNickname(authorsNickname);
        System.out.println(author);
        Page<Article> articles = articleRepository.findAllByAuthor(author, pageable);
        System.out.println(articles);
        Iterator i = articles.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }
        return articles;
    }

    public Page<Article> getListByText(String text, Pageable pageable) {
        String elasticSearchHost = "127.0.0.1/ojackkyo/article";
        int elasticSearchPort = 6000;
        RestClient restClient = RestClient.builder(new HttpHost(elasticSearchHost, elasticSearchPort)).build();
        Request request = new Request("GET", elasticSearchHost);
        request.addParameter("text", text);

        Response response = null;
        String resBody = null;
        try {
            response  = restClient.performRequest(request);
            resBody =  EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(resBody);
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

    public Article create(@RequestBody Article article) throws MalFormedResourceException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = req.getHeader("token");
        if(article.getTitle().equals("") || article.getText().equals("")){
            throw new MalFormedResourceException();
        }
        article.setViewed(0);
        article.setId(null);
        TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(time);
        article.setTimeCreated(sdf.format(new Date()));

        //TODO resultHolder 말고 다른 방법 없나???
        final Article[] resultHolder = {null};
        authService.askLoginedAndRun(token, ()->{
            String authorsNickname = (String) authService.getDecodedToken(token).getBody().get("nickname");
            //토큰 검증은 이미 한 상태이므로 아래 토큰에서 닉네임을 가져오는 것은 실행에 문제가 없음
            article.setAuthor(userRepository.findByNickname(authorsNickname));
            resultHolder[0] = (Article) articleRepository.save(article);
            ArrayList<Tag> tags = article.getTags();
            if(tags != null) {
                saveTagsToTagArticleMap(tags, article.getId());
            }
        });

        return resultHolder[0];
    }

    public Article update(Article article) throws MalFormedResourceException, NoResourcePresentException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = req.getHeader("token");
        if(article.getTitle().equals("") || article.getText().equals("")){
            throw new MalFormedResourceException();
        }

        final Article[] resultHolder = {null};
        if(articleRepository.existsById(article.getId())) {
            authService.askAuthorityAndRun(article.getAuthorsNickname(), token, () -> {
                resultHolder[0] = (Article) articleRepository.save(article);
            });
        }else{
            throw new NoResourcePresentException();
        }
        return resultHolder[0];
    }

    public void delete(Integer id) throws NoResourcePresentException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = req.getHeader("token");

        Optional<Article> optArticle = articleRepository.findById(id);
        if(optArticle.isPresent()){
            Article article = optArticle.get();
            authService.askAuthorityAndRun(article.getAuthorsNickname(), token, ()->{
                articleRepository.deleteById(id);
            });
        }else{
            throw new NoResourcePresentException();
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
        if(tags.size() == 0) {
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


}
