package com.gdut.graduation.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/12 19:34
 * @Version 1.0
 **/
@Data
@ConfigurationProperties(prefix = "alipay")
@Component
public class AlipayProperties {
    private String url;
}
