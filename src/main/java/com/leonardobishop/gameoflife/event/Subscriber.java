package com.leonardobishop.gameoflife.event;

public interface Subscriber {

    void handle(Event event);

}
