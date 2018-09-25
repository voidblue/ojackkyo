package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.*;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class AuthService {
    @Autowired
    UserRepository userRepository;
    //TODO 패스워드 변경시 토큰 만료 정책
    /*
    TODO 람다식 안쓰고 에러처리만 해주는 걸로도 괜찮을것 같은데...
    람다식을 꼭 쓸 필요가 없고 일어나는 에러위로 보내면서 위에서 보기에도
    무엇을 검사하는지도 알 수 있을듯
    */

    public void askAuthorityAndRun(String nicknameFromEntityModel, String token, Runnable runnable) {
        try {
            nullTokenCheck(token);
            Claims claims = getDecodedToken(token);
            String nickname = (String) claims.get("nickname");
            resourceOwnerCheck(nicknameFromEntityModel, nickname);
            updatedUserCheck(claims,nickname);
            runnable.run();
        } catch (UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            for(StackTraceElement x : e.getStackTrace()){
                System.out.println(x);
            }
            sendErrorWithCatchingIOException(e, 400);
        } catch (NullTokenException|ExpiredJwtException|TokenExpiredException e){
            sendErrorWithCatchingIOException(e, 401);
        } catch (NoPermissionException e) {
            sendErrorWithCatchingIOException(e, 403);
        }
    }
    public void askLoginedAndRun(String token, Runnable runnable){
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        try {
            nullTokenCheck(token);
            Claims claims = getDecodedToken(token);
            String nickname = (String) claims.get("nickname");
            updatedUserCheck(claims, nickname);
            runnable.run();
        } catch (UnsupportedJwtException|MalformedJwtException|SignatureException|IllegalArgumentException e) {
            sendErrorWithCatchingIOException(e, 400);
        } catch (NullTokenException|ExpiredJwtException|TokenExpiredException e){
            sendErrorWithCatchingIOException(e, 401);
        }
    }

    //TODO 그때그때 해시해서 비교 vs 객체를 스코프로 해서 하나의 객체는 한번의 디코드만 하게d각 클래스
    public Claims getDecodedToken(String token) throws MalformedJwtException, SignatureException, IllegalArgumentException{
        return Jwts.parser()
                .setSigningKey("portalServiceFinalExam")
                .parseClaimsJws(token).getBody();
    }


    private void nullTokenCheck(String token) throws NullTokenException {
        if(token == null){
            throw new NullTokenException();
        }
    }

    private void sendErrorWithCatchingIOException(Throwable e, int statusCode){
        HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        try {
            res.sendError(statusCode, e.getMessage());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void resourceOwnerCheck(String nicknameFromEntityModel, String nicknameInToken) throws NoPermissionException {
        if(!nicknameFromEntityModel.equals(nicknameInToken)){
            throw new NoPermissionException();
        }
    }

    private void updatedUserCheck(Claims claims, String nickname) throws TokenExpiredException {
        String updatedTimes = (String) claims.get("updateTimes");
        User user = userRepository.findByNickname(nickname);
        if(!user.getUpdateTimes().toString().equals(updatedTimes)){
            throw new TokenExpiredException();
        }
    }


}