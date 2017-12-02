package com.sgsl.foodsee.cloud.dingtalk;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by maoxianzhi.
 * CreateTime: 2017/10/12
 * ModifyBy  maoxianzhi
 * ModifyTime: 2017/10/12
 * Description:获取本机IP
 */

@Slf4j
@AllArgsConstructor
public class LocalIPGetter {
    private final String includeLocalIpPrefix;
    private List<String> excludeLocalIpPrefixs = new ArrayList<>();

    public String getLocalIP() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                int addressCount = 0;
                String hostAddress = null;
                while (addresses.hasMoreElements()) {
                    addressCount++;
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        hostAddress = ip.getHostAddress();
                        if (StringUtils.isNotEmpty(includeLocalIpPrefix) && hostAddress.startsWith(includeLocalIpPrefix)) {
                            return hostAddress;
                        }

                        boolean excluded = false;
                        for (String excludeLocalIpPrefix : excludeLocalIpPrefixs) {
                            if (hostAddress.startsWith(excludeLocalIpPrefix)) {
                                excluded = true;
                                break;
                            }
                        }

                        if (!excluded) {
                            return hostAddress;
                        }
                    }
                }

                if (addressCount == 1) {
                    return hostAddress;
                }
            }

        } catch (SocketException e) {
            log.error("localIP is null", e);
        }

        return null;
    }
}
