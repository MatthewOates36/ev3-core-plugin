package org.team1619.models.outputs.numeric.robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import org.team1619.models.outputs.numeric.LargeMotor;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.ObjectsDirectory;
import org.uacr.utilities.Config;

import javax.annotation.Nullable;

/**
 * RobotLargeMotor extends LargeMotor, and controls large motors on the robot
 */

public class RobotLargeMotor extends LargeMotor {

    private final EV3LargeRegulatedMotor fRegulatedMotor;
    private final UnregulatedMotor fUnregulatedMotor;
    private ObjectsDirectory fSharedObjectsDirectory;
    private String fCurrentProfileName = "none";

    public RobotLargeMotor(Object name, Config config, ObjectsDirectory objectsDirectory, InputValues inputValues) {
        super(name, config, inputValues);

        fSharedObjectsDirectory = objectsDirectory;

        @Nullable
        Object regulatedMotorObject = fSharedObjectsDirectory.getHardwareObject(fPort + "regulated");
        if (regulatedMotorObject == null) {
            fRegulatedMotor = new EV3LargeRegulatedMotor(MotorPort.A);
            fSharedObjectsDirectory.setHardwareObject(fPort + "regulated", fRegulatedMotor);
        } else {
            fRegulatedMotor = (EV3LargeRegulatedMotor) regulatedMotorObject;
        }

        @Nullable
        Object unregulatedMotorObject = fSharedObjectsDirectory.getHardwareObject(fPort + "unregulated");
        if (unregulatedMotorObject == null) {
            fUnregulatedMotor = new UnregulatedMotor(MotorPort.A);
            fSharedObjectsDirectory.setHardwareObject(fPort + "unregulated", fUnregulatedMotor);
        } else {
            fUnregulatedMotor = (UnregulatedMotor) unregulatedMotorObject;
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

        if(fIsInverted) {
            outputValue = -outputValue;
        }
        if (fReadPosition) {
            readEncoderPosition();
        }
        if (fReadVelocity) {
            readEncoderVelocity();
        }

        switch (outputType) {
            case "percent":
                outputValue = outputValue * 100;
                fRegulatedMotor.suspendRegulation();
                if(outputValue < 0.0) {
                    fUnregulatedMotor.setPower((int) outputValue);
                    fUnregulatedMotor.backward();
                } else if(outputValue > 0.0) {
                    fUnregulatedMotor.setPower((int) outputValue);
                    fUnregulatedMotor.forward();
                } else {
                    if(fIsBrakeModeEnabled) {
                        fUnregulatedMotor.stop();
                    } else {
                        fUnregulatedMotor.flt();
                    }
                }
                break;
            case "follower":
                break;
            case "position":
                fRegulatedMotor.rotateTo((int) outputValue, true);
                break;
            default:
                throw new RuntimeException("No output type " + outputType + " for Large Motor");
        }
    }

    @Override
    public double getSensorPosition() {
        return fRegulatedMotor.getTachoCount() * fPositionScalar * (fIsInverted ? -1 : 1) * (fSensorInverted ? -1 : 1);
    }

    @Override
    public double getSensorVelocity() {
        return fRegulatedMotor.getRotationSpeed() * fVelocityScalar * (fIsInverted ? -1 : 1) * (fSensorInverted ? -1 : 1);
    }

    @Override
    public void zeroSensor() {
        fRegulatedMotor.resetTachoCount();
    }
}
