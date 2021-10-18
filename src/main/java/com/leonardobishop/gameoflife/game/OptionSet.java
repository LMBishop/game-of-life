package com.leonardobishop.gameoflife.game;

public class OptionSet {

    private final boolean toroidalGrid;

    // default options
    public OptionSet() {
        this(false);
    }

    public OptionSet(boolean toroidalGrid) {
        this.toroidalGrid = toroidalGrid;
    }

    public boolean isToroidalGrid() {
        return toroidalGrid;
    }
}
