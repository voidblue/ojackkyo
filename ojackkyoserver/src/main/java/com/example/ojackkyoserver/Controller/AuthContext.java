package com.example.ojackkyoserver.Controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import javax.servlet.http.HttpServletResponse;

public class AuthContext {
    public static void askAuthorityAndRun(String nicknameFromEntityModel, String token,
                                            HttpServletResponse res, Runnable runnable) {
        if (token == null) {
            res.setStatus(404, "토큰이 없습니다.");
        } else {
            String user = null;
            try {
                Jws<Claims> claims = decodeToken(token);
                user = (String) claims.getBody().get("nickname");
            }catch (MalformedJwtException e){
                res.setStatus(404, "토큰이 변조되었습니다.");
            }
            if (nicknameFromEntityModel.equals(user)) {
                runnable.run();
            } else
                res.setStatus(404, "접근 권한이 없습니다.");
            }
        }

    public static void askLoginedAndRun(String token,
                                        HttpServletResponse res, Runnable runnable) {
        if (token == null) {
            res.setStatus(404, "토큰이 없습니다.");
        } else {
            try {
                Jws<Claims> claims = decodeToken(token);
                runnable.run();
            } catch (MalformedJwtException e) {
                res.setStatus(404, "토큰이 변조되었습니다.");
            }
        }
    }

    public static Jws<Claims> decodeToken(String token){
        return Jwts.parser()
                .setSigningKey("portalServiceFinalExam")
                .parseClaimsJws(token);
    }

}