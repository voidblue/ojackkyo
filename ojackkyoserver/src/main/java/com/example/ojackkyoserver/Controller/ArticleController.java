package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Exceptions.MalFormedResourceException;
import com.example.ojackkyoserver.Exceptions.NoPermissionException;
import com.example.ojackkyoserver.Exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.Exceptions.NullTokenException;
import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import com.example.ojackkyoserver.Service.ArticleService;
import io.jsonwebtoken.JwtException;
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

    @GetMapping(value = "/{id}")
    public Article get(@PathVariable Integer id, HttpServletResponse res) throws IOException {
        try {
            return articleService.get(id);
        } catch (NoResourcePresentException e) {
            res.sendError(404, e.getMessage());
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
    public Article create(@RequestBody Article article, HttpServletResponse res) throws IOException {
        try {
            return articleService.create(article);
        } catch (MalFormedResourceException|JwtException e) {
            res.sendError(400, e.getMessage());
            return null;
        }
    }



    @PutMapping
    public Article update(@RequestBody Article article, HttpServletResponse res) throws IOException {
        try {
            return articleService.update(article);
        } catch (MalFormedResourceException|NoResourcePresentException|JwtException e) {
            res.sendError(400, e.getMessage());
            return null;
        } catch (NoPermissionException e) {
            res.sendError(403, e.getMessage());
            return null;
        }
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletResponse res) throws IOException {
        try {
            articleService.delete(id);
        } catch (NoResourcePresentException|JwtException e) {
            res.sendError(400,e.getMessage());
        } catch (NoPermissionException e) {
            e.printStackTrace();
        }
    }



    //TODO article에 파일 매핑기켜줘야함!
    @PostMapping("/image")
    public void fileupload(@RequestParam String token ,@RequestParam MultipartFile image, @RequestParam Integer articleId,
                           HttpServletResponse res) throws IOException {
        try {
            articleService.saveImage(token, image, articleId);
        } catch (NoPermissionException e) {
            res.sendError(403, e.getMessage());
        } catch (NoResourcePresentException e) {
            res.sendError(404, e.getMessage());
        }
    }

}
