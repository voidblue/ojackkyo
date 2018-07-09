package com.example.ojackkyoserver;

import com.example.ojackkyoserver.Model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private static final String PATH = "/user";

    String jwtString;
    HttpHeaders httpHeaders;
    @Before
    public void setup(){
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis())
                .setSubject("")
                .claim("uid", "testuser")
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .compact();
        httpHeaders.add("token", jwtString);

    }


    @Test
    public void get(){
        String uid = "testuser";
        String password = "1234";


        User user = restTemplate.getForObject(PATH + "/" + 1, User.class);

        assertThat(user.getUid(), is(uid));
        assertThat(user.getPassword(), is(password));


    }

    @Test
    public void create(){
        //TODO DB에서 테스트유저 꼭 지우고 테스트할것!
        User userForCreate = new User();
        userForCreate.setUid("testuser");
        userForCreate.setPassword("1234");
        User user = restTemplate.postForObject(PATH + "/", userForCreate, User.class);
        System.out.println(user);
        validate(userForCreate, user);
    }

    @Test
    public void duplicatedUidCreate(){
        User userForCreate = new User();
        userForCreate.setUid("duplicateuser");
        userForCreate.setPassword("1234");
        User user = restTemplate.postForObject(PATH + "/", userForCreate, User.class);

        assertThat(user ,is(nullValue()));

    }


    @Test
    //TODO testuser 지우고 테스트할것!
    public void update(){
        User userForUpdate = getCreatedUser();
        userForUpdate.setPassword("4321");
        notoken(PATH , HttpMethod.PUT, userForUpdate);
        notOwner(PATH, HttpMethod.PUT, userForUpdate);

        HttpEntity entity = new HttpEntity(userForUpdate, httpHeaders);
        ResponseEntity<User> user= restTemplate.exchange(PATH, HttpMethod.PUT, entity , User.class);

        validate(user.getBody(), userForUpdate);

    }

    @Test
    public void invailidUidWhenUpdate(){
        User userForUpdate = getCreatedUser();
        userForUpdate.setUid("gg");

        notoken(PATH , HttpMethod.PUT, userForUpdate);
        notOwner(PATH, HttpMethod.PUT, userForUpdate);

        HttpEntity entity = new HttpEntity(userForUpdate, httpHeaders);
        ResponseEntity<User> user= restTemplate.exchange(PATH, HttpMethod.PUT, entity , User.class);

        assertThat(user.getBody(), is(nullValue()));
    }

//    @Test
//    public void delete(){
//        User user = restTemplate.getForObject(PATH + "/"+ getValidUser().getId(), User.class);
//        restTemplate.delete(PATH + "/" + user.getId());
//
//        User deletedUser = restTemplate.getForObject(PATH + "/" + user.getId(), User.class);
//
//        assertThat(deletedUser, is(nullValue()));
//
//    }
//
    public void notOwner(String url,HttpMethod httpMethod, User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis())
                .setSubject("")
                .claim("uid", "다른아이디")
                .claim("nickname", "")
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .compact();
        headers.add("token", jwtString);

        HttpEntity entity = new HttpEntity(user, headers);
        ResponseEntity<User> resultUser = restTemplate.exchange(url , httpMethod, entity, User.class);
        assertThat(resultUser.getBody(), is(nullValue()));
    }

    public void notoken(String url, HttpMethod httpMethod, User user){
        HttpHeaders notokenHeader = new HttpHeaders();
        HttpEntity entity = new HttpEntity(user, notokenHeader);
        ResponseEntity<User> resultUser = restTemplate.exchange(url , httpMethod, entity, User.class);

    }

    private User getCreatedUser() {
        User user = new User();
        user.setUid("testuser");
        user.setPassword("1234");
        return  restTemplate.postForObject(PATH + "/", user, User.class);
    }

    private void validate(User user1, User user2) {
        assertThat(user2.getUid(), is(user1.getUid()));
        assertThat(user2.getPassword(), is(user1.getPassword()));
    }
}
