package com.example.ojackkyoserver.Controller;


import com.example.ojackkyoserver.Model.TagArticleMap;
import com.example.ojackkyoserver.Repository.TagArticleMapRepository;
import com.example.ojackkyoserver.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tagArticleMap")
public class TagArticleMapController {
    @Autowired
    TagArticleMapRepository repository;
    @Autowired
    TagRepository tagRepository;

    @GetMapping("/{id}")
    public Optional<TagArticleMap> get(@PathVariable Integer id){
        return repository.findById(id);
    }
}
