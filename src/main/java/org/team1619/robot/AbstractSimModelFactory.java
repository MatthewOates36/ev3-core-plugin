package org.team1619.robot;

import org.team1619.models.inputs.vector.sim.SimGyro;
import org.team1619.models.outputs.numeric.MotorGroup;
import org.team1619.models.outputs.numeric.sim.SimLargeMotor;
import org.uacr.models.inputs.bool.InputBoolean;
import org.uacr.models.inputs.numeric.InputNumeric;
import org.uacr.models.inputs.vector.InputVector;
import org.uacr.models.outputs.bool.OutputBoolean;
import org.uacr.models.outputs.numeric.OutputNumeric;
import org.uacr.robot.AbstractModelFactory;
import org.uacr.shared.abstractions.*;
import org.uacr.utilities.Config;
import org.uacr.utilities.YamlConfigParser;
import org.uacr.utilities.injection.Inject;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

public class AbstractSimModelFactory extends AbstractModelFactory {

    private static final Logger sLogger = LogManager.getLogger(AbstractSimModelFactory.class);

    private final EventBus fEventBus;

    @Inject
    public AbstractSimModelFactory(EventBus eventBus, InputValues inputValues, OutputValues outputValues, RobotConfiguration robotConfiguration, ObjectsDirectory objectsDirectory) {
        super(inputValues, outputValues, robotConfiguration, objectsDirectory);
        fEventBus = eventBus;
    }

    @Override
    public OutputNumeric createOutputNumeric(Object name, Config config, YamlConfigParser parser) {
        sLogger.trace("Creating output numeric '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

        switch (config.getType()) {
            case "large_motor":
                return new SimLargeMotor(name, config, fEventBus, fSharedObjectDirectory, fSharedInputValues);
            case "motor_group":
                return new MotorGroup(name, config, parser, this);
            default:
                return super.createOutputNumeric(name, config, parser);
        }
    }

    @Override
    public OutputBoolean createOutputBoolean(Object name, Config config, YamlConfigParser parser) {
        sLogger.trace("Creating output boolean '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());
        switch (config.getType()) {
            default:
                return super.createOutputBoolean(name, config, parser);
        }
    }

    @Override
    public InputBoolean createInputBoolean(Object name, Config config) {
        sLogger.trace("Creating input boolean '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

        switch (config.getType()) {
            default:
                return super.createInputBoolean(name, config);
        }
    }

    @Override
    public InputNumeric createInputNumeric(Object name, Config config) {
        sLogger.trace("Creating input numeric '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

        switch (config.getType()) {
            default:
                return super.createInputNumeric(name, config);
        }
    }

    @Override
    public InputVector createInputVector(Object name, Config config) {
        sLogger.trace("Creating input vector '{}' of type '{}' with config '{}'", name, config.getType(), config.getData());

        switch (config.getType()) {
            case "navx":
                return new SimGyro(fEventBus, name, config);
            default:
                return super.createInputVector(name, config);
        }
    }
}