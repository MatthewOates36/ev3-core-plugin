package org.team1619.models.inputs.numeric.sim;

import org.team1619.models.inputs.numeric.Axis;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.Config;

public class SimControllerAxis extends Axis {

    private final SimInputNumericListener fListener;

    public SimControllerAxis(EventBus eventBus, Object name, Config config) {
        super(name, config);

        fListener = new SimInputNumericListener(eventBus, name);
    }

    @Override
    public double getAxis() {
        return fListener.get();
    }
}
