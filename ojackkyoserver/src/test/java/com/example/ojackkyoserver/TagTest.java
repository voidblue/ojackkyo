package com.example.ojackkyoserver;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import com.example.ojackkyoserver.Model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TagTest {
    private static final String PATH = "/tag";
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
    public void create(){
        Tag tag = new Tag();
        tag.setReferredTimes(1);
        tag.setName("테스트태그");
        context.notoken(PATH, HttpMethod.POST, tag);

        HttpEntity entity = new HttpEntity(tag, httpHeaders);
        ResponseEntity<Tag> createdTag = restTemplate.exchange(PATH, HttpMethod.POST, entity, Tag.class);
        System.out.println(createdTag);
        validate(tag, createdTag.getBody());
    }

    private void validate(Tag tag, Tag createdTag) {
        assertThat(tag.getReferredTimes() , is (createdTag.getReferredTimes()));
        assertThat(tag.getName() , is (createdTag.getName()));
    }

}
