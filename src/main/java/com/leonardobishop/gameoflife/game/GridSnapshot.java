package com.leonardobishop.gameoflife.game;

public class GridSnapshot {

    private final boolean[][] grid;
    private final int width;
    private final int height;

    public GridSnapshot(Grid grid) {
        this.width = grid.getWidth();
        this.height = grid.getHeight();

        this.grid = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.grid[x][y] = grid.get(x, y);
            }
        }
    }

    public boolean get(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return false;
        }

        return grid[x][y];
    }

}
