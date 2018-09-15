package com.example.ojackkyoserver;

import com.example.ojackkyoserver.Model.Comment;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.support.NullValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.*;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;

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

        HashMap<String, Integer> comments = restTemplate.getForObject(PATH + "/list/search?" + "articleId=181", HashMap.class);
        System.out.println(comments.toString());
        assertThat( comments.get("totalElements") > 0, is(true));
        HashMap<String, Integer> comments2 = restTemplate.getForObject(PATH + "/list/search?" + "authorsNickname=test", HashMap.class);
        assertThat(comments2.get("totalElements") > 0, is(true));

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

    @Test
    public void update(){
        Comment comment = new Comment();
        comment.setId(1); //test resouce
        comment.setArticleId(181);
        comment.setTitle("updated title");
        comment.setContents("updated contents");
        comment.setAuthorsNickname("test");

        HttpEntity<Comment> entity = new HttpEntity(comment, httpHeaders);
        ResponseEntity<Comment> resEntity = restTemplate.exchange(PATH, HttpMethod.PUT, (HttpEntity<?>) entity, Comment.class);;
        System.out.println(resEntity.getStatusCode().getReasonPhrase());
        assertThat(resEntity.getStatusCode().value(), is(200));
        Comment updatedComment = resEntity.getBody();
        validateComment(comment, updatedComment);

        Comment origin = new Comment();
        origin.setId(1); //test resouce
        origin.setArticleId(181);
        origin.setTitle("test title");
        origin.setContents("test contents");
        origin.setAuthorsNickname("test");

        HttpEntity<Comment> originEntity = new HttpEntity(origin, httpHeaders);
        ResponseEntity<Comment> resOriginEntity = restTemplate.exchange(PATH, HttpMethod.PUT, (HttpEntity<?>) originEntity, Comment.class);
        assertThat(resOriginEntity.getStatusCode().is2xxSuccessful(), is(true));
        Comment updatedOriginComment = resEntity.getBody();
        validateComment(comment, updatedOriginComment);

        context.notOwner(PATH, HttpMethod.PUT, comment);
        context.notoken(PATH, HttpMethod.PUT, comment);
    }

    @Test
    public void delete(){
        Comment comment = new Comment();
        comment.setArticleId(181);
        comment.setAuthorsNickname("test");
        comment.setTitle("delete test title");
        comment.setContents("delete test contents");

        HttpEntity<Comment> entity = new HttpEntity(comment, httpHeaders);
        ResponseEntity<Comment> resEntity = restTemplate.exchange(PATH, HttpMethod.POST, (HttpEntity<?>) entity, Comment.class);
        assertThat(resEntity.getStatusCode().is2xxSuccessful(), is(true));
        Comment insertedComment = resEntity.getBody();
        validateComment(comment, insertedComment);

        ResponseEntity<Comment> resDeletedEntity = restTemplate.exchange(PATH+"/"+insertedComment.getId(), HttpMethod.DELETE, (HttpEntity<?>) entity, Comment.class);
        Comment deletedComment = restTemplate.getForObject(PATH + "/" + insertedComment.getId(), Comment.class);
        assertThat(deletedComment, is(nullValue()));
    }


    private void validateComment(Comment comment1, Comment comment2) {
        assertThat(comment1.getTitle(), is(comment2.getTitle()));
        assertThat(comment1.getContents(), is(comment2.getContents()));
        assertThat(comment1.getArticleId(), is(comment2.getArticleId()));
        assertThat(comment1.getAuthorsNickname(), is(comment2.getAuthorsNickname()));
    }
}
