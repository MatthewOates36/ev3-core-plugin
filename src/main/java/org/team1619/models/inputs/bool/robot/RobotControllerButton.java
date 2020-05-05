package org.team1619.models.inputs.bool.robot;

import org.team1619.models.inputs.bool.Button;
import org.team1619.models.inputs.bool.sim.SimInputBooleanListener;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.Config;

public class RobotControllerButton extends Button {

    private final SimInputBooleanListener fListener;

    public RobotControllerButton(EventBus eventBus, Object name, Config config) {
        super(name, config);

        fListener = new SimInputBooleanListener(eventBus, name);
    }

    @Override
    public boolean isPressed() {
        return fListener.get();
    }
}
