package com.hyg.domain;

/**
 * @author hyg
 **/
public class UserHistory {
    private int id;
    private String username;
    private String fanhao;
    private String time;
    private int endTime;

    public UserHistory() {
    }

    public UserHistory(String username, String fanhao, String time, int endTime) {
        this.username = username;
        this.fanhao = fanhao;
        this.time = time;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "UserHistory{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fanhao='" + fanhao + '\'' +
                ", time='" + time + '\'' +
                ", endTime=" + endTime +
                '}';
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
