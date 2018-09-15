package com.example.ojackkyoserver;

import com.example.ojackkyoserver.Model.Comment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentTest {
    private static final String PATH = "/comments";

    @Autowired
    private TestRestTemplate restTemplate;
    RestTestContext context;

    HttpHeaders httpHeaders;
    @Before
    public void setup() {
        context = new RestTestContext(restTemplate);
        httpHeaders = context.getHttpHeadersWithToken();

    }

    @Test
    public void get() {
        Comment commentInDB = new Comment();
        commentInDB.setId(1);
        commentInDB.setTitle("test title");
        commentInDB.setContents("test contents");
        commentInDB.setAuthorsNickname("test");
        commentInDB.setArticleId(181);

        Comment comment = restTemplate.getForObject(PATH + "/" + 1, Comment.class);
        validateComment(commentInDB, comment);
    }

    @Test
    public void noResourcePresentExceptionGet(){
        ResponseEntity<Comment> comment = restTemplate.getForEntity(PATH + "/" + 0, Comment.class);
        assertThat(comment.getStatusCode().value(), is(404));
    }

    @Test
    public void getList(){

        ArrayList<Comment> comments = restTemplate.getForObject(PATH + "/list/search?" + "authorsNickname=test", ArrayList.class);
        assertThat(comments.size() > 0, is(true));

        ArrayList<Comment> comments2 = restTemplate.getForObject(PATH + "/list/search?" + "articleId=181", ArrayList.class);
        assertThat(comments2.size() > 0, is(true));
    }

    @Test
    public void create(){
        Comment comment = new Comment();
        comment.setArticleId(181);
        comment.setAuthorsNickname("test");
        comment.setTitle("create test title");
        comment.setContents("create test contents");

        HttpEntity<Comment> entity = new HttpEntity(comment, httpHeaders);
        ResponseEntity<Comment> resEntity = restTemplate.exchange(PATH, HttpMethod.POST, (HttpEntity<?>) entity, Comment.class);
        assertThat(resEntity.getStatusCode().is2xxSuccessful(), is(true));
        Comment insertedComment = resEntity.getBody();
        validateComment(comment, insertedComment);

        context.notoken(PATH, HttpMethod.POST, comment);
    }




    private void validateComment(Comment comment1, Comment comment2) {
        assertThat(comment1.getTitle(), is(comment2.getTitle()));
        assertThat(comment1.getContents(), is(comment2.getContents()));
        assertThat(comment1.getArticleId(), is(comment2.getArticleId()));
        assertThat(comment1.getAuthorNickname(), is(comment2.getAuthorNickname()));
    }
}
