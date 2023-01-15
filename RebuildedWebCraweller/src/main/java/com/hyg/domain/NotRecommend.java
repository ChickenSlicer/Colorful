package com.hyg.domain;

/**
 * @author hyg
 **/
public class NotRecommend {
    private int id;
    private String username;
    private String fanhao;
    //这个值为1表示弃用，为0表示正在使用
    private int deprecated;

    public NotRecommend() {
    }

    public NotRecommend(String username, String fanhao, int deprecated) {
        this.username = username;
        this.fanhao = fanhao;
        this.deprecated = deprecated;
    }

    @Override
    public String toString() {
        return "NotRecommend{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fanhao='" + fanhao + '\'' +
                ", deprecated=" + deprecated +
                '}';
    }

    public int getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(int deprecated) {
        this.deprecated = deprecated;
    }

    public int getId() {
        return id;
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
}
