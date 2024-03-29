package io.backQL.BackIo.utils;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class RequestUtils {
    public static String getIpAddress(HttpServletRequest request){
        String ipAddress = "Unknown Ip";
        if (request != null) {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || "".equals(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        }
        return ipAddress;
    }
    public static String getDevice(HttpServletRequest request) {
        UserAgentAnalyzer userAgentAnalyzer = UserAgentAnalyzer.newBuilder().hideMatcherLoadStats().withCache(1000).build();
        UserAgent agent = userAgentAnalyzer.parse(request.getHeader("user-agen"));
        System.out.println(agent);
//        return agent.getValue(UserAgent.OPERATING_SYSTEM_NAME) + " - " + agent.get(UserAgent.AGENT_NAME + " - " + agent.getValue(UserAgent.DEVICE_NAME));
        return agent.getValue(UserAgent.AGENT_NAME) + " - " + agent.getValue(UserAgent.DEVICE_BRAND);
    }
}
