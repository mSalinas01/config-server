package com.gaidar.config;

import com.netflix.appinfo.AmazonInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EurekaInstanceConfig {
    private final static Logger logger = LoggerFactory.getLogger(EurekaInstanceConfig.class);
    @Value("${server.port}")
    private int serverPort;

    /**
     * Eureka aws config.
     *
     * @return EurekaInstanceConfigBean.
     */
    @Bean
    @Profile("!default")
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            logger.debug("ip {}", ip);
        } catch (UnknownHostException exception) {
            logger.error("UnknownHostException: ",exception);
        }
        config.setDataCenterInfo(info);
        config.setNonSecurePort(serverPort);
        config.setPreferIpAddress(true);
        config.setIpAddress(ip);
        config.setInstanceId(config.getIpAddress() + ":" + serverPort);
        logger.info("aws info: {}",info.toString());
        return config;
    }
}
