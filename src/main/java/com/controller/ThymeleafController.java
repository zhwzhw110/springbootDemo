package com.controller;

import com.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/10 0010 下午 3:57
 * @DESC : 实验Thymeleaf引擎
 */
@Controller
public class ThymeleafController {

    @RequestMapping(value = "/thymeleafDemo")
    public String thymeleafDemo(Model model){
        model.addAttribute("name","zhangsan");
        model.addAttribute("result_true",true);
        model.addAttribute("result_num",100);

        List<User> userList = new ArrayList<User>();
        int i = 0;
        while (i<4){
            User user = new User();
            user.setHost("192.168.0."+i);
            user.setUser("zhanghaiwen0"+i);
            userList.add(user);
            i++;
        }
        model.addAttribute("userlist",userList);
        return "firstThymeleaf";
    }


    @RequestMapping(value = "/secondThymeleaf")
    public String secondThymeleaf(){
        return "secondThymeleaf";
    }

}
