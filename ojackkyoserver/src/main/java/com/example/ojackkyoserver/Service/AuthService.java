package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.*;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

@Service
@Scope(value = SCOPE_REQUEST)
public class AuthService {
    private HttpServletResponse res;

    AuthService(){
        res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }

    //TODO 패스워드 변경시 토큰 만료 정책
    private Jws<Claims> decodedToken = null;
    public void askAuthorityAndRun(String nicknameFromEntityModel, String token, Runnable runnable) {
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
    public void askLoginedAndRun(String token,
                                        HttpServletResponse res, Runnable runnable) {
        try {
            nullTokenCheck(token);
            decodeToken(token);
            runnable.run();
        } catch (NullTokenException|UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            res.setStatus(400, e.getMessage());
        } catch (ExpiredJwtException e){
            res.setStatus(401, e.getMessage());
        }
    }

    public Jws<Claims> getDecodedToken(String token) throws MalformedJwtException, SignatureException, IllegalArgumentException{
        if(decodedToken == null) {
            decodeToken(token);
        }
        return decodedToken;
     }

    private void decodeToken(String token) throws MalformedJwtException, SignatureException, IllegalArgumentException{
        decodedToken = Jwts.parser()
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