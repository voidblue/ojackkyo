package com.example.ojackkyoserver.Repository;

import com.example.ojackkyoserver.Model.TagArticleMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagArticleMapRepository extends JpaRepository<TagArticleMap, Integer> {
    List<TagArticleMap> findAllByTagName(String tagName);
    void deleteAllByArticle(Integer articleId);
    List<TagArticleMap> findAllByArticle(Integer article);
}
