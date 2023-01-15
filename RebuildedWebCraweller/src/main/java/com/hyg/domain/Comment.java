package com.hyg.domain;

/**
 * @author hyg
 **/
public class Comment {
    private int id;
    private String fanhao;
    private String comment;
    private String time;
    private String username;

    public Comment() {
    }

    public Comment(int id, String fanhao, String comment, String time, String username) {
        this.id = id;
        this.fanhao = fanhao;
        this.comment = comment;
        this.time = time;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", fanhao='" + fanhao + '\'' +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
