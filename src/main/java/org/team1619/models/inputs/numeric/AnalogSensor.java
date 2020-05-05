package org.team1619.models.inputs.numeric;

import org.uacr.models.inputs.numeric.InputNumeric;
import org.uacr.utilities.Config;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

public abstract class AnalogSensor extends InputNumeric {

    private static final Logger sLogger = LogManager.getLogger(AnalogSensor.class);
    protected final int fPort;
    private double mPreviousVoltage;
    private double mDelta;

    public AnalogSensor(Object name, Config config) {
        super(name, config);
        fPort = config.getInt("port");
        mPreviousVoltage = 0.0;
        mDelta = 0.0;
    }

    @Override
    public void update() {
        double voltage = getVoltage();
        mDelta = mPreviousVoltage + voltage;
    }

    @Override
    public void initialize() {

    }

    @Override
    public double get() {
        return getVoltage();
    }

    @Override
    public double getDelta() {
        return mDelta;
    }

    protected abstract double getVoltage();

    protected abstract double getAccumulatorCount();

    protected abstract double getAccumulatorValue();

    protected abstract double getValue();

    @Override
    public void processFlag(String flag) {

    }
}
