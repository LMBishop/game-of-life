package com.leonardobishop.gameoflife.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<String, List<Subscriber>> subscribers;

    public EventBus() {
        this.subscribers = new HashMap<>();
    }

    public void register(String event, Subscriber subscriber) {
        // NOT thread safe!!!!!!
        this.subscribers.compute(event, (key, list) -> {
            if (list == null) {
                List<Subscriber> subscribers = new ArrayList<>();
                subscribers.add(subscriber);
                return subscribers;
            }
            list.add(subscriber);
            return list;
        });
    }

    public void dispatch(Event event) {
        List<Subscriber> subscribers = this.subscribers.get(event.getName());
        if (subscribers == null) {
            return;
        }
        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.handle(event);
            } catch (Exception e) {
                System.err.println("Uncaught exception thrown when handling event " + event.getClass().getName());
                e.printStackTrace();
            }
        }
    }

}
