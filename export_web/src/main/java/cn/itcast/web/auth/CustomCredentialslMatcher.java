package cn.itcast.web.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 自定义凭证匹配器
 */
public class CustomCredentialslMatcher extends SimpleCredentialsMatcher {

    /**
     * 密码匹配判断
     * @param token 通过token获取用户输入的账号密码信息
     * @param info  获取认证后的密码信息。也是数据库中正确的密码
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // 1.获取用户输入的用户名(email)
        String email = (String) token.getPrincipal();

        // 获取用户输入的密码
        // y用户输入的密码以字符数组的形式传进来。所以要强转成字符串。可以通过调试查看
        String  password = new String((char[])token.getCredentials());

        // 3.对用户的密码进行：加密，加盐
        String encodepwd = new Md5Hash(password, email).toString();

        // 4.获取数据库中正确的密码
        String dbpassword = (String)info.getCredentials();

        // 5.判断：拿用户输入的密码和数据库中的密码进行匹对
        return encodepwd.equals(dbpassword);

    }
}
