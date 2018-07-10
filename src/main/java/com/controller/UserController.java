package com.controller;

import com.domain.User;
import com.service.UserServcie;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: zhangHaiWen
 * @date : 2018/6/26 0026 上午 11:19
 * @DESC :
 */
@RestController
public class UserController {

    @Resource(name = "userServcie")
    private UserServcie userServcie;

    @RequestMapping(value = "/getone")
    public User getone(){
        return  userServcie.getone();
    }

}
