package com.hyg.domain;

public class Movie {
    private Integer id;
    private Integer starId;
    private String fanHao;
    private String magnet;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", starId=" + starId +
                ", fanHao='" + fanHao + '\'' +
                ", magnet='" + magnet + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    private String introduction;

    public Movie() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStarId() {
        return starId;
    }

    public void setStarId(Integer starId) {
        this.starId = starId;
    }

    public String getFanHao() {
        return fanHao;
    }

    public void setFanHao(String fanHao) {
        this.fanHao = fanHao;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
