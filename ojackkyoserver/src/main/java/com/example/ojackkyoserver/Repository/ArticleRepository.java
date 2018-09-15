package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository<Object, Integer extends Pageable> extends JpaRepository<Article, Integer> {
    Page<Article> findAllByAuthor(User user, Pageable pageable);
    Page<Article> findByIdIn(List<Integer> ids, Pageable pageable);
}
