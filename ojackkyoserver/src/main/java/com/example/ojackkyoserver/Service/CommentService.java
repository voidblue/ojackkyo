package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.exceptions.MalFormedResourceException;
import com.example.ojackkyoserver.exceptions.NoPermissionException;
import com.example.ojackkyoserver.exceptions.NoResourcePresentException;
import com.example.ojackkyoserver.exceptions.NullTokenException;
import com.example.ojackkyoserver.Model.Article;
import com.example.ojackkyoserver.Model.Comment;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.ArticleRepository;
import com.example.ojackkyoserver.Repository.CommentRepository;
import com.example.ojackkyoserver.Repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(SCOPE_SINGLETON)
@AllArgsConstructor
public class CommentService {
    private JwtContext jwtContext;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;

    public Comment get(Integer id) throws NoResourcePresentException {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isPresent()){
            return comment.get();
        }else{
            throw new NoResourcePresentException();
        }
    }

    public Page<Comment> getListByAuthorNickname(String authorNickname, Pageable pageable) {
        User author = userRepository.findByNickname(authorNickname);
        return commentRepository.findAllByAuthor(author, pageable);
    }

    public Page<Comment> getListByArticleId(Integer articleId, Pageable pageable){
        Optional<Article> article = articleRepository.findById(articleId);

        return article.map(article1 -> commentRepository.findAllByArticle(article1, pageable)).orElse(null);
    }

    public Comment create(Comment comment) throws MalFormedResourceException, NoResourcePresentException, NullTokenException, JwtException {
        comment.setId(null);

        if(comment.getContents().equals("") || comment.getTitle().equals("")){
            throw new MalFormedResourceException();
        }

        if(articleRepository.existsById(comment.getArticleId())){
            Article article = (Article) articleRepository.findById(comment.getArticleId()).get();
            comment.setArticle(article);
        }else{
            throw new NoResourcePresentException();
        }
        TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(time);
        comment.setTimeCreated(sdf.format(new Date()));

        jwtContext.loginCheck();
        String authorNickname = (String) jwtContext.getDecodedToken().get("nickname");
        //jwtContext에서 이 유저가 유효한지 이미 검사함
        User author = userRepository.findByNickname(authorNickname);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    public Comment update(Comment comment) throws NoResourcePresentException, MalFormedResourceException,
                                                NullTokenException, NoPermissionException, JwtException {
        if(comment.getContents().equals("") || comment.getTitle().equals("")){
            throw new MalFormedResourceException();
        }
        Article article = (Article) articleRepository.findById(comment.getArticleId()).get();
        comment.setArticle(article);

        if(commentRepository.existsById(comment.getId())) {
            jwtContext.entityOwnerCheck(comment.getAuthorsNickname());
            String authorNickname = (String) jwtContext.getDecodedToken().get("nickname");
            //jwtContext에서 이 유저가 유효한지 이미 검사함
            User author = userRepository.findByNickname(authorNickname);
            comment.setAuthor(author);
            //TODO 왜 업데이트문에서는 자기 엔터티를 제대로 못들어오지??
            commentRepository.save(comment);
            return commentRepository.findById(comment.getId()).get();
        }else{
            throw new NoResourcePresentException();
        }
    }

    public void delete(Integer id) throws NoResourcePresentException, NullTokenException,
                                        NoPermissionException, JwtException {
        if(commentRepository.existsById(id)){
            Comment comment = commentRepository.findById(id).get();
            jwtContext.entityOwnerCheck(comment.getAuthorsNickname());
            commentRepository.deleteById(id);
        }else{
            throw new NoResourcePresentException();
        }
    }
}
