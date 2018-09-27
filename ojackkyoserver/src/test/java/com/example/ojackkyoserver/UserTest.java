package com.example.ojackkyoserver;

import com.example.ojackkyoserver.Model.User;
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

@Deprecated
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    private static final String PATH = "/user";
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
    @Deprecated
    public void get(){
        String uid = "test";
        String password = "test";

        User user = restTemplate.getForObject(PATH + "/" + 17, User.class);

        assertThat(user.getUid(), is(uid));
        assertThat(user.getPassword(), is(password));


    }
    //토큰에 의해 개별 테스트가 힘들기 때문에 CUD 순서로 통합테스트
    @Test
    public void create(){
        //TODO DB에서 테스트유저 꼭 지우고 테스트할것!
        User userForCreate = new User();
        userForCreate.setUid("testForCUD");
        userForCreate.setPassword("1234");
        userForCreate.setNickname("testForCUD");
        User createdUser = restTemplate.postForObject(PATH + "/", userForCreate, User.class);
        System.out.println(createdUser);
        validate(userForCreate, createdUser);

        duplicateCheckTest();

        User userForUpdate = createdUser;

        userForUpdate.setNickname("test");
        userForUpdate.setPassword("4321");
        context.notoken(PATH , HttpMethod.PUT, userForUpdate);
        context.notOwner(PATH, HttpMethod.PUT, userForUpdate);

        HttpEntity entity = new HttpEntity(userForUpdate, httpHeaders);
        ResponseEntity<User> user= restTemplate.exchange(PATH, HttpMethod.PUT, entity , User.class);

        validate(user.getBody(), userForUpdate);

    }

    private void duplicateCheckTest() {
        User userForDuplicationTest = new User();
        userForDuplicationTest.setNickname("test");
        userForDuplicationTest.setUid("testForCUD");
        userForDuplicationTest.setPassword("1234");
        HttpEntity entityForDuplicationTest = new HttpEntity(userForDuplicationTest,context.getSimpleHttpHeader());
        context.getSimpleHttpHeader();
        ResponseEntity<User> CreateFailUser = restTemplate.exchange(PATH + "/", HttpMethod.PUT, (HttpEntity<?>) entityForDuplicationTest, User.class);
        assertThat(CreateFailUser.getStatusCode().value() ,is(404));
    }

    @Test
    public void invalidUidWhenUpdate(){
        User userForUpdate = getCreatedUser();
        userForUpdate.setUid("gg");

        context.notoken(PATH , HttpMethod.PUT, userForUpdate);
        context.notOwner(PATH, HttpMethod.PUT, userForUpdate);

        HttpEntity entity = new HttpEntity(userForUpdate, httpHeaders);
        ResponseEntity<User> user= restTemplate.exchange(PATH, HttpMethod.PUT, entity , User.class);

        assertThat(user.getBody(), is(nullValue()));
    }

    //TODO 계정 삭제는 논의 후 진행
//    @Test
//    public void delete(){
//        User user = restTemplate.getForObject(PATH + "/"+ getCreatedUser().getId(), User.class);
//        restTemplate.delete(PATH + "/" + user.getId());
//
//        User deletedUser = restTemplate.getForObject(PATH + "/" + user.getId(), User.class);
//
//        assertThat(deletedUser, is(nullValue()));
//
//    }



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
