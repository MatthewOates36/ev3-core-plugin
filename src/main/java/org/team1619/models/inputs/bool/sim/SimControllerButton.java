package org.team1619.models.inputs.bool.sim;

import org.team1619.models.inputs.bool.Button;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.Config;

public class SimControllerButton extends Button {

    private final SimInputBooleanListener fListener;

    public SimControllerButton(EventBus eventBus, Object name, Config config) {
        super(name, config);

        fListener = new SimInputBooleanListener(eventBus, name);
    }

    @Override
    public boolean isPressed() {
        return fListener.get();
    }
}
