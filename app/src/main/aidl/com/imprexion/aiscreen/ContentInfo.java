package com.imprexion.aiscreen;

import android.os.Parcel;
import android.os.Parcelable;

public class ContentInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contentName);
        dest.writeInt(this.content_len);
        dest.writeInt(this.content_type);
        dest.writeStringArray(this.contentUrl);
        dest.writeInt(this.content_priority);
        dest.writeInt(this.content_change_type);
        dest.writeString(this.text);
    }

    public ContentInfo() {
    }

    protected ContentInfo(Parcel in) {
        this.contentName = in.readString();
        this.content_len = in.readInt();
        this.content_type = in.readInt();
        this.contentUrl = in.createStringArray();
        this.content_priority = in.readInt();
        this.content_change_type = in.readInt();
        this.text = in.readString();
    }

    public static final Parcelable.Creator<ContentInfo> CREATOR = new Parcelable.Creator<ContentInfo>() {
        @Override
        public ContentInfo createFromParcel(Parcel source) {
            return new ContentInfo(source);
        }

        @Override
        public ContentInfo[] newArray(int size) {
            return new ContentInfo[size];
        }
    };
}
