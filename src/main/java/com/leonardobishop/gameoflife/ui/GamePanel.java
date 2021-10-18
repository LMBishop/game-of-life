package com.leonardobishop.gameoflife.ui;

import com.leonardobishop.gameoflife.event.EventBus;
import com.leonardobishop.gameoflife.event.user.CellClickedEvent;
import com.leonardobishop.gameoflife.game.GameOfLife;
import com.leonardobishop.gameoflife.game.GridSnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel {

    private final int CELL_LENGTH = 12;
    private final int GRID_HEIGHT = GameOfLife.GRID_HEIGHT * CELL_LENGTH;
    private final int GRID_WIDTH = GameOfLife.GRID_WIDTH * CELL_LENGTH;

    private GridSnapshot recentSnapshot;

    public GamePanel(EventBus eventBus, JLabel mousePosition) {
        this.setPreferredSize(new Dimension(GRID_WIDTH, GRID_HEIGHT));
        this.addMouseMotionListener(new MouseMotionListener() {
            int currentCellX;
            int currentCellY;
            @Override
            public void mouseDragged(MouseEvent e) {
                int cellX = e.getX() / CELL_LENGTH;
                int cellY = e.getY() / CELL_LENGTH;

                if (cellX == currentCellX && cellY == currentCellY) {
                    return;
                }

                currentCellX = cellX;
                currentCellY = cellY;

                eventBus.dispatch(new CellClickedEvent(cellX, cellY));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition.setText("X: " + e.getX() / CELL_LENGTH + " Y: " + e.getY() / CELL_LENGTH);
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // no-op
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // no-op
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int cellX = e.getX() / CELL_LENGTH;
                int cellY = e.getY() / CELL_LENGTH;

                eventBus.dispatch(new CellClickedEvent(cellX, cellY));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // no-op
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point p = new Point(e.getLocationOnScreen());
                SwingUtilities.convertPointFromScreen(p, e.getComponent());
                if (e.getComponent().contains(p)) {
                    return;
                }
                mousePosition.setText("");
            }
        });
    }

    public void refresh(GridSnapshot gridSnapshot) {
        this.recentSnapshot = gridSnapshot;
        super.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setStroke(new BasicStroke(1));

        for (int x = 0; x < GRID_WIDTH; x = x + CELL_LENGTH) {
            for (int y = 0; y < GRID_HEIGHT; y = y + CELL_LENGTH) {
                int cellX = x / CELL_LENGTH;
                int cellY = y / CELL_LENGTH;

                if (recentSnapshot != null && recentSnapshot.get(cellX, cellY)) {
                    graphics.setPaint(Color.BLACK);
                    graphics.fillRect(x, y, CELL_LENGTH, CELL_LENGTH);
                    graphics.setPaint(Color.GRAY);
                } else {
                    graphics.setPaint(Color.LIGHT_GRAY);
                }
                graphics.drawRect(x, y, CELL_LENGTH, CELL_LENGTH);
            }
        }
    }

}
