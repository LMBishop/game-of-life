package com.leonardobishop.gameoflife.event.user;

import com.leonardobishop.gameoflife.event.Event;
import com.leonardobishop.gameoflife.game.OptionSet;

public class OptionSetUpdateEvent extends Event {

    private final OptionSet optionSet;

    public OptionSetUpdateEvent(OptionSet optionSet) {
        super("OptionSetUpdateEvent");
        this.optionSet = optionSet;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }
}
