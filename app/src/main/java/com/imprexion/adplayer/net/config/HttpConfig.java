package com.imprexion.adplayer.net.config;

import com.imprexion.library.config.PublicConfig;

/**
 * @author : yan
 * @date : 2019/7/22 11:02
 * @desc : TODO
 */
public class HttpConfig implements PublicConfig {
    private String env;
    private String url;
    private String token;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "HttpConfig{" +
                "env='" + env + '\'' +
                ", url='" + url + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
