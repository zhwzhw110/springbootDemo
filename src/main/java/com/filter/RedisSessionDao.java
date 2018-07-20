package com.filter;

import com.redis.JedisUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/20 0020 下午 2:09
 * @DESC : 重写SessionDao的增删改查方法
 */

public class RedisSessionDao extends AbstractSessionDAO{

    @Resource(name = "jedisUtils")
    private JedisUtils jedisUtils;

    private final static String shiro_session_prifix="shiro_session:"; //添加前缀

    //redis 的 key就是前缀+sessionId组成，redi中通过存储二进制的形式存储数据
    private byte[] getkey(String key){
        return  (shiro_session_prifix + key).getBytes();
    }

    //保存session的方法
    private void saveSession(Session session){
        if( session!=null && session.getId() != null){
            byte[] key = getkey(session.getId().toString()); //key: 前缀+sessionId
            byte[] value =  SerializationUtils.serialize(session); //value ：session对象
            jedisUtils.set(key,value); //保存到缓存中
            jedisUtils.expire(key,600); //设置超时时间 600秒
        }
    }

   /**
   *@author: zhanghHaiWen
   *@Desc: 创建session
   *@params:  * @param null
   *@Date: 2018/7/20 0020 下午 2:11
   */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session); //创建sessionId -->> UUID.randomUUID().toString();
        assignSessionId(session,sessionId); //生成的sessionId和session对象绑定
        saveSession(session);
        return sessionId;
    }

    /**
     *@author: zhanghHaiWen
     *@Desc: 获得session的方法
     *@params:  * @param null
     *@Date: 2018/7/20 0020 下午 2:11
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("访问redis");
        if(sessionId == null){
            return null;
        }
        byte[] key = getkey(sessionId.toString());
        byte[] value =jedisUtils.get(key);
        return (Session) SerializationUtils.deserialize(value); //byte数组反序列化成session对象
    }

    /**
     *@author: zhanghHaiWen
     *@Desc: 更新session的方法,直接更新一个session
     *@params:  * @param null
     *@Date: 2018/7/20 0020 下午 2:11
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    /**
     *@author: zhanghHaiWen
     *@Desc: 删除session的方法
     *@params:  * @param null
     *@Date: 2018/7/20 0020 下午 2:11
     */
    @Override
    public void delete(Session session) {
        if(session==null||session.getId()==null){
            return;
        }
        byte[] key = getkey(session.getId().toString());
        jedisUtils.del(key);
    }

    /**
     *@author: zhanghHaiWen
     *@Desc: 获取存活的session
     *@params:  * @param null
     *@Date: 2018/7/20 0020 下午 2:11
     */
    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtils.keys(shiro_session_prifix);
        Set<Session> sessions = new HashSet<Session>();
        if(CollectionUtils.isEmpty(keys)){
            return  sessions;
        }
        for (byte[] key : keys){
            byte[] value = jedisUtils.get(key);
            sessions.add((Session) SerializationUtils.deserialize(value));
        }
        return sessions;
    }
}
