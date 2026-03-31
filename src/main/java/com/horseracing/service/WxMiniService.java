package com.horseracing.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.horseracing.config.WxMiniProperties;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 微信小程序服务
 *
 * @author rzf
 */
@Slf4j
@Service
public class WxMiniService {

    @Autowired
    private WxMiniProperties wxMiniProperties;

    /**
     * 通过code获取openid
     *
     * @param code 微信登录code
     * @return openid
     */
    public String getOpenidByCode(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", wxMiniProperties.getAppid());
        params.put("secret", wxMiniProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String result = HttpUtil.get(url, params);
        log.info("微信登录返回: {}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.containsKey("openid")) {
            return jsonObject.getString("openid");
        } else {
            log.error("获取openid失败: {}", result);
            return null;
        }
    }
}
