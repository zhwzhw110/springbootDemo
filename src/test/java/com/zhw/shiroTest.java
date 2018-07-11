package com.zhw;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
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
    SimpleAccountRealm realm1 = new SimpleAccountRealm();
    SimpleAccountRealm realm2 = new SimpleAccountRealm();
    IniRealm iniRealm = new IniRealm("classpath:userRealm.ini");
    JdbcRealm jdbcRealm = new JdbcRealm();

    @Before
    public void init(){
        realm1.addAccount("zhangsan","123");//认证realm
        realm2.addAccount("zhangsan","123","del","add");//授权realm

        //使用jdbcRealm
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setUrl("");
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true); //启动权限校验
    }
    /**
    *@author: zhanghHaiWen
    *@Desc: shiro认证过程
    *@params:  * @param null
    *@Date: 2018/7/11 0011 下午 1:55
    */
    @Test
    public void contextLoads() {
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm1);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //获取一个主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");
        subject.login(token);

        //判断是否认证
        System.out.println(subject.isAuthenticated());
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 授权
    *@params:  * @param null
    *@Date: 2018/7/11 0011 下午 2:02
    */
    @Test
    public void author(){
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm2);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //获取一个主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");
        //授权
        subject.login(token);
        subject.checkRoles("del","add","select");
    }

    /**
     *@author: zhanghHaiWen
     *@Desc: iniRealm
     *@params:  * @param null
     *@Date: 2018/7/11 0011 下午 2:02
     */
    @Test
    public void iniRealmTest(){
        //构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //获取一个主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123456");
        //授权
        subject.login(token);
        System.out.println(subject.isAuthenticated());
        subject.checkRoles("role1"); //校验角色身份
        subject.checkPermission("user:update");//校验权限
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 使用jdbcRealm 测试用例
    *@params:  * @param null
    *@Date: 2018/7/11 0011 下午 3:12
    */
    @Test
    public void jdbcRealmTest(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");
        subject.login(token);
        System.out.println(subject.isAuthenticated()); //是否认证
        subject.checkRole("role1");//校验角色
        subject.checkPermission("user:delete");//校验用户权限
    }

}

