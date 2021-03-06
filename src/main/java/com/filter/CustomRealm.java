package com.filter;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangHaiWen
 * @date : 2018/7/11 0011 下午 3:24
 * @DESC :
 */
public class CustomRealm extends AuthorizingRealm {
    /**
     * 授权信息
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        System.out.println("查询数据库"+username);
        //数据库查询数据根据username
        List<String> permissions = new ArrayList<String>();
        List<String> roles = new ArrayList<String>();

        permissions.add("user:select");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(permissions);
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }

    /**
     * 认证
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        //根据用户名查找数据，查找用户
        if(null == username){
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username,"202cb962ac59075b964b07152d234b70",getName());
       // authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("asda")); 加盐
        return authenticationInfo;
    }

    //加密的测试类
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123");
        System.out.println(md5Hash.toString());
    }
}
