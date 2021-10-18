package com.leonardobishop.gameoflife.ui;

import com.leonardobishop.gameoflife.event.EventBus;
import com.leonardobishop.gameoflife.event.game.GameStateUpdateEvent;
import com.leonardobishop.gameoflife.event.user.*;
import com.leonardobishop.gameoflife.game.OptionSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameFrame extends JFrame {

    private final GamePanel gamePanel;
    private final JPanel controlPanel;
    private final JPanel statusPanel;

    private final JLabel mousePosition;
    private final JLabel gameStatus;

    private final JButton initialButton;
    private final JButton settingsButton;
    private final JButton slowerButton;
    private final JButton pauseButton;
    private final JButton stepButton;
    private final JButton fasterButton;

    private OptionSet optionSet;

    public GameFrame(EventBus eventBus) {
        optionSet = new OptionSet();
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

        initialButton = new JButton("Start Game");
        settingsButton = new JButton("\u2699 Settings...");
        slowerButton = new JButton("<< Slower");
        pauseButton = new JButton("Pause");
        stepButton = new JButton("Step >|");
        fasterButton = new JButton("Faster >>");
        initialButton.addActionListener(e -> {
            controlPanel.remove(initialButton);
            controlPanel.remove(settingsButton);

            controlPanel.add(slowerButton);
            controlPanel.add(pauseButton);
            controlPanel.add(stepButton);
            controlPanel.add(fasterButton);

            slowerButton.addActionListener(event -> eventBus.dispatch(new SpeedDecreaseCommandEvent()));
            fasterButton.addActionListener(event -> eventBus.dispatch(new SpeedIncreaseCommandEvent()));
            pauseButton.addActionListener(event -> {
                if (pauseButton.getText().equals("Pause")) {
                    eventBus.dispatch(new PauseCommandEvent());
                } else {
                    eventBus.dispatch(new StartCommandEvent());
                }
            });
            stepButton.addActionListener(event -> {
                eventBus.dispatch(new StepCommandEvent());
            });

            controlPanel.revalidate();

            eventBus.dispatch(new StartCommandEvent());
        });
        settingsButton.addActionListener(e -> {
            JDialog modal = new JDialog(this, "Options", true);
            modal.setLayout(new GridLayout(2, 1));

            JCheckBox toroidalGrid = new JCheckBox("Use toroidal grid instead of bounded grid");
            toroidalGrid.setToolTipText("A toroidal grid has no bounds on the edges, instead the edges are 'stitched' to the opposite ends creating one contiguous grid.");
            JButton confirmButton = new JButton("Confirm");

            confirmButton.addActionListener(e1 -> {
                optionSet = new OptionSet(toroidalGrid.isSelected());
                eventBus.dispatch(new OptionSetUpdateEvent(optionSet));
                modal.setVisible(false);
                modal.dispose();
            });

            toroidalGrid.setSelected(optionSet.isToroidalGrid());

            modal.add(toroidalGrid);
            modal.add(confirmButton);

            modal.setResizable(false);
            modal.pack();
            modal.setLocationRelativeTo(null);
            modal.setVisible(true);
        });
        controlPanel.add(initialButton);
        controlPanel.add(settingsButton);

        eventBus.register("GameStateUpdateEvent", event -> {
            SwingUtilities.invokeLater(() -> {
                GameStateUpdateEvent e = (GameStateUpdateEvent) event;
                gamePanel.refresh(e.getGridSnapshot());
                boolean running = e.isRunning();

                pauseButton.setText(running ? "Pause" : "Resume");
                stepButton.setEnabled(!running);
                slowerButton.setEnabled(running && e.getRate() != 1);
                fasterButton.setEnabled(running);

                gameStatus.setText("Game is " + (running ? "running" : "paused") + "." + (e.getFrame() > 0 ? " (Generation: " + e.getFrame() + "; speed: " + e.getRate() + " Hz)" : ""));
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
