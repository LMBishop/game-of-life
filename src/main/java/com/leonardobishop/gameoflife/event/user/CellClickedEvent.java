package com.leonardobishop.gameoflife.event.user;

import com.leonardobishop.gameoflife.event.Event;

public class CellClickedEvent extends Event {

    private final int x, y;

    public CellClickedEvent(int x, int y) {
        super("CellClickedEvent");
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
