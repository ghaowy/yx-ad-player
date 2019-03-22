package com.imprexion.aiscreen.advertising;

public class AdContentInfo {
    private String contentName;
    private int content_len;
    private int content_type;
    private String[] contentUrl;
    private int content_priority;
    private int content_change_type;
    private String text;

    public int getContent_len() {
        return content_len;
    }

    public int getContent_type() {
        return content_type;
    }

    public String[] getContentUrl() {
        return contentUrl;
    }
}
