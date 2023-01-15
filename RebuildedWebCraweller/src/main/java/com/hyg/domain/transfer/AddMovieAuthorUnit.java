package com.hyg.domain.transfer;

public class AddMovieAuthorUnit {
    private String starName;
    private String fanhao;

    public AddMovieAuthorUnit(String starName, String fanhao) {
        this.starName = starName;
        this.fanhao = fanhao;
    }

    public AddMovieAuthorUnit() {
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    @Override
    public String toString() {
        return "AddMovieAuthorUnit{" +
                "starName='" + starName + '\'' +
                ", fanhao='" + fanhao + '\'' +
                '}';
    }
}
