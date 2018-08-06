package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository<Object, Integer extends Pageable> extends JpaRepository<Article, Integer> {
    Page<Article> findAlLByAuthorsNickname(String authorsNickname, Pageable pageable);
    Page<Article> findByIdIn(List<Integer> ids, Pageable pageable);
}
