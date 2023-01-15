package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class UserHistoryLimit {
    private String username;
    private int position;
    private int size;

    public UserHistoryLimit(String username, int position, int size) {
        this.username = username;
        this.position = position;
        this.size = size;
    }

    public UserHistoryLimit() {
    }

    @Override
    public String toString() {
        return "UserHistoryLimit{" +
                "username='" + username + '\'' +
                ", position=" + position +
                ", size=" + size +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
