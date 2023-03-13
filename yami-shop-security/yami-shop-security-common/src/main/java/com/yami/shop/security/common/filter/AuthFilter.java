/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.handler.HttpHandler;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.common.adapter.AuthConfigAdapter;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.util.AuthUserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 授权过滤，只要实现AuthConfigAdapter接口，添加对应路径即可：
 *
 * @author 菠萝凤梨
 * @date 2022/3/25 17:33
 */
@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private AuthConfigAdapter authConfigAdapter;

    @Autowired
    private HttpHandler httpHandler;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestUri = req.getRequestURI();

        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();

        AntPathMatcher pathMatcher = new AntPathMatcher();
        // 如果匹配不需要授权的路径，就不需要校验是否需要授权
        if (CollectionUtil.isNotEmpty(excludePathPatterns)) {
            for (String excludePathPattern : excludePathPatterns) {
                if (pathMatcher.match(excludePathPattern, requestUri)) {
                    chain.doFilter(req, resp);
                    return;
                }
            }
        }

        String accessToken = req.getHeader("Authorization");
        // 也许需要登录，不登陆也能用的uri
        boolean mayAuth = pathMatcher.match(AuthConfigAdapter.MAYBE_AUTH_URI, requestUri);


        UserInfoInTokenBO userInfoInToken = null;

        try {
            // 如果有token，就要获取token
            if (StrUtil.isNotBlank(accessToken)) {
                userInfoInToken = tokenStore.getUserInfoByAccessToken(accessToken, true);
            }
            else if (!mayAuth) {
                // 返回前端401
                httpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
                return;
            }
            // 保存上下文
            AuthUserContext.set(userInfoInToken);

            chain.doFilter(req, resp);

        }catch (Exception e) {
            // 手动捕获下非controller异常
            if (e instanceof YamiShopBindException) {
                httpHandler.printServerResponseToWeb((YamiShopBindException) e);
            } else {
                throw e;
            }
        } finally {
            AuthUserContext.clean();
        }
    }
}
