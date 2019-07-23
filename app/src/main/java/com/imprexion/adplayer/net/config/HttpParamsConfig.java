package com.imprexion.adplayer.net.config;

import com.imprexion.library.config.Config;

/**
 * @author : yan
 * @date : 2019/7/22 11:02
 * @desc : TODO
 */
public class HttpParamsConfig implements Config {
    private String token;
    private String baseUrl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
