package com.hyg.domain;

/**
 * @author hyg
 **/
public class UserCollections {
    private int id;
    private int accountId;
    private String fanhao;

    public UserCollections() {
    }

    public UserCollections(int id, int accountId, String fanhao) {
        this.id = id;
        this.accountId = accountId;
        this.fanhao = fanhao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getFanhao() {
        return fanhao;
    }

    public void setFanhao(String fanhao) {
        this.fanhao = fanhao;
    }

    @Override
    public String toString() {
        return "UserCollections{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", fanhao='" + fanhao + '\'' +
                '}';
    }
}
