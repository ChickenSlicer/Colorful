package com.hyg.domain;

public class Allocates {
    private int id;
    private String fanhao;

    public Allocates() {
    }

    public Allocates(int id, String fanhao) {
        this.id = id;
        this.fanhao = fanhao;
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
}
