package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.Model.Comment;
import com.example.ojackkyoserver.Repository.CommentRepository;
import com.example.ojackkyoserver.Service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    CommentService commentService;

    @GetMapping(value = "/{id}")
    public Comment get(@PathVariable Integer id, HttpServletResponse res){
        try {
            return commentService.get(id);
        } catch (NoResourcePresentException e) {
            res.setStatus(404, e.getMessage());
            return null;
        }
    }
}
