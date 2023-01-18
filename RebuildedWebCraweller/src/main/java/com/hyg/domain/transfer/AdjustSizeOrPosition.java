package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class AdjustSizeOrPosition {
    private int position;
    private int size;

    public AdjustSizeOrPosition() {
    }

    public AdjustSizeOrPosition(int position, int size) {
        this.position = position;
        this.size = size;
    }

    @Override
    public String toString() {
        return "AdjustSizeOrPosition{" +
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
