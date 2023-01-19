package com.hyg.domain.transfer;

import java.util.List;

/**
 * @author hyg
 **/
public class CheckSubscribeInList {
    private String username;
    private List<String> stars;

    public CheckSubscribeInList(String username, List<String> stars) {
        this.username = username;
        this.stars = stars;
    }

    public CheckSubscribeInList() {
    }

    @Override
    public String toString() {
        return "CheckSubscribeInList{" +
                "username='" + username + '\'' +
                ", stars=" + stars +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getStars() {
        return stars;
    }

    public void setStars(List<String> stars) {
        this.stars = stars;
    }
}
