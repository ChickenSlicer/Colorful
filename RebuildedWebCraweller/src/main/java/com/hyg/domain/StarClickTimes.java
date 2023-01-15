package com.hyg.domain;

/**
 * @author hyg
 **/
public class StarClickTimes {
    private String starName;
    private int times;
    private int id;
    private String username;

    public StarClickTimes() {
    }

    public StarClickTimes(String starName, int times, String username) {
        this.starName = starName;
        this.times = times;
        this.username = username;
    }

    @Override
    public String toString() {
        return "StarClickTimes{" +
                "starName='" + starName + '\'' +
                ", times=" + times +
                ", id=" + id +
                ", username='" + username + '\'' +
                '}';
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

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
