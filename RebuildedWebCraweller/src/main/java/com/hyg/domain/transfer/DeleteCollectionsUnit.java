package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class DeleteCollectionsUnit {
    private String username;
    private String fanhao;

    public DeleteCollectionsUnit() {
    }

    public DeleteCollectionsUnit(String username, String fanhao) {
        this.username = username;
        this.fanhao = fanhao;
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
