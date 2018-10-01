package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Comment;
import com.example.ojackkyoserver.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findById(Integer id);
    Page<Comment> findAllByArticle(Article article, Pageable pageable);
    Page<Comment> findAllByAuthor(User author, Pageable pageable);
}
