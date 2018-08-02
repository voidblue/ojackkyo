package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findAlLByAuthorsNickname(String authorsNickname);
}
