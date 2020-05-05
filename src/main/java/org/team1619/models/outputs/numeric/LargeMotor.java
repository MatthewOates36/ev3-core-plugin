package org.team1619.models.outputs.numeric;

import org.uacr.shared.abstractions.InputValues;
import org.uacr.utilities.Config;

/**
 * Talon is a motor object, which is extended to control talons
 */

public abstract class LargeMotor extends Motor {

    protected final InputValues fSharedInputValues;

    protected final String fPositionInputName;
    protected final String fVelocityInputName;

    protected final boolean fSensorInverted;
    protected final boolean fReadPosition;
    protected final boolean fReadVelocity;
    protected final double fPercentScalar;
    protected final double fPositionScalar;
    protected final double fVelocityScalar;

    public LargeMotor(Object name, Config config, InputValues inputValues) {
        super(name, config);

        fSharedInputValues = inputValues;

        fPositionInputName = config.getString("position_input_name", name.toString().replaceFirst("opn_", "ipn_") + "_position");
        fVelocityInputName = config.getString("velocity_input_name", name.toString().replaceFirst("opn_", "ipn_") + "_velocity");

        fSensorInverted = config.getBoolean("sensor_inverted", false);
        fReadPosition = config.getBoolean("read_position", false);
        fReadVelocity = config.getBoolean("read_velocity", false);

        fPercentScalar = config.getDouble("percent_scalar", 1);
        fPositionScalar = config.getDouble("position_scalar", 1);
        fVelocityScalar = config.getDouble("velocity_scalar", 1);
    }

    // Read the encoder's position
    protected void readEncoderPosition() {
        fSharedInputValues.setNumeric(fPositionInputName, getSensorPosition());
    }

    // Read the encoder's velocity
    protected void readEncoderVelocity() {
        fSharedInputValues.setNumeric(fVelocityInputName, getSensorVelocity());
    }

    public abstract double getSensorPosition();

    public abstract double getSensorVelocity();

    public abstract void zeroSensor();
}
