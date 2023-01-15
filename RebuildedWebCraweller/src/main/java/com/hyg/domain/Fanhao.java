package com.hyg.domain;

import java.io.Serializable;

public class Fanhao implements Serializable {
    private String fanhao;
    private String magnet_length;
    private String magnet_heat;

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    public String getMagnet_length() {
        return magnet_length;
    }

    public void setMagnet_length(String magnet_length) {
        this.magnet_length = magnet_length;
    }

    public String getMagnet_heat() {
        return magnet_heat;
    }

    public void setMagnet_heat(String magnet_heat) {
        this.magnet_heat = magnet_heat;
    }

    public Fanhao(String fanhao, String magnet_length, String magnet_heat) {
        this.fanhao = fanhao;
        this.magnet_length = magnet_length;
        this.magnet_heat = magnet_heat;
    }

    public Fanhao() {
    }

    public Fanhao(String fanhao) {
        this.fanhao = fanhao;
        this.magnet_heat = "";
        this.magnet_length = "";
    }

    @Override
    public String toString() {
        return "Fanhao{" +
                "fanhao='" + fanhao + '\'' +
                ", magnet_length='" + magnet_length + '\'' +
                ", magnet_heat='" + magnet_heat + '\'' +
                '}';
    }
}
