package com.leonardobishop.gameoflife.ui;

import com.leonardobishop.gameoflife.event.EventBus;
import com.leonardobishop.gameoflife.event.game.GameStateUpdateEvent;
import com.leonardobishop.gameoflife.event.user.SpeedDecreaseCommandEvent;
import com.leonardobishop.gameoflife.event.user.SpeedIncreaseCommandEvent;
import com.leonardobishop.gameoflife.event.user.StartCommandEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameFrame extends JFrame {

    private final GamePanel gamePanel;
    private final JPanel controlPanel;
    private final JPanel statusPanel;
    private final JLabel mousePosition;
    private final JLabel gameStatus;

    public GameFrame(EventBus eventBus) {
        controlPanel = new JPanel();
        statusPanel = new JPanel();
        mousePosition = new JLabel();
        gamePanel = new GamePanel(eventBus, mousePosition);
        gameStatus = new JLabel("Game is paused.");

        gameStatus.setHorizontalAlignment(JLabel.LEFT);
        gameStatus.setBorder(new EmptyBorder(0, 10, 0, 0));
        mousePosition.setHorizontalAlignment(JLabel.RIGHT);
        mousePosition.setBorder(new EmptyBorder(0, 0, 0, 10));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton("Start game");
        startButton.addActionListener(e -> {
            controlPanel.remove(0);

            JButton slowerButton = new JButton("<< Slower");
            JButton pauseButton = new JButton("Pause");
            JButton stepButton = new JButton("Step >|");
            JButton fasterButton = new JButton("Faster >>");

            controlPanel.add(slowerButton);
            controlPanel.add(pauseButton);
            controlPanel.add(stepButton);
            controlPanel.add(fasterButton);

            slowerButton.addActionListener(event -> eventBus.dispatch(new SpeedDecreaseCommandEvent()));
            fasterButton.addActionListener(event -> eventBus.dispatch(new SpeedIncreaseCommandEvent()));

            stepButton.setEnabled(false);

            controlPanel.revalidate();

            eventBus.dispatch(new StartCommandEvent());
        });
        controlPanel.add(startButton);

        eventBus.register("GameStateUpdateEvent", event -> {
            SwingUtilities.invokeLater(() -> {
                GameStateUpdateEvent e = (GameStateUpdateEvent) event;
                gamePanel.refresh(e.getGridSnapshot());
                if (e.isRunning()) {
                    gameStatus.setText("Game is running. (Frame: " + e.getFrame() + "; speed: " + e.getRate() + " Hz)");
                } else {
                    gameStatus.setText("Game is paused.");
                }
            });
        });

        statusPanel.setPreferredSize(new Dimension(750, 25));
        statusPanel.setLayout(new GridLayout(1, 2));
        statusPanel.add(gameStatus);
        statusPanel.add(mousePosition);

        this.add(gamePanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);
        this.add(statusPanel, BorderLayout.NORTH);

        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("Conway's Game of Life");
    }

}