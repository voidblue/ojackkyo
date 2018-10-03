package com.example.ojackkyoserver.Service;

import com.example.ojackkyoserver.Exceptions.*;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;


//TODO jwtException는 개념상은 런타임 exception이 아닌 거 같은데?? 여튼 이거떄문에 상위클래스에서 자동으로 catch못하는듯, 대체할 수 있나 확인
@Service
@Scope(value = SCOPE_SINGLETON)
public class JwtContext {
    @Autowired
    UserRepository userRepository;

    public void entityOwnerCheck(String nicknameFromEntityModel, String token) throws NoPermissionException, JwtException, NullTokenException {
        nullTokenCheck(token);
        Claims claims = getDecodedToken(token);
        String nickname = (String) claims.get("nickname");
        resourceOwnerCheck(nicknameFromEntityModel, nickname);
        updatedUserCheck(claims);
    }
    public void loginCheck(String token) throws JwtException, NullTokenException {
        nullTokenCheck(token);
        Claims claims = getDecodedToken(token);
        updatedUserCheck(claims);
    }

    public void entityOwnerCheck(String nicknameFromEntityModel) throws NoPermissionException, JwtException, NullTokenException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        entityOwnerCheck(nicknameFromEntityModel, req.getHeader("token"));
    }
    public void loginCheck() throws JwtException, NullTokenException {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        loginCheck(req.getHeader("token"));
    }

    //TODO 싱글턴으로 만들어 그때그때 해시해서 비교 vs 객체를 프로토 타입 스코프로 해서 하나의 객체는 한번의 디코드만
    public Claims getDecodedToken(String token) throws MalformedJwtException, SignatureException, IllegalArgumentException{
        return Jwts.parser()
                .setSigningKey("portalServiceFinalExam")
                .parseClaimsJws(token).getBody();
    }
    public Claims getDecodedToken() throws MalformedJwtException, SignatureException, IllegalArgumentException{
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return getDecodedToken(req.getHeader("token"));
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

    private void updatedUserCheck(Claims claims) throws ExpiredByUserUpdateException {
        System.out.println(claims.toString());
        Long lastUpdatedTime = (Long) claims.get("lastUpdatedTime");
        Long timeCreated = (Long) claims.get("timeCreated");

        System.out.println(lastUpdatedTime);
        if(lastUpdatedTime > timeCreated){
            throw new ExpiredByUserUpdateException();
        }
    }


}