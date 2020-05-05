package org.team1619.models.inputs.vector.sim;

import org.team1619.models.inputs.vector.Gyro;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.utilities.Config;
import org.uacr.utilities.Maps;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class SimGyro extends Gyro {

    private static final Logger sLogger = LogManager.getLogger(SimGyro.class);
    private final SimInputVectorListener fListener;
    private double mGyroAngle;
    private double mGyroRate;

    public SimGyro(EventBus eventBus, Object name, Config config) {
        super(name, config);

        mGyroAngle = 0.0;
        mGyroRate = 0.0;

        fListener = new SimInputVectorListener(eventBus, name, Maps.of( "angle", mGyroAngle, "rate", mGyroRate));
    }

    protected Map<String, Double> readHardware() {
        //Inverted
        mGyroAngle = getValue("angle");
        mGyroAngle = getValue("rate");

        return Maps.of("angle", mGyroAngle, "rate", mGyroRate);
    }

    private double getValue(String name) {
        double value = mIsInverted.get(name) ? fListener.get().get(name) * -1 : fListener.get().get(name);
        return (mIsRaidans.containsKey(name) && mIsRaidans.get(name)) ? value * Math.PI / 180 : value;
    }

    protected void zeroAngle() {
        sLogger.debug("SimGyroInput -> Zeroing angle");

        Map<String, Double> lastGyroValues = mGyroValues;
        mGyroValues = new HashMap<>();
        mGyroValues.putAll(lastGyroValues);

        mGyroValues.put("angle", 0.0);
    }

    @Override
    protected void resetSensor() {
        sLogger.debug("SimGyroInput -> Resetting Sensor");

        Map<String, Double> lastNavxValues = mGyroValues;
        mGyroValues = new HashMap<>();
        mGyroValues.putAll(lastNavxValues);

        mGyroValues.put("angle", 0.0);
        mGyroValues.put("rate", 0.0);
    }
}
