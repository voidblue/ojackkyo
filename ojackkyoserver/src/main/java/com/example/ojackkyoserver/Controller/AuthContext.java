package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Controller.RunAfterAuthStrategy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletResponse;

public class AuthContext {
    public static void askAuthorityAndRun(String userFromEntityModel, String token,
                                            HttpServletResponse res, RunAfterAuthStrategy runable) {
        if (token == null) {
            res.setStatus(404, "토큰이 없습니다.");
        } else {
            String user = null;
            try {
                Jws<Claims> claims = Jwts.parser()
                        .setSigningKey("portalServiceFinalExam")
                        .parseClaimsJws(token);
                user = (String) claims.getBody().get("uid");
            }catch (MalformedJwtException e){
                res.setStatus(404, "토큰이 변조되었습니다.");
            }
            if (userFromEntityModel.equals(user)) {
                runable.run();
            } else
                res.setStatus(404, "접근 권한이 없습니다.");
            }
        }
}

