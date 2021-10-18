package com.leonardobishop.gameoflife.event.game;

import com.leonardobishop.gameoflife.event.Event;
import com.leonardobishop.gameoflife.game.GridSnapshot;

public class GameStateUpdateEvent extends Event {

    private final GridSnapshot gridSnapshot;
    private final boolean running;
    private final int frame;
    private final int rate;

    public GameStateUpdateEvent(GridSnapshot gridSnapshot, boolean running, int frame, int period) {
        super("GameStateUpdateEvent");
        this.gridSnapshot = gridSnapshot;
        this.running = running;
        this.frame = frame;
        this.rate = period;
    }

    public GridSnapshot getGridSnapshot() {
        return gridSnapshot;
    }

    public boolean isRunning() {
        return running;
    }

    public int getFrame() {
        return frame;
    }

    public int getRate() {
        return rate;
    }
}
