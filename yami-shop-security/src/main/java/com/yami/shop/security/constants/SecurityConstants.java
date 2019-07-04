/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.security.constants;

public interface SecurityConstants {
    /**
     * 前缀
     */
    String YAMI_PREFIX = "yami_";

    /**
     * oauth 相关前缀
     */
    String OAUTH_PREFIX = "oauth:";

    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "yami_oauth:client:details";

    /**
     * sys_oauth_client_details 表的字段，不包括client_id、client_secret
     */
    String CLIENT_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    /**
     * JdbcClientDetailsService 查询语句
     */
    String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS
            + " from tz_oauth_client_details";

    /**
     * 默认的查询语句
     */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /**
     * 按条件client_id 查询
     */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /**
     * 小程序登陆(暂时与数据库tz_oauth_client_details 中一致)
     */
    String SPRING_SECURITY_RESTFUL_TYPE_MINI_APP = "mini_app";
    /**
     * 公众号登陆(暂时与数据库tz_oauth_client_details 中一致)
     */
    String SPRING_SECURITY_RESTFUL_TYPE_MP = "weixin_mp";
    /**
     * 后台账号密码登陆(暂时与数据库tz_oauth_client_details 中一致)
     */
    String SPRING_SECURITY_RESTFUL_TYPE_ADMIN = "admin";

    String SPRING_SECURITY_RESTFUL_IMAGE_CODE = "imageCode";
}
