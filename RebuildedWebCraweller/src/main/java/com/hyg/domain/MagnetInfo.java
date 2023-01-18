package com.hyg.domain;

/**
 * @author hyg
 **/
public class MagnetInfo {
    private String fanhao;
    private String magnet;
    private float size;
    private String units;

    public MagnetInfo() {
    }

    public MagnetInfo(String fanhao, String magnet) {
        this.fanhao = fanhao;
        this.magnet = magnet;
    }

    @Override
    public String toString() {
        return "MagnetInfo{" +
                "fanhao='" + fanhao + '\'' +
                ", magnet='" + magnet + '\'' +
                ", size=" + size +
                ", units='" + units + '\'' +
                '}';
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }
}
