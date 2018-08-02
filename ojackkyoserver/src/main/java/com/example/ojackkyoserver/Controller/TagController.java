package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.Tag;
import com.example.ojackkyoserver.Repository.TagRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@ResponseBody
@RequestMapping("/tag")
public class TagController {

    @Autowired
    TagRespository tagRespository;

    @GetMapping("/{id}")
    public void get(@PathVariable Integer id){

    }

    @PostMapping
    public Tag create(@RequestBody Tag tag, HttpServletRequest req, HttpServletResponse res){
        Tag[] tagHolder = new Tag[1];
        System.out.println(tag);
        AuthContext.askLoginedAndRun(req.getHeader("token"), res, ()->{
            if(!tagRespository.existsByName(tag.getName())) {
                tagHolder[0] = tagRespository.save(tag);
            }else {
                try {
                    tagHolder[0] = null;
                    res.sendError(404, "아이디 중복입니다.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(tagHolder[0]);
        return tagHolder[0];
    }

}
