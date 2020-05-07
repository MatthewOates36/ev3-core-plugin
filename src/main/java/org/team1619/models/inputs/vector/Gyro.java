package org.team1619.models.inputs.vector;

import org.uacr.models.inputs.vector.InputVector;
import org.uacr.utilities.Config;
import org.uacr.utilities.Maps;

import java.util.HashMap;
import java.util.Map;

public abstract class Gyro extends InputVector {

    protected Map<String, Boolean> mIsRaidans;
    protected Map<String, Boolean> mIsInverted;
    protected Map<String, Double> mGyroValues;

    public Gyro(Object name, Config config) {
        super(name, config);

        mGyroValues = new HashMap<>();

        //Is Inverted
        mIsInverted = new HashMap<>();
        mIsInverted.put("angle", config.getBoolean("angle_is_inverted", false));
        mIsInverted.put("rate", config.getBoolean("rate_is_inverted", false));

        // Is radians
        mIsRaidans = new HashMap<>();
        mIsRaidans.put("angle", config.getBoolean("angle_is_radians", false));
        mIsRaidans.put("rate", config.getBoolean("rate_is_radians", false));
    }

    @Override
    public void update() {
        mGyroValues = readHardware();
    }

    @Override
    public void initialize() {
        mGyroValues = Maps.of("angle", 0.0, "rate", 0.0);
    }

    @Override
    public Map<String, Double> get() {
        return mGyroValues;
    }

    public void processFlag(String flag) {
        if (flag.equals("zero")) {
            zeroAngle();
        }
        if (flag.equals("reset")) {
            resetSensor();
        }
    }

    protected abstract Map<String, Double> readHardware();

    protected abstract void zeroAngle();

    protected abstract void resetSensor();
}
