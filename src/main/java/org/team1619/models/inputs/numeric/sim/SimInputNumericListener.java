package org.team1619.models.inputs.numeric.sim;

import org.uacr.events.sim.SimInputNumericSetEvent;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.eventbus.Subscribe;

public class SimInputNumericListener {

    private final Object fName;

    private double mValue = 0.0;

    public SimInputNumericListener(EventBus eventBus, Object name) {
        eventBus.register(this);
        fName = name;
    }

    public double get() {
        return mValue;
    }

    @Subscribe
    public void onInputNumericSet(SimInputNumericSetEvent event) {
        if (event.fName.equals(fName)) {
            mValue = event.fValue;
        }
    }
}
