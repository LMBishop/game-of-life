package com.leonardobishop.gameoflife.game;

public class Grid {

    private final int GRID_WIDTH = 75;
    private final int GRID_HEIGHT = 50;

    private final boolean[][] grid;

    public Grid() {
        this.grid = new boolean[GRID_WIDTH][GRID_HEIGHT];
    }

    public void toggle(int x, int y) {
        if (x < 0 || y < 0 || x >= GRID_WIDTH || y >= GRID_HEIGHT) {
            return;
        }

        grid[x][y] = !grid[x][y];
    }

    public boolean get(int x, int y) {
        if (x < 0 || y < 0 || x >= GRID_WIDTH || y >= GRID_HEIGHT) {
            return false;
        }

        return grid[x][y];
    }

    public int getWidth() {
        return GRID_WIDTH;
    }

    public int getHeight() {
        return GRID_HEIGHT;
    }

}
