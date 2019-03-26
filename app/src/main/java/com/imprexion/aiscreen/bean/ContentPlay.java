package com.imprexion.aiscreen.bean;

public class ContentPlay {
    private ContentInfo contentInfo;
    private long start_time;

    public ContentInfo getContentInfo() {
        return contentInfo;
    }

    public void setContentInfo(ContentInfo contentInfo) {
        this.contentInfo = contentInfo;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public static class ContentInfo {
        private String contentName;
        private int content_len;
        private int content_type;
        private String[] contentUrl;
        private int content_priority;
        private int content_change_type;
        private String text;

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public int getContent_len() {
            return content_len;
        }

        public void setContent_len(int content_len) {
            this.content_len = content_len;
        }

        public int getContent_type() {
            return content_type;
        }

        public void setContent_type(int content_type) {
            this.content_type = content_type;
        }

        public String[] getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String[] contentUrl) {
            this.contentUrl = contentUrl;
        }

        public int getContent_priority() {
            return content_priority;
        }

        public void setContent_priority(int content_priority) {
            this.content_priority = content_priority;
        }

        public int getContent_change_type() {
            return content_change_type;
        }

        public void setContent_change_type(int content_change_type) {
            this.content_change_type = content_change_type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        //content_type;
        public final static int PIC = 0;      // 图片
        public final static int MP4 = 1;      // 视频
        public final static int TEXT = 2;     // 文字
        public final static int APP_T = 3;    // 应用

//        private enum ContentType {
//            PIC,      // 图片
//            MP4,     // 视频
//            TEXT,     // 文字
//            APP_T,    // 应用
//        }
    }
}
