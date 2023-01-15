package com.hyg.domain.transfer;

public class AddAllocateUnit {
    private int type;
    private String fanhao;

    public AddAllocateUnit() {
    }

    public AddAllocateUnit(int type, String fanhao) {
        this.type = type;
        this.fanhao = fanhao;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    @Override
    public String toString() {
        return "AddAllocateUnit{" +
                "type=" + type +
                ", fanhao='" + fanhao + '\'' +
                '}';
    }
}
