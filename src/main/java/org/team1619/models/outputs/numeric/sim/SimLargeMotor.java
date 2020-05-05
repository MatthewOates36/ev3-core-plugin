package org.team1619.models.outputs.numeric.sim;

import org.team1619.models.inputs.numeric.sim.SimInputNumericListener;
import org.team1619.models.outputs.numeric.LargeMotor;
import org.uacr.events.sim.SimInputNumericSetEvent;
import org.uacr.shared.abstractions.EventBus;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.ObjectsDirectory;
import org.uacr.utilities.Config;

import javax.annotation.Nullable;

/**
 * SimLargeMotor extends LargeMotor, and acts like a large motor in sim mode
 */

public class SimLargeMotor extends LargeMotor {

    private final SimInputNumericListener fPositionListener;
    private final SimInputNumericListener fVelocityListener;
    private double fOutput = 0.0;
    @Nullable
    private Integer fMotor;

    public SimLargeMotor(Object name, Config config, EventBus eventBus, ObjectsDirectory objectsDirectory, InputValues inputValues) {
        super(name, config, inputValues);

        fPositionListener = new SimInputNumericListener(eventBus, fPositionInputName);
        fVelocityListener = new SimInputNumericListener(eventBus, fVelocityInputName);

        // Included to mimic RobotTalon for testing
        fMotor = (Integer) objectsDirectory.getHardwareObject(fPort);
        //noinspection ConstantConditions
        if (fMotor == null) {
            Integer port = fPort;
            fMotor = port;
            objectsDirectory.setHardwareObject(fPort, fMotor);
        }
    }

    @Override
    public void processFlag(String flag) {
        if (flag.equals("zero")) {
            zeroSensor();
        }
    }

    @Override
    public void setHardware(String outputType, double outputValue, String profile) {

        if (fReadPosition) {
            readEncoderPosition();
        }
        if (fReadVelocity) {
            readEncoderVelocity();
        }

        switch (outputType) {
            case "percent":
                fOutput = outputValue;
                break;
            case "velocity":
                fOutput = outputValue;
                break;
            case "position":
                fOutput = outputValue;
                break;
            default:
                throw new RuntimeException("No output type " + outputType + " for Large Motor");
        }
//		sLogger.trace("{}", outputValue);
    }

    @Override
    public double getSensorPosition() {
        return fPositionListener.get();
    }

    @Override
    public double getSensorVelocity() {
        return fVelocityListener.get();
    }

    @Override
    public void zeroSensor() {
        fPositionListener.onInputNumericSet(new SimInputNumericSetEvent(fPositionInputName, 0));
    }
}
