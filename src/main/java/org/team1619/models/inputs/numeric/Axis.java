package org.team1619.models.inputs.numeric;

import org.uacr.models.inputs.numeric.InputNumeric;
import org.uacr.utilities.Config;

public abstract class Axis extends InputNumeric {

    protected final int fPort;
    protected final int fAxis;

    private double mAxisValue = 0.0;
    private double mDelta = 0.0;
    private double mDeadBand = 0.0;
    private double mScale = 0.0;

    public Axis(Object name, Config config) {
        super(name, config);

        fPort = config.getInt("port");
        fAxis = config.getInt("axis");
        mDeadBand = config.getDouble("deadband", 0.0);
        mScale = config.getDouble("scale", 1.0);
    }

    @Override
    public void update() {
        double nextAxis = fIsInverted ? -getAxis() : getAxis();
        if (Math.abs(nextAxis) < mDeadBand) {
            nextAxis = 0.0;
        }
        nextAxis = nextAxis * mScale;
        mDelta = nextAxis - mAxisValue;
        mAxisValue = nextAxis;
    }

    @Override
    public void initialize() {

    }

    @Override
    public double get() {
        return mAxisValue;
    }

    @Override
    public double getDelta() {
        return mDelta;
    }

    protected abstract double getAxis();

    @Override
    public void processFlag(String flag) {

    }
}
