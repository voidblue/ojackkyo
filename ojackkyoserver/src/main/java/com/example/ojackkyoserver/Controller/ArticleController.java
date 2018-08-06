package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    TagArticleMapRepository tagArticleMapRepository;
    @Autowired
    TagRepository tagRepository;
    @GetMapping("/{id}")
    public Article get(@PathVariable Integer id){
        Article article;
        Optional<Article> optArticle = articleRepository.findById(id);
        if(optArticle.isPresent()) {
            article = optArticle.get();
            article.setViewed(article.getViewed() + 1);
            articleRepository.save(article);
        }else{
            article = null;
        }
        return article;
    }

    @GetMapping("/list/search")
    public Page<Article> get(@RequestParam(defaultValue = "null") String tag,
                             @RequestParam(defaultValue = "null") String authorsNickname,
                             @RequestParam(defaultValue = "null")String text, Pageable pageable){
        if(tag != null && !tag.equals("null")){
            List articles = new ArrayList();
            List<TagArticleMap> map = tagArticleMapRepository.findAllByTagName(tag);
            List<Integer> ids = new ArrayList<>();
            return articleRepository.findByIdIn(ids, pageable);
        }else if (authorsNickname != null && !authorsNickname.equals("null")){
            Page<Article> articles = articleRepository.findAlLByAuthorsNickname(authorsNickname, pageable);
            return articles;
        }else if (text != null && !text.equals("null")){
            String[] words = text.split(" ");

            ArrayList fullResult = new ArrayList();
            List<Article> Articles = articleRepository.findAll();

            for (Article e : Articles){
                for (String w : words) {
                    if (e.getTitle().contains(w)) {e.setSearchPriority(e.getSearchPriority()+2);}
                    else if(e.getText().contains(w)) {e.setSearchPriority(e.getSearchPriority()+1);}
                }
                if(e.getSearchPriority()>1){
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
            ArrayList result = new ArrayList();
            for (int i = pageable.getPageSize() * pageable.getPageNumber() ; i < (pageable.getPageSize()+1) * pageable.getPageNumber() ; i++){
                result.add(fullResult.get(i));
            }
            return articleRepository.findAlLByAuthorsNickname(authorsNickname, pageable);

        }else{
            return null;
        }

    }

    @PostMapping
    public Article create(@RequestBody Article article, HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("token");
        article.setId(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        article.setTimeCreated(sdf.format(new Date()));
        final Article[] result = {null};
        AuthContext.askLoginedAndRun(token, res, ()->{
            result[0] = (Article) articleRepository.save(article);
            ArrayList<TagArticleMap> maps = article.getTagArticleMaps();
            for (TagArticleMap e : maps){
                if (tagRepository.existsByName(e.getTagName())){
                    Tag tag = (Tag) tagRepository.findByName(e.getTagName());
                    tag.setReferredTimes(tag.getReferredTimes() + 1);
                    tagRepository.save(tag);
                }else{
                    Tag tag = new Tag();
                    tag.setReferredTimes(1);
                    tag.setName(e.getTagName());
                    tagRepository.save(tag);
                }
            }
        });
        return result[0];
    }

    @PutMapping
    public Article update(@RequestBody Article article, HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("token");
        final Article[] result = {null};
        AuthContext.askAuthorityAndRun(article.getAuthorsNickname(), token, res, ()->{
            if(articleRepository.existsById(article.getId())){
                result[0] = (Article) articleRepository.save(article);
            }else{
                try {
                    res.sendError(404, "존재하지 않은 자원에 대한 수정입니다.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return result[0];
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletRequest req, HttpServletResponse res){
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

    @PostMapping("/file")
    public void fileupload(@RequestParam MultipartFile file, @RequestParam Integer articleId){
        File path = new File("files/article/"+articleId + "/" + file.getOriginalFilename());
        if(!path.exists()){
            path.mkdirs();
        }
        try {
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
