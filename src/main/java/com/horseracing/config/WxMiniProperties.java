package com.horseracing.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 *
 * @author rzf
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.mini")
public class WxMiniProperties {

    /**
     * 小程序AppID
     */
    private String appid;

    /**
     * 小程序Secret
     */
    private String secret;
}
