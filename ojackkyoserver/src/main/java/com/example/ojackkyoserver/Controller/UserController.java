package com.example.ojackkyoserver.Controller;

import com.example.ojackkyoserver.Model.User;
import com.example.ojackkyoserver.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@ResponseBody
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRepository userRepository;

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

    @GetMapping("/duplicationCheck/{uid}")
    public HashMap duplicationCheck(@PathVariable String uid, HttpServletResponse res){
        if(!userRepository.existsByUid(uid)) {
            HashMap hashMap = new HashMap();
            hashMap.put("결과", "중복되지 않았습니다.");
            return hashMap;
        }else{
            HashMap hashMap = new HashMap();
            res.setStatus(404, "아이디가 중복되었습니다.");
            return null;
        }

    }


    @PostMapping
    public User create(@RequestBody User user, HttpServletResponse res) throws IOException {
        if (userRepository.existsByUid(user.getUid())){
            res.sendError(400, "중복 키 에러입니다.");
            return null;
        }else {
            return userRepository.save(user);
        }
    }

    //TODO 코드 개선
    @PutMapping
    public User update(@RequestBody User user, HttpServletRequest req, HttpServletResponse res){
        User[] userholder = new User[1];
        AuthContext.askAuthorityAndRun(user.getUid(), req.getHeader("token"), res, ()->{
            if (userRepository.existsByUid(user.getUid())){
                userholder[0] = userRepository.save(user);
            }else{
                userholder[0] = null;
                try {
                    res.sendError(400, "수정할 유저가 없습니다.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println(userholder[0]);
        return userholder[0];
    }

    //TODO 계정 삭제는 논의 후 진행
//    @DeleteMapping("/{id}")
//    public void Delete(@PathVariable Integer id){
//
//    }
}
