package com.yami.shop.api.security;

import cn.hutool.extra.servlet.ServletUtil;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.xss.XssUtil;
import com.yami.shop.security.exception.BadCredentialsException;
import com.yami.shop.security.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author SJL
 */
public class YamiAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;
    /**
     * 请求字符串的最大长度 1m
     */
    public static final int MAX_STRING_SIZE = 1024 * 1024;

    protected YamiAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, ServletException{
        if (!ServletUtil.METHOD_POST.equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" });
        }

        AuthenticationToken authenticationToken = Json.parseObject(getStringFromStream(request), AuthenticationToken.class);
        UserDetails userDetails = getUserDetails(authenticationToken);
        return handleAuthenticationToken(authenticationToken,userDetails);
    }

    /**
     * 获取用户信息
     * @param authenticationToken
     * @return
     */
    protected UserDetails getUserDetails(AuthenticationToken authenticationToken) {
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
        } catch (UsernameNotFoundException var6) {
            // 账号或密码不正确
            throw new UsernameNotFoundException("账号或密码不正确");
        }
        if (!user.isEnabled()) {
            // 账号已被锁定,请联系管理员
            throw new UsernameNotFoundException("账号已被锁定,请联系管理员");
        }

        String encodedPassword = user.getPassword();
        String rawPassword = authenticationToken.getCredentials().toString();

        // 密码不正确
        if (!passwordEncoder.matches(rawPassword,encodedPassword)){
            // 账号或密码不正确
            throw new BadCredentialsException("账号或密码不正确");
        }
        return user;
    }


    /**
     * 保存用户信息
     */
    protected AuthenticationToken handleAuthenticationToken(AuthenticationToken authentication, UserDetails userDetails) {
        // 保存用户信息
        authentication.setPrincipal(userDetails.getUsername());
        authentication.setDetails(userDetails);
        authentication.setAuthenticated(true);
        return authentication;
    }


    public String getStringFromStream(HttpServletRequest req) {
        if (req.getContentLength() > MAX_STRING_SIZE) {
            // 请求数据过长
            throw new YamiShopBindException("yami.request.data.too.long");
        }
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[1024];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0) {
                    nTotalRead = nTotalRead + nRead;
                }
            }
            if (nTotalRead > MAX_STRING_SIZE) {
                // 请求数据过长
                throw new YamiShopBindException("yami.request.data.too.long");
            }
            return XssUtil.clean(new String(bytes, 0, nTotalRead, StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
