package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.exceptions.InvalidLoginException;
import com.example.ojackkyoserver.Model.Auth;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("auth")
@Scope(SCOPE_SINGLETON)
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @PostMapping("/login")
    public HashMap login(@RequestBody Auth auth, HttpServletResponse res) throws IOException {
        Optional<User> user = userRepository.findByUid(auth.getUid());
        try {
            LoginCheck(user, auth);
            String jwtString = Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setHeaderParam("issueDate", System.currentTimeMillis())
                    .setSubject("")
                    .claim("uid", user.get().getUid())
                    .claim("id", user.get().getId())
                    .claim("nickname", user.get().getNickname())
                    .claim("lastUpdatedTime", user.get().getLastUpdatedTime())
                    .claim("timeCreated", System.currentTimeMillis())
                    .claim("tags", Arrays.toString(user.get().notNullTags().toArray()))
                    .setExpiration(new Date(new Date().getTime() + 604800*1000))
                    .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                    .compact();
            HashMap<String ,String> hashMap = new HashMap<>();
            hashMap.put("token", jwtString);
            return hashMap;
        } catch (InvalidLoginException e) {
            res.sendError(400, e.getMessage());
            return null;
        }



    }

    private void LoginCheck(Optional<User> user, Auth auth) throws InvalidLoginException {
        if(!user.isPresent()){
            throw new InvalidLoginException();
        }else if(!user.get().getPassword().equals(auth.getPassword())){
            throw new InvalidLoginException();
        }
    }

}