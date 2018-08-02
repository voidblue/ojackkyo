package com.example.ojackkyoserver;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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
                .claim("uid", "testuser")
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
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
                .claim("nickname", "")
                .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                .compact();
        headers.add("token", jwtString);

        HttpEntity httpEntity = new HttpEntity(entity, headers);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getBody(), is(nullValue()));
    }

    void notoken(String url, HttpMethod httpMethod, Object entity){
        HttpHeaders notokenHeader = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(entity, notokenHeader);
        ResponseEntity resultUser = restTemplate.exchange(url , httpMethod, httpEntity, entity.getClass());
        assertThat(resultUser.getBody(), is(nullValue()));
    }
}
