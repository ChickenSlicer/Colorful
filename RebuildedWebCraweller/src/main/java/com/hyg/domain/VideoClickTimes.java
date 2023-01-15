package com.hyg.domain;

/**
 * @author hyg
 **/
public class VideoClickTimes {
    private String fanhao;
    private int times;
    private String username;
    private int id;

    public VideoClickTimes() {
    }

    public VideoClickTimes(String fanhao, int times, String username) {
        this.fanhao = fanhao;
        this.times = times;
        this.username = username;
    }

    @Override
    public String toString() {
        return "VideoClickTimes{" +
                "fanhao='" + fanhao + '\'' +
                ", times=" + times +
                ", username='" + username + '\'' +
                ", id=" + id +
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

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
