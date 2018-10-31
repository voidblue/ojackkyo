package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Exceptions.NoPermissionException;
import com.example.ojackkyoserver.Exceptions.NullTokenException;
import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import com.example.ojackkyoserver.Service.JwtContext;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarException;

@CrossOrigin(origins = "*")
@RestController
@ResponseBody
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtContext jwtContext;


    @GetMapping("/{id}")
    public User get(@PathVariable Integer id){
        System.out.println(userRepository.findById(id).get().toString());
        return userRepository.findById(id).get();
    }

    @GetMapping("/list/search")
    public List<User> list(HttpServletResponse res, @RequestParam String xxx){
        res.setHeader("Access-Control-Allow-Origin","*");
        return userRepository.findAll();
    }

    @GetMapping("/uidCheck/{uid}")
    public HashMap uidCheck(@PathVariable String uid, HttpServletResponse res) throws IOException {
        if(!userRepository.existsByUid(uid)) {
            HashMap hashMap = new HashMap();
            hashMap.put("결과", "중복되지 않았습니다.");
            return hashMap;
        }else{
            HashMap hashMap = new HashMap();
            res.sendError(404, "아이디가 중복되었습니다.");
            return null;
        }

    }
    @GetMapping("/nicknameCheck/{nickname}")
    public HashMap duplicationCheck(@PathVariable String nickname, HttpServletResponse res) throws IOException {
        if(!userRepository.existsByNickname(nickname)) {
            HashMap hashMap = new HashMap();
            hashMap.put("결과", "중복되지 않았습니다.");
            return hashMap;
        }else{
            res.sendError(404, "아이디가 중복되었습니다.");
            return null;
        }

    }

    @PostMapping
    public User create(@RequestBody User user, HttpServletResponse res) throws IOException {
        user.setLastUpdatedTime(System.currentTimeMillis());
        if (userRepository.existsByUid(user.getUid()) || userRepository.existsByNickname(user.getNickname())){
            res.sendError(400, "중복 키 에러입니다.");
            return null;
        }else {
            String[] basicTags = {"자유", "구직", "질의응답"};
            String[] tags = user.getTags();
            if(tags!=null) {
                if (tags.length >= 6) {
                    res.sendError(400, "태그가 너무 많습니다.");
                    return null;
                } else {
                    String[] signInTag = new String[tags.length + 3];
                    for (int i = 0; i < tags.length; i++) {
                        signInTag[i] = tags[i];
                        user.setNullTags(signInTag);
                    }
                    for (int i = tags.length; i < signInTag.length; i++) {
                        signInTag[i] = basicTags[i-signInTag.length];
                        user.setNullTags(signInTag);
                    }
                }
            }else{
                user.setNullTags(basicTags);
            }
            return userRepository.save(user);
        }
    }

    //TODO 코드 개선
    @PutMapping
    public User update(@RequestBody User user, HttpServletResponse res) throws IOException {
        User result = null;
        try {
            jwtContext.entityOwnerCheck(user.getNickname());
            if (userRepository.existsByUid(user.getUid())){
                user.setLastUpdatedTime(System.currentTimeMillis());
                userRepository.save(user);
                result = userRepository.getOne(user.getId());
            }else{
                try {
                    res.sendError(400, "수정할 유저가 없습니다.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (JwtException e){
            res.sendError(400, e.getMessage());
        } catch (NoPermissionException e) {
            res.sendError(403, e.getMessage());
        } catch (NullTokenException e) {
            res.sendError(401, e.getMessage());
        }
        return result;
    }

    //TODO 계정 삭제는 논의 후 진행

}
