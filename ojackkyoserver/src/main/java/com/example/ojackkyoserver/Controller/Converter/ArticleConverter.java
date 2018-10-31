package com.example.ojackkyoserver.Controller.Converter;

import com.example.ojackkyoserver.Controller.value.ArticleUpdateParams;
import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;

@Component
public class ArticleConverter {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleConverter(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article convertArticleUpdateParamsToDomainArticle(ArticleUpdateParams params){
        Article article = articleRepository.findById(params.getId()).orElseThrow(
                ()-> new NoResultException("해당하는 게시글이 없습니다.")
        );
        article.setText(params.getText());
        article.setTitle(params.getTitle());
        article.setTags(params.getTags());
        return article;
    }
}
