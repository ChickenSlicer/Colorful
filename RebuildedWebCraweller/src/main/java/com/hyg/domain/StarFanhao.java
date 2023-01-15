package com.hyg.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 **/
public class StarFanhao {
    private String name;
    private List<Fanhao> fanhaos;

    public StarFanhao() {
        this.fanhaos = new ArrayList<>();
    }

    public StarFanhao(String name) {
        this.name = name;
        this.fanhaos = new ArrayList<>();
    }

    public void addFanhao(Fanhao fanhao){
        this.fanhaos.add(fanhao);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Fanhao> getFanhaos() {
        return fanhaos;
    }

    public void setFanhaos(List<Fanhao> fanhaos) {
        this.fanhaos = fanhaos;
    }

    @Override
    public String toString() {
        return "StarFanhao{" +
                "name='" + name + '\'' +
                ", fanhaos=" + fanhaos +
                '}';
    }
}
