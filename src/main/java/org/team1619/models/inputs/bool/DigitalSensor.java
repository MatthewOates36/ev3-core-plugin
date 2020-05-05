package org.team1619.models.inputs.bool;

import org.uacr.models.inputs.bool.InputBoolean;
import org.uacr.utilities.Config;

public abstract class DigitalSensor extends InputBoolean {

    protected final int fId;
    private boolean mPreviousValue;
    private boolean mValue;

    public DigitalSensor(Object name, Config config) {
        super(name, config);

        fId = config.getInt("id");
    }

    public abstract boolean getDigitalInputValue();

    @Override
    public void initialize() {
        mValue = getDigitalInputValue();
    }

    @Override
    public void update() {
        mPreviousValue = mValue;
        mValue = getDigitalInputValue();
    }

    @Override
    public boolean get() {
        return fIsInverted != mValue;
    }

    @Override
    public DeltaType getDelta() {
        if (!mPreviousValue && mValue) {
            return DeltaType.RISING_EDGE;
        } else if (mPreviousValue && !mValue) {
            return DeltaType.FALLING_EDGE;
        } else {
            return DeltaType.NO_DELTA;
        }
    }

    @Override
    public void processFlag(String flag) {

    }
}
