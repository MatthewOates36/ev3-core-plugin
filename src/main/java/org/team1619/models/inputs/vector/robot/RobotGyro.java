package org.team1619.models.inputs.vector.robot;

import javax.annotation.Nullable;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import org.team1619.models.inputs.vector.Gyro;
import org.uacr.utilities.Config;
import org.uacr.utilities.Maps;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Map;

public class RobotGyro extends Gyro {

    private static final Logger sLogger = LogManager.getLogger(RobotGyro.class);

    private final EV3GyroSensor fGyro;
    private final SampleProvider fSampleProvider;

    private float [] mSample;
    private double mGyroAngle;
    private double mAngleOffset;
    private double mGyroRate;

    public RobotGyro(Object name, Config config) {
        super(name, config);

        @Nullable
        Port port;

        int portNum = config.getInt("port");

        switch (portNum) {
            case 1:
                port = SensorPort.S1;
                break;
            case 2:
                port = SensorPort.S2;
                break;
            case 3:
                port = SensorPort.S3;
                break;
            case 4:
                port = SensorPort.S4;
                break;
            default:
                throw new RuntimeException("Sensor Port " + portNum + " doesn't exist");
        }

        fGyro = new EV3GyroSensor(port);
        fGyro.reset();

        fSampleProvider = fGyro.getAngleAndRateMode();
        mSample = new float[fSampleProvider.sampleSize()];

        mGyroAngle = 0.0;
        mAngleOffset = 0.0;
    }

    @Override
    protected void zeroAngle() {
        sLogger.debug("RobotGyroInput -> Zeroing angle");

        fSampleProvider.fetchSample(mSample, 0);

        mAngleOffset = mSample[0];
    }

    @Override
    protected void resetSensor() {
        sLogger.debug("RobotGyroInput -> Resetting sensor");
        fGyro.reset();
    }

    @Override
    protected Map<String, Double> readHardware() {

        fSampleProvider.fetchSample(mSample, 0);
        mGyroAngle = mSample[0] - mAngleOffset;
        mGyroRate = mSample[1];

        // Inverted
        mGyroAngle = mIsInverted.get("angle") ? mGyroAngle * -1 : mGyroAngle;
        mGyroRate = mIsInverted.get("rate") ? mGyroRate * -1 : mSample[1];

        //Radians
        mGyroAngle = mIsRaidans.get("angle") ? mGyroAngle * Math.PI / 180 : mGyroAngle;
        mGyroRate = mIsRaidans.get("rate") ? mGyroRate * Math.PI / 180 : mGyroRate;

        return Maps.of("angle", mGyroAngle, "rate", mGyroRate);
    }
}
