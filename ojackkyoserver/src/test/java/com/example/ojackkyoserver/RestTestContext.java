package com.example.ojackkyoserver;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class RestTestContext {
    private TestRestTemplate restTemplate;

    RestTestContext(TestRestTemplate restTemplate){
        this.restTemplate = restTemplate;
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
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .setExpiration(new Date(new Date().getTime() + 604800))
                .compact();
        httpHeaders.add("token", jwtString);
        return  httpHeaders;
    }

    void notOwner(String url, HttpMethod httpMethod, Object entity){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis())
                .setSubject("")
                .claim("uid", "다른아이디")
                .claim("id", 0)
                .claim("tags", Arrays.toString(new String[]{}))
                .claim("nickname", "다른닉네임")
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .setExpiration(new Date(new Date().getTime() + 604800))
                .compact();
        headers.add("token", jwtString);

        HttpEntity httpEntity = new HttpEntity(entity, headers);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getStatusCode().value(), is(403));
        assertThat(resultUser.getBody(), is(nullValue()));
    }

    void notoken(String url, HttpMethod httpMethod, Object entity){
        HttpHeaders notokenHeader = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(entity, notokenHeader);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getBody(), is(nullValue()));
        assertThat(resultUser.getStatusCode().value(), is(401));
    }
}
