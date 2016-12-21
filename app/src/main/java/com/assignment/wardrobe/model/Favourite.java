package com.assignment.wardrobe.model;

/**
 * Created by Rashmi on 20/12/16.
 */

public class Favourite {

    private long id;
    private int topId;
    private int bottomId;

    public Favourite() {
    }

    public Favourite(int topId, int bottomId) {
        this.topId = topId;
        this.bottomId = bottomId;
    }

    public Favourite(long id, int topId, int bottomId) {
        this.id = id;
        this.topId = topId;
        this.bottomId = bottomId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }
}
