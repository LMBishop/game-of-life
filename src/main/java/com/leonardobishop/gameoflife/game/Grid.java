package com.leonardobishop.gameoflife.game;

public class Grid {

    private final boolean[][] grid;

    public Grid() {
        this.grid = new boolean[GameOfLife.GRID_WIDTH][GameOfLife.GRID_HEIGHT];
    }

    public void toggle(int x, int y) {
        if (x < 0 || y < 0 || x >= GameOfLife.GRID_WIDTH || y >= GameOfLife.GRID_HEIGHT) {
            return;
        }

        grid[x][y] = !grid[x][y];
    }

    public boolean get(int x, int y) {
        if (x < 0 || y < 0 || x >= GameOfLife.GRID_WIDTH || y >= GameOfLife.GRID_HEIGHT) {
            return false;
        }

        return grid[x][y];
    }

    public int getWidth() {
        return GameOfLife.GRID_WIDTH;
    }

    public int getHeight() {
        return GameOfLife.GRID_HEIGHT;
    }

}
