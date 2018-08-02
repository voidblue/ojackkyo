package com.example.ojackkyoserver.Controller;


import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tagArticleMap")
public class TagArticleMapController {
    @Autowired
    TagArticleMapRepository repository;

    @PostMapping
    public TagArticleMap create(TagArticleMap tagArticleMap){
        tagArticleMap.setId(null);
        return repository.save(tagArticleMap);
    }
}
