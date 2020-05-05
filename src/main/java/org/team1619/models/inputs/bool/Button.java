package org.team1619.models.inputs.bool;

import org.uacr.models.inputs.bool.InputBoolean;
import org.uacr.utilities.Config;

public abstract class Button extends InputBoolean {

    protected final int fPort;
    protected final String fButton;

    private boolean mIsPressed = false;
    private DeltaType mDelta = DeltaType.NO_DELTA;

    public Button(Object name, Config config) {
        super(name, config);

        fPort = config.getInt("port");
        fButton = config.getString("button");
    }

    @Override
    public void update() {
        boolean nextIsPressed = fIsInverted != isPressed();

        if (nextIsPressed && !mIsPressed) {
            mDelta = DeltaType.RISING_EDGE;
        } else if (!nextIsPressed && mIsPressed) {
            mDelta = DeltaType.FALLING_EDGE;
        } else {
            mDelta = DeltaType.NO_DELTA;
        }

        mIsPressed = nextIsPressed;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean get() {
        return mIsPressed;
    }

    @Override
    public DeltaType getDelta() {
        return mDelta;
    }

    public abstract boolean isPressed();

    @Override
    public void processFlag(String flag) {

    }
}
