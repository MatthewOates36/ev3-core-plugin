package org.team1619.models.inputs.vector;

import org.uacr.models.inputs.vector.InputVector;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.utilities.Config;
import org.uacr.utilities.Maps;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;
import org.uacr.utilities.purepursuit.Pose2d;
import org.uacr.utilities.purepursuit.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * OdometryInput is a Inputvector which uses the navx and drive encoders,
 * to track the robots current position relative to its starting point
 *
 * @author Matthew Oates
 */

public class Odometry extends InputVector {

    private static final Logger sLogger = LogManager.getLogger(Odometry.class);

    protected final Config fConfig;
    protected final InputValues fSharedInputValues;

    private final String fHeadingSensor;
    private final String fHeadingKey;
    private final String fLeftEncoder;
    private final String fRightEncoder;
    private Map<String, Double> mNavxValues;
    private double mLeftPosition = 0;
    private double mRightPosition = 0;
    private double mHeading = 0;

    private Pose2d mCurrentPosition;

    public Odometry(Object name, Config config, InputValues inputValues) {
        super(name, config);

        fConfig = config;
        fSharedInputValues = inputValues;
        mNavxValues = new HashMap<>();

        fHeadingSensor = config.getString("heading_sensor");
        fHeadingKey = config.getString("heading_key");

        fLeftEncoder = config.getString("left_encoder");
        fRightEncoder = config.getString("right_encoder");

        mCurrentPosition = new Pose2d();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update() {
        if (!fSharedInputValues.getBoolean("ipb_odometry_has_been_zeroed")) {
            zeroPosition();
            fSharedInputValues.setBoolean("ipb_odometry_has_been_zeroed", true);
            sLogger.debug("Odometry Input -> Zeroed");
            return;
        }

        mHeading = getHeading();

        double leftPosition = fSharedInputValues.getNumeric(fLeftEncoder);
        double rightPosition = fSharedInputValues.getNumeric(fRightEncoder);

		/*
		"distance" is the straight line distance the robot has traveled since the last iteration

		We calculate the straight line distance the robot has traveled by averaging the left and right encoder deltas.
		This works because we take readings so frequently that curvature doesn't impact the straight line distance
		enough to accumulate much error.

		This could be improved later but in our testing it worked well.
		 */
        double distance = ((leftPosition - mLeftPosition) + (rightPosition - mRightPosition)) / 2;

        mCurrentPosition = new Pose2d(mCurrentPosition.add(new Vector(distance * Math.cos(Math.toRadians(mHeading)), distance * Math.sin(Math.toRadians(mHeading)))), mHeading);

        mLeftPosition = leftPosition;
        mRightPosition = rightPosition;
    }

    public void zeroPosition() {
        mLeftPosition = fSharedInputValues.getNumeric(fLeftEncoder);
        mRightPosition = fSharedInputValues.getNumeric(fRightEncoder);
        mCurrentPosition = new Pose2d();
    }

    @Override
    public Map<String, Double> get() {
        return Maps.of("x", mCurrentPosition.getX(), "y", mCurrentPosition.getY(), "heading", mCurrentPosition.getHeading());
    }

    private double getHeading() {
        mNavxValues = fSharedInputValues.getVector(fHeadingSensor);
        double heading = mNavxValues.getOrDefault(fHeadingKey, 0.0);

        if (heading > 180) heading = -(360 - heading);

        //Inverts the heading to so that positive angle is counterclockwise, this makes trig functions work properly
        heading = -heading;

        return heading;
    }

    @Override
    public void processFlag(String flag) {

    }
}
