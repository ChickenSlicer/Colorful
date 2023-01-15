package com.hyg.domain;

/**
 * @author hyg
 **/
public class ChatRecord {
    private long id;
    private String username;
    private String message;
    private String time;
    private int type;//1表示正常文字，2表示图片，3表示视频，4表示撤回消息
    private int replyTo;//表示回复id为该值的聊天记录，若为0则不是回复信息
    private String speakTo;//表示@用户名为该值的人，若为""则不是@信息

    public ChatRecord() {
    }

    public ChatRecord(String username, String message, String time, int type, int replyTo, String speakTo) {
        this.username = username;
        this.message = message;
        this.time = time;
        this.type = type;
        this.replyTo = replyTo;
        this.speakTo = speakTo;
    }

    @Override
    public String toString() {
        return "CharRecord{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", type=" + type +
                ", replyTo=" + replyTo +
                ", speakTo='" + speakTo + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }

    public String getSpeakTo() {
        return speakTo;
    }

    public void setSpeakTo(String speakTo) {
        this.speakTo = speakTo;
    }
}
