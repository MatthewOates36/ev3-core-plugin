package org.team1619.models.outputs.numeric.robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
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
    private ObjectsDirectory fSharedObjectsDirectory;

    private String mCurrentOutputType;
    private double mCurrentOutputValue;

    public RobotLargeMotor(Object name, Config config, ObjectsDirectory objectsDirectory, InputValues inputValues) {
        super(name, config, inputValues);

        fSharedObjectsDirectory = objectsDirectory;

        mCurrentOutputType = "";
        mCurrentOutputValue = 0.0;

        @Nullable
        Port port;

        switch (fPort) {
            case "A":
                port = MotorPort.A;
                break;
            case "B":
                port = MotorPort.B;
                break;
            case "C":
                port = MotorPort.C;
                break;
            case "D":
                port = MotorPort.D;
                break;
            default:
                throw new RuntimeException("Motor Port " + fPort + " doesn't exist");
        }

        @Nullable
        Object regulatedMotorObject = fSharedObjectsDirectory.getHardwareObject(fPort + "regulated");
        if (regulatedMotorObject == null) {
            fRegulatedMotor = new EV3LargeRegulatedMotor(port);
            fSharedObjectsDirectory.setHardwareObject(fPort + "regulated", fRegulatedMotor);
        } else {
            fRegulatedMotor = (EV3LargeRegulatedMotor) regulatedMotorObject;
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

        if(outputType.equals(mCurrentOutputType) && outputValue == mCurrentOutputValue) {
            return;
        }

        mCurrentOutputType = outputType;
        mCurrentOutputValue = outputValue;

        switch (outputType) {
            case "percent":
            case "velocity":
                outputValue = outputValue * 360;
                if(outputValue < 0.0) {
                    fRegulatedMotor.setSpeed((int) outputValue);
                    fRegulatedMotor.backward();
                } else if(outputValue > 0.0) {
                    fRegulatedMotor.setSpeed((int) outputValue);
                    fRegulatedMotor.forward();
                } else {
                    if(fIsBrakeModeEnabled) {
                        fRegulatedMotor.stop();
                    } else {
                        fRegulatedMotor.flt();
                    }
                }
                break;
            case "position":
                fRegulatedMotor.setSpeed(fRegulatedMotor.getMaxSpeed());
                fRegulatedMotor.setAcceleration((int) fRegulatedMotor.getMaxSpeed());
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
