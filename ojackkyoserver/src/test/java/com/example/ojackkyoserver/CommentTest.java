package com.example.ojackkyoserver;

import com.example.ojackkyoserver.Model.Comment;
import com.example.ojackkyoserver.Model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

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
        Integer id = 1;
        String title = "test title";
        String contents = "test contents";
        String authorNickname = "test";
        Integer articleId = 181;

        Comment comment = restTemplate.getForObject(PATH + "/" + 1, Comment.class);
        System.out.println(comment);
//        assertThat(comment.getId(), is(id));
//        assertThat(comment.getTitle(), is(title));
//        assertThat(comment.getContents(), is(contents));
//        assertThat(comment.getArticleId(), is(articleId));
//        assertThat(comment.getAuthorNickname(), is(authorNickname));


    }

}
