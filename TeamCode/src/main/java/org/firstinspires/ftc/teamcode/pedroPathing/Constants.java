package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.drivetrains.SwerveConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    private static HardwareMap hardwareMap;

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(13.2);

    //PathConstraints need to be properly configured
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    //This is the Pinpoint Odometry setup for pedro
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-114)
            .strafePodX(-174)
            .distanceUnit(DistanceUnit.CM)
            .hardwareMapName("odometry")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    //This is the mecanum setup for pedro
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("motorFrontRight")
            .rightRearMotorName("motorBackRight")
            .leftRearMotorName("motorBackLeft")
            .leftFrontMotorName("motorFrontLeft")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE);

    public static SwerveConstants swerveConstants = new SwerveConstants()
            .maxPower(1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        //Constants.hardwareMap = hardwareMap;
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .swerveDrivetrain(swerveConstants)
                .build();
    }
}
