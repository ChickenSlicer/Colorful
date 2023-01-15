package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class ChatInfo {
    private int position;
    private int size;

    public ChatInfo() {
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "position=" + position +
                ", size=" + size +
                '}';
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
