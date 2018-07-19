package com.filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/18 0018 下午 4:32
 * @DESC : 自定义filter 注册器 Realm的一些配置
 */
@Configuration
public class MyFilterRegistr {

    //配置自定义的密码比较器
    @Bean(name="hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        return new HashedCredentialsMatcher();
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 自定义的Realm配置
    *@params:  * @param null
    *@Date: 2018/7/19 0019 上午 11:26
    */
    @Bean(name = "customRealm")
    public CustomRealm customRealm(@Qualifier(value = "hashedCredentialsMatcher")  HashedCredentialsMatcher hashedCredentialsMatcher){
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");//使用加密的算法名称
        hashedCredentialsMatcher.setHashIterations(1); //散列次数
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return customRealm;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 自定义的DefaultSecurityManager
    *@params:  * @param CustomRealm 自定义的Realm
    *@Date: 2018/7/18 0018 下午 4:57
    */
    @Bean(name = "securityManager")
    public DefaultSecurityManager securityManager(@Qualifier(value = "customRealm") CustomRealm customRealm){
        //使用DefaultWebSecurityManager  WebSecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager ();
        securityManager.setRealm(customRealm);
        return securityManager;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 设置shrio的过滤器
    *@params:  * @param SecurityManager 安全管理器
    *@Date: 2018/7/18 0018 下午 4:55
    */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier(value = "securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //shiroFilterFactoryBean.setLoginUrl("/login");//登录的URL
        //shiroFilterFactoryBean.setUnauthorizedUrl("403.heml");//未认证的页面

        //配置过滤器链filterChainDefinitionMap必须是LinkedHashMap 因为它必须保证有序
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/secondThymeleaf.html","authc"); //authc 需要认证
        filterChainDefinitionMap.put("/*","anon");//anon不需要认证
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);//设置过滤器链

        return shiroFilterFactoryBean;
    }




}
