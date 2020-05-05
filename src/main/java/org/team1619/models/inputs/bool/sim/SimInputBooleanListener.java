package org.team1619.models.inputs.bool.sim;

import org.uacr.events.sim.SimInputBooleanSetEvent;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.eventbus.Subscribe;

public class SimInputBooleanListener {

    private final Object fName;

    private boolean mValue = false;

    public SimInputBooleanListener(EventBus eventBus, Object name) {
        fName = name;
        eventBus.register(this);
    }

    public boolean get() {
        return mValue;
    }

    @Subscribe
    public void onInputBooleanSet(SimInputBooleanSetEvent event) {
        if (event.fName.equals(fName)) {
            mValue = event.fValue;
        }
    }
}
