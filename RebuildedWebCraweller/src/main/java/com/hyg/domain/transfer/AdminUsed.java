package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class AdminUsed {
    private String username;
    private String todo;
    private String fanhaoToBeRemoved;

    public AdminUsed() {
    }

    public AdminUsed(String username, String todo, String fanhaoToBeRemoved) {
        this.username = username;
        this.todo = todo;
        this.fanhaoToBeRemoved = fanhaoToBeRemoved;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getFanhaoToBeRemoved() {
        return fanhaoToBeRemoved;
    }

    public void setFanhaoToBeRemoved(String fanhaoToBeRemoved) {
        this.fanhaoToBeRemoved = fanhaoToBeRemoved;
    }
}
