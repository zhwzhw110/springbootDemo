package com.filter;

import com.cache.CustomCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
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
    *@params:  * @param HashedCredentialsMatcher
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
    *@Desc: 配置sessionDao
    *@params:  * @param null
    *@Date: 2018/7/20 0020 下午 3:18
    */
    @Bean(value = "redisSessionDao")
    public RedisSessionDao redisSessionDao(){
        RedisSessionDao sessionDao = new RedisSessionDao();
        return sessionDao;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 配置DefaultWebSessionManager
    *@params:  * @param redisSessionDao
    *@Date: 2018/7/20 0020 下午 3:19
    */
    @Bean(name = "customSessionManage")
    public CustomSessionManage sessionManager(@Qualifier("redisSessionDao") RedisSessionDao redisSessionDao){
        CustomSessionManage sessionManager = new CustomSessionManage();
        sessionManager.setSessionDAO(redisSessionDao);
        return sessionManager;
    }
    /**
    *@author: zhanghHaiWen
    *@Desc: 创建自定义的缓存管理器CacheManager
    *@params:  * @param null
    *@Date: 2018/7/20 0020 下午 5:06
    */
    @Bean(name = "customCacheManager")
    public CustomCacheManager customCacheManager(){
        CustomCacheManager cacheManager = new CustomCacheManager();
        return cacheManager;
    }

    @Bean(value = "simpleCookie")
    public SimpleCookie simpleCookie(){
        //设置cookie的名字
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(6000);//设置cookie存活的时间
        return null;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 自动登录CookieManager
    *@params:  * @param null
    *@Date: 2018/7/20 0020 下午 5:32
    */
    @Bean(value = "cookieRememberMeManager")
    public CookieRememberMeManager cookieRememberMeManager(@Qualifier(value = "simpleCookie") Cookie cookie){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        return  null;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 自定义的DefaultSecurityManager
    *@params:
     *  * @param CustomRealm 自定义的Realm
     *  * @param DefaultWebSessionManager 自定义的session会话管理器
    *@Date: 2018/7/18 0018 下午 4:57
    */
    @Bean(name = "securityManager")
    public DefaultSecurityManager securityManager(@Qualifier(value = "customRealm") CustomRealm customRealm,@Qualifier(value = "customSessionManage") CustomSessionManage customSessionManage,@Qualifier(value = "customCacheManager") CustomCacheManager customCacheManager,@Qualifier(value = "cookieRememberMeManager") CookieRememberMeManager cookieRememberMeManager){
        //使用DefaultWebSecurityManager  WebSecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager ();
        securityManager.setRealm(customRealm); //配置自定义的Realm
        securityManager.setSessionManager(customSessionManage); //配置会话管理器
        securityManager.setCacheManager(customCacheManager); //配置缓存管理器
        securityManager.setRememberMeManager(cookieRememberMeManager); //设置RememberMe管理器
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
        //shiroFilterFactoryBean.setUnauthorizedUrl("403.heml");//没有权限的时候跳到的页面

        //配置过滤器链filterChainDefinitionMap必须是LinkedHashMap 因为它必须保证有序
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/secondThymeleaf.html","authc"); //authc 需要认证
        //设置权限
        //filterChainDefinitionMap.put("/example/user", "authc,perms[user:view:*]");
        //设置身份
        //filterChainDefinitionMap.put("/example/user", "authc,roles[user]");

        filterChainDefinitionMap.put("/*","anon");//anon不需要认证
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);//设置过滤器链

        return shiroFilterFactoryBean;
    }


    /**
    *@author: zhanghHaiWen
    *@Desc: 创建自定义的过滤器
    *@params:  * @param null
    *@Date: 2018/7/19 0019 下午 3:59
    */
    public RolesFilter rolesFilter(){
        RolesFilter rolesFilter = new RolesFilter();
        return  rolesFilter;
    }

}
