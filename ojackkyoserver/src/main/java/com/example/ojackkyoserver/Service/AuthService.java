package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.*;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class AuthService {
    //TODO 패스워드 변경시 토큰 만료 정책

    public void askAuthorityAndRun(String nicknameFromEntityModel, String token, Runnable runnable) {
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        try {
            nullTokenCheck(token);
            Jws<Claims> claims = getDecodedToken(token);
            String nickname = (String) claims.getBody().get("nickname");
            resourceOwnerCheck(nicknameFromEntityModel, nickname);
            runnable.run();
        } catch (NullTokenException|UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            res.setStatus(400, e.getMessage());
        } catch (ExpiredJwtException e){
            res.setStatus(401, e.getMessage());
        } catch (NoPermissionException e) {
            res.setStatus(403, e.getMessage());
        }
    }
    public void askLoginedAndRun(String token, Runnable runnable){
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        try {
            nullTokenCheck(token);
            getDecodedToken(token);
            runnable.run();
        } catch (NullTokenException|UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            res.setStatus(400, e.getMessage());
        } catch (ExpiredJwtException e){
            res.setStatus(401, e.getMessage());
        }
    }

    //TODO 그때그때 해시해서 비교 vs 객체를 스코프로 해서 하나의 객체는 한번의 디코드만 하게
    public Jws<Claims> getDecodedToken(String token) throws MalformedJwtException, SignatureException, IllegalArgumentException{
        return Jwts.parser()
                .setSigningKey("portalServiceFinalExam")
                .parseClaimsJws(token);
    }


    private void nullTokenCheck(String token) throws NullTokenException {
        if(token == null){
            throw new NullTokenException();
        }
    }


    private void resourceOwnerCheck(String nicknameFromEntityModel, String nicknameInToken) throws NoPermissionException {
        if(!nicknameFromEntityModel.equals(nicknameInToken)){
            throw new NoPermissionException();
        }
    }


}