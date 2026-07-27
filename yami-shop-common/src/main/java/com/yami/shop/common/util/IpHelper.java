/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;

import jakarta.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * IP帮助工具
 * @author lanhai
 */
public class IpHelper {
    private static final String UNKNOWN = "unknown";

    /**
     * 得到用户的真实地址,如果有多个就取第一个
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (isUnknown(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ips = ip.split(",");
        return ips[0].trim();
    }

    /**
     * 获取本地IP
     * @return
     */
    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    String hostAddress = inetAddress.getHostAddress();
                    if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress() && !hostAddress.contains(":")) {
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    private static boolean isUnknown(String ip) {
        return ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }
}
