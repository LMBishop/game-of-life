package com.leonardobishop.gameoflife.game;

import com.leonardobishop.gameoflife.event.user.CellClickedEvent;
import com.leonardobishop.gameoflife.event.EventBus;
import com.leonardobishop.gameoflife.event.game.GameStateUpdateEvent;
import com.leonardobishop.gameoflife.event.user.OptionSetUpdateEvent;

import java.util.concurrent.*;

public class GameOfLife extends Thread {

    public static final int GRID_WIDTH = 75;
    public static final int GRID_HEIGHT = 50;

    private final Grid grid;
    private final EventBus eventBus;
    private final ScheduledExecutorService scheduler;
    private final BlockingQueue<Runnable> blockingQueue;
    private final Runnable gameStep;

    private boolean activated;
    private boolean running;
    private int frame;
    private int rate;
    private ScheduledFuture game;

    public GameOfLife(EventBus eventBus) {
        this.grid = new Grid();
        this.eventBus = eventBus;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.rate = 2;
        this.blockingQueue = new LinkedBlockingDeque<>();

        gameStep = () -> blockingQueue.add(() -> {
            GridSnapshot previous = new GridSnapshot(grid);
            for (int x = 0; x < grid.getWidth(); x++) {
                for (int y = 0; y < grid.getHeight(); y++) {
                    boolean alive = previous.get(x, y);

                    int neighbours = 0;
                    for (int offsetX = -1; offsetX <= 1; offsetX++) {
                        for (int offsetY = -1; offsetY <= 1; offsetY++) {
                            if (offsetX == 0 && offsetY == 0) {
                                continue;
                            }

                            if (previous.get(x + offsetX, y + offsetY)) {
                                neighbours++;
                            }
                        }
                    }

                    if (alive) {
                        if (neighbours < 2 || neighbours > 3) {
                            grid.toggle(x, y);
                        }
                    } else {
                        if (neighbours == 3) {
                            grid.toggle(x, y);
                        }
                    }
                }
            }

            frame++;
            eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
        });

        eventBus.register("CellClickedEvent", event -> blockingQueue.add(() -> {
            if (activated || running) {
                return;
            }

            CellClickedEvent e = (CellClickedEvent) event;
            grid.toggle(e.getX(), e.getY());
            eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
        }));
        eventBus.register("OptionSetUpdateEvent", event -> blockingQueue.add(() -> {
            if (activated) {
                return;
            }

            OptionSetUpdateEvent e = (OptionSetUpdateEvent) event;
            grid.setToroidal(e.getOptionSet().isToroidalGrid());
        }));
        eventBus.register("StartCommandEvent", event -> blockingQueue.add(() -> {
            if (running) {
                return;
            }

            this.activated = true;
            this.resumeAutomatedPlay();
            eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
        }));
        eventBus.register("PauseCommandEvent", event -> blockingQueue.add(() -> {
            if (!running) {
                return;
            }

            if (game != null) {
                game.cancel(false);
                this.running = false;
                eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
            }
        }));
        eventBus.register("StepCommandEvent", event -> blockingQueue.add(() -> {
            if (running) {
                return;
            }

            gameStep.run();
        }));
        eventBus.register("SpeedIncreaseCommandEvent", event -> blockingQueue.add(() -> {
            if (!running) {
                return;
            }
            rate++;
            eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
            this.resumeAutomatedPlay();
        }));
        eventBus.register("SpeedDecreaseCommandEvent", event -> blockingQueue.add(() -> {
            if (!running || rate == 1) {
                return;
            }
            rate--;
            eventBus.dispatch(new GameStateUpdateEvent(new GridSnapshot(grid), running, frame, rate));
            this.resumeAutomatedPlay();
        }));
    }

    @Override
    public void run() {
        while (true) {
            try {
                blockingQueue.take().run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void resumeAutomatedPlay() {
        if (game != null) {
            game.cancel(false);
        }
        running = true;
        game = scheduler.scheduleAtFixedRate(gameStep, 1000/rate, 1000/rate, TimeUnit.MILLISECONDS);
    }

}
