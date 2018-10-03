package com.example.ojackkyoserver;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RestTestContext {
    private TestRestTemplate restTemplate;

    RestTestContext(TestRestTemplate restTemplate){
        this.restTemplate = restTemplate;

    }
    HttpHeaders getSimpleHttpHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return  httpHeaders;
    }

    HttpHeaders getHttpHeadersWithToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis())
                .setSubject("")
                .claim("id", 17)
                .claim("tags", Arrays.toString(new String[]{}))
                .claim("uid", "test")
                .claim("nickname", "test")
                .claim("lastUpdatedTime", System.currentTimeMillis()-1000)
                .claim("timeCreated", System.currentTimeMillis())
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .compact();
        httpHeaders.add("token", jwtString);
        return  httpHeaders;
    }

    void notOwner(String url, HttpMethod httpMethod, Object entity){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis()-1000)
                .setSubject("")
                .claim("uid", "다른아이디")
                .claim("id", 0)
                .claim("tags", Arrays.toString(new String[]{}))
                .claim("nickname", "다른닉네임")
                .claim("lastUpdatedTime", System.currentTimeMillis())
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .setExpiration(new Date(new Date().getTime() + 604800000))
                .compact();
        headers.add("token", jwtString);

        HttpEntity httpEntity = new HttpEntity(entity, headers);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getStatusCode().value(), is(403));
    }

    void notoken(String url, HttpMethod httpMethod, Object entity){
        HttpHeaders notokenHeader = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(entity, notokenHeader);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getStatusCode().value(), is(401));
    }
}
