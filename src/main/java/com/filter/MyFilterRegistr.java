package com.filter;

import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    /**
    *@author: zhanghHaiWen
    *@Desc: 自定义的DefaultSecurityManager
    *@params:  * @param null
    *@Date: 2018/7/18 0018 下午 4:57
    */
    @Bean
    public DefaultSecurityManager securityManager(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(new CustomRealm());
        return securityManager;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 设置shrio的过滤器
    *@params:  * @param null
    *@Date: 2018/7/18 0018 下午 4:55
    */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setLoginUrl("/login");//登录的URL
        shiroFilterFactoryBean.setUnauthorizedUrl("403.heml");//未认证的页面

        //配置过滤器链filterChainDefinitionMap必须是LinkedHashMap 因为它必须保证有序
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login,html","anon"); //anon不需要认证
        filterChainDefinitionMap.put("/*","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);//设置过滤器链

        return shiroFilterFactoryBean;
    }

    /**
    *@author: zhanghHaiWen
    *@Desc: 自定义的一些Bean
    *@params:  * @param null
    *@Date: 2018/7/18 0018 下午 4:54
    */
     @Bean
     public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter()); //注册shiro提供的拦截器
        filterRegistrationBean.addUrlPatterns("/*"); //拦截所有的请求
         //添加不需要忽略的格式信息.
         //filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


}
