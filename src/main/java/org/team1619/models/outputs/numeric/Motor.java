package org.team1619.models.outputs.numeric;

import org.uacr.models.outputs.numeric.OutputNumeric;
import org.uacr.utilities.Config;

/**
 * CTREMotor is a class that stores data specific to CTRE motors, TalonSRX and VictorSPX
 */

public abstract class Motor extends OutputNumeric {

    protected final int fPort;
    protected final boolean fIsBrakeModeEnabled;

    public Motor(Object name, Config config) {
        super(name, config);

        fPort = config.getInt("port");
        fIsBrakeModeEnabled = config.getBoolean("brake_mode_enabled", true);
    }

    public int getDeviceNumber() {
        return fPort;
    }
}
