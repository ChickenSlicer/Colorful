package com.hyg.domain;

/**
 * @author hyg
 **/
public class StarSubscribe {
    private int id;
    private String username;
    private String starName;
    private int starId;
    private int updated;//为0未更新，为1表示已更新

    public StarSubscribe() {
    }

    public StarSubscribe(String username, String starName, int starId) {
        this.username = username;
        this.starName = starName;
        this.starId = starId;
        this.updated = 0;
    }

    @Override
    public String toString() {
        return "StarSubscribe{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", starName='" + starName + '\'' +
                ", starId=" + starId +
                ", updated=" + updated +
                '}';
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }
}
