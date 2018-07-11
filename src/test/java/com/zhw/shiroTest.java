package com.zhw;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/11 0011 上午 10:16
 * @DESC :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class shiroTest {
    SimpleAccountRealm realm = new SimpleAccountRealm();
    @Before
    public void init(){
        realm.addAccount("zhangsan","123");
    }
    @Test
    public void contextLoads() {
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //获取一个主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");
        subject.login(token);

        //判断是否认证
        System.out.println(subject.isAuthenticated());

    }


}

