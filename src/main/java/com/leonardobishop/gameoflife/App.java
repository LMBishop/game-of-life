package com.leonardobishop.gameoflife;

import com.leonardobishop.gameoflife.event.EventBus;
import com.leonardobishop.gameoflife.game.GameOfLife;
import com.leonardobishop.gameoflife.ui.GameFrame;

public class App {

    private final GameOfLife game;
    private final EventBus eventBus;
    private final GameFrame gameFrame;

    public App() {
        eventBus = new EventBus();
        game = new GameOfLife(eventBus);
        gameFrame = new GameFrame(eventBus);

        game.start();
    }

    public static void main(String[] args) {
        new App();
    }

}
