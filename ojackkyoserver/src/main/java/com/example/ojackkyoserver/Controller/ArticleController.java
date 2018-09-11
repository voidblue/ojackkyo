package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Exceptions.MalFormedResourceException;
import com.example.ojackkyoserver.Exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import com.example.ojackkyoserver.Service.ArticleService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
@Scope(SCOPE_SINGLETON)
public class ArticleController {
    @Autowired
    ArticleService articleService;

    public Article get(@PathVariable Integer id){
        try {
            return articleService.get(id);
        } catch (NoResourcePresentException e) {
            return null;
        }
    }

    @GetMapping("/list/search")
    public Page<Article> get(@RequestParam(defaultValue = "null") String tag,
                             @RequestParam(defaultValue = "null") String authorsNickname,
                             @RequestParam(defaultValue = "null")String text, Pageable pageable){
        if(!tag.equals("null")){
            return articleService.getListByTag(tag, pageable);
        }else if (!authorsNickname.equals("null")){
            return articleService.getListByNickname(authorsNickname, pageable);
        }else if (!text.equals("null")){
            return articleService.getListByText(text, pageable);
        }else{
            return null;
        }

    }



    @PostMapping
    public Article create(@RequestBody Article article, HttpServletResponse res){
        try {
            return articleService.create(article);
        } catch (MalFormedResourceException e) {
            res.setStatus(400, e.getMessage());
            return null;
        }
    }



    @PutMapping
    public Article update(@RequestBody Article article, HttpServletResponse res){
        try {
            return articleService.update(article);
        } catch (MalFormedResourceException|NoResourcePresentException e) {
            res.setStatus(400, e.getMessage());
            return null;
        }
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletResponse res){
        try {
            articleService.delete(id);
        } catch (NoResourcePresentException e) {
            res.setStatus(400,e.getMessage());
        }
    }



    //TODO article에 파일 매핑기켜줘야함!
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
