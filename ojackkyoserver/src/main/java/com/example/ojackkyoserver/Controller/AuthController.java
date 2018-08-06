package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.Auth;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @PostMapping("/login")
    public HashMap login(@RequestBody Auth auth, HttpServletResponse res){
        String jwtString = null;
        try{
            Optional<User> user = userRepository.findByUid(auth.getUid());
            if(user.get().getPassword().equals(auth.getPassword())){
                jwtString = Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setHeaderParam("issueDate", System.currentTimeMillis())
                        .setSubject("")
                        .claim("uid", user.get().getUid())
                        .claim("id", user.get().getId())
                        .claim("nickname", user.get().getNickname())
//                        .claim("imageName", user.get().getImageName())
                        .signWith(SignatureAlgorithm.HS512, "portalServiceFinalExam")
                        .compact();
            }else{
                res.setStatus(404);
            }
        }catch (EmptyResultDataAccessException e){
            res.setStatus(500);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("token", jwtString);
        return hashMap;

    }
}