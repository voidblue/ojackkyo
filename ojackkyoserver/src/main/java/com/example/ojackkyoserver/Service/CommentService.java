package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.Model.Comment;
import com.example.ojackkyoserver.Repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    public Comment get(Integer id) throws NoResourcePresentException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }else{
            throw new NoResourcePresentException();
        }
    }
}
