package com.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 3:31
 * @DESC : 自定义的SessionManage 为了减少对Redis的访问
 */
public class CustomSessionManage extends DefaultSessionManager{
    /**
     *@author: zhanghHaiWen
     *@Desc: 重写方法:将session存放存放到request里面，减少对redis的访问
     *@params:  * @param sessionKey
     *@Date: 2018/7/20 0020 下午 3:35
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if(sessionKey instanceof  WebSessionKey){
            request =  ((WebSessionKey)sessionKey).getServletRequest();
        }
        //如果request中存在，那么直接从request中取出
        if(request!=null && sessionId!=null){
            Session session = (Session) request.getAttribute(sessionId.toString());
            if(session!=null){
                return session;
            }
        }
        //如果不存在，就先冲redis中取出，session
        Session session = super.retrieveSession(sessionKey); // --->sessionDAO.readSession(sessionId); 还是从sessionDAO中取出
        if(request!=null && session!=null){
            request.setAttribute(sessionId.toString(),session);
        }
        return session;
    }
}
