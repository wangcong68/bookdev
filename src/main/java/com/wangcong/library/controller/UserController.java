package com.wangcong.library.controller;

import com.wangcong.library.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserController(UserRepository userRepository,CollectionRepository collectionRepository,BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * 注册
     * @param tel 电话
     * @param username 用户名
     * @param password 密码
     * @return {code:0/1,msg:手机号已被注册/注册成功}
     */
    @RequestMapping(value = "/register",params={"tel","username","password"}, method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> register(String tel,String username,String password) {

        Map<String, Object> respon = new HashMap<>();
        List<User> userList = userRepository.findAllByTel(tel);
        if(userList.size() == 0){
            User user = new User();
            user.setTel(tel);
            user.setPassword(password);
            user.setRoot(1L);
            user.setUsername(username);
            user.setHeadUrl("http://img1.imgtn.bdimg.com/it/u=719266464,336990793&fm=26&gp=0.jpg");
            //user.setHeadUrl();
            userRepository.save(user);
            respon.put("code",1);
            respon.put("msg","注册成功");
        }else{
            respon.put("code",0);//
            respon.put("msg","该手机号已被注册");
        }
        return respon;
    }

    /**
     * 登录
     * @param tel 电话
     * @param password 密码
     * @return {code:0/1/2,msg:用户名或密码错误/登录成功/用户名不存在}
     */
    @RequestMapping(value = "/login", params={"tel","password"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(String tel,
                                     String password) {
        System.out.println("heelo");
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.findByTel(tel);
        if (user == null) {
            map.put("code",2);
            map.put("msg", "用户名不存在");
            return map;
        }

        User user0 = userRepository.findByTelAndPassword(tel, password);
        if (user0 == null) {
            map.put("code",0);
            map.put("msg", "用户名或密码错误");
            return map;
        } else {
            map.put("code",1);
            map.put("msg", "登录成功");
        }
        return map;
    }

    /**
     * 我的信息
     * @param tel 电话
     * @return {code:0/1,msg:用户名不存在/成功}
     */
    @RequestMapping(value = "/myinfo", params={"tel"},method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(String tel) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userRepository.findByTel(tel);
        if(user != null){
            map.put("code",1);
            map.put("msg","请求成功");
            map.put("data",user);
        }else{
            map.put("code",0);
            map.put("msg","该用户不存在");
        }
        return map;
    }



}
