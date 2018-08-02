package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRespository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    TagArticleMapRepository tagArticleMapRepository;

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

    @GetMapping("/list/search?")
    public List<Article> get(@RequestParam(defaultValue = "null") String tag,
                            @RequestParam(defaultValue = "null") String authorsNickname,
                             @RequestParam(defaultValue = "null")String text){
        if(tag != null && !tag.equals("null")){
            List articles = new ArrayList();

            List<TagArticleMap> map = tagArticleMapRepository.findAllByTagName(tag);
            for (TagArticleMap e : map){
                articles.add(articleRepository.findById(e.getArticle()));
            }
            return articles;
        }else if (authorsNickname != null && authorsNickname.equals("null")){
            List<Article> articles = articleRepository.findAlLByAuthorsNickname(authorsNickname);
            return articles;
        }else if (text != null && !text.equals("null")){
            String[] words = text.split(" ");

            List<Article> allAricles = articleRepository.findAll();
            ArrayList<ArrayList> sets = new ArrayList();
            for (int i = 0 ; i < words.length; i++){
                sets.add(new ArrayList<Article>());
            }
            for (Article e : allAricles){
                int matchTime = 0;
                for (String w : words) {
                    if(e.getText().contains(w)) {matchTime++;}
                }
                if (matchTime >= 1){
                    sets.get(matchTime - 1).add(e);
                }
            }
            ArrayList result = new ArrayList();
            for (int i = words.length -1 ; i >= 0 ; i--){
                result.addAll(sets.get(i));
            }
            return result;

        }else{
            return null;
        }

    }

    @PostMapping
    public Article create(@RequestBody Article article, HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("token");
        final Article[] result = {null};
        AuthContext.askLoginedAndRun(token, res, ()->{
            article.setId(null);
            result[0] = articleRepository.save(article);
        });
        return result[0];
    }

    @PutMapping
    public Article update(@RequestBody Article article, HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("token");
        final Article[] result = {null};
        AuthContext.askAuthorityAndRun(article.getAuthorsNickname(), token, res, ()->{
            if(articleRepository.existsById(article.getId())){
                result[0] = articleRepository.save(article);
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

}
