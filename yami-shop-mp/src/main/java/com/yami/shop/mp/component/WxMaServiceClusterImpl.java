package com.yami.shop.mp.component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.hutool.http.HttpUtil;
import com.yami.shop.common.exception.YamiShopBindException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * WxMaServiceImpl 在集群模式获取accessToken的方式
 * @author LGH
 */
@Slf4j
public class WxMaServiceClusterImpl extends WxMaServiceImpl {

    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    private RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if (!this.getWxMaConfig().isAccessTokenExpired() && !forceRefresh) {
            return this.getWxMaConfig().getAccessToken();
        }

        RLock rLock = redissonClient.getLock(REDISSON_LOCK_PREFIX + ":WxMaServiceCluster:getAccessToken");

        try {
            boolean lockSuccess;
            try {
                lockSuccess = rLock.tryLock(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                return this.getWxMaConfig().getAccessToken();
            }

            if (!lockSuccess) {
                throw new YamiShopBindException("服务器繁忙，请稍后再试");
            }

            if (!this.getWxMaConfig().isAccessTokenExpired()) {
                return this.getWxMaConfig().getAccessToken();
            }

            String url = String.format(WxMaService.GET_ACCESS_TOKEN_URL, this.getWxMaConfig().getAppid(),
                    this.getWxMaConfig().getSecret());
            String resultContent = HttpUtil.get(url);
            WxError error = WxError.fromJson(resultContent);
            if (error.getErrorCode() != 0) {
                throw new WxErrorException(error);
            }
            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
            this.getWxMaConfig().updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());

            return this.getWxMaConfig().getAccessToken();

        } finally {
            rLock.unlock();
        }

    }
}
