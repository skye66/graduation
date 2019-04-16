package com.gdut.graduation.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/13 20:03
 * @Version 1.0
 **/
@Component
@ConfigurationProperties("project")
@Data
public class ProjectConfig {
    /**
     * 项目配置的路径
     */
    private String path;
    /**
     * 图片地址主机host
     */
    private String imageHost;
}
