package com.service.serviceImp;

import com.dao.UserMapper;
import com.domain.User;
import com.service.UserServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: zhangHaiWen
 * @date : 2018/6/26 0026 上午 11:23
 * @DESC :
 */
@Service(value = "userServcie")
public class UserServiceImp implements UserServcie{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getone() {
        return userMapper.getone();
    }
}
