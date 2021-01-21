package cc.wangzijie.ipecho.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangzijie
 */
@Slf4j
@RestController
@RequestMapping("/ip")
public class IpEchoController {

    @RequestMapping("/echo")
    public String getPublicIp(HttpServletRequest request) {
        String ipAddress = getIpAddress(request);
        log.info("ipAddress = {}", ipAddress);
        return ipAddress;
    }

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取当前网络ip
     */
    private String getIpAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(LOCALHOST_IPV6.equals(ipAddress)){
                ipAddress = LOCALHOST_IPV4;
            }
        }

        // 对于通过多个代理的情况, 第一个IP为客户端真实IP, 多个IP按照','分割
        if(ipAddress != null && ipAddress.indexOf(",") > 0){
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }
}
