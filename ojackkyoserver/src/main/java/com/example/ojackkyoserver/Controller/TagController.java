package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@ResponseBody
@RequestMapping("/tag")
public class TagController {

    @Autowired
    TagRepository tagRepository;

    @GetMapping("/{id}")
    public Optional<Tag> get(@PathVariable Integer id){
        return tagRepository.findById(id);
    }


}
