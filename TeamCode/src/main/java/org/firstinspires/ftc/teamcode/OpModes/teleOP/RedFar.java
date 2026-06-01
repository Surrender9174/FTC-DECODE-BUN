package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.driveConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.followerConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.localizerConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.pathConstraints;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.Functions.Commands;
import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.ServoIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@Autonomous
public class RedFar extends OpMode {
    public Follower follower;
    public RobotHardware robot;
    public AllObjects objects;

    public Commands commands;
    public Outtake outtake;
    public Transfer transfer;

    public Intake intake;
    public Detection detection;
    public ActiveIntake activeIntake;
    public ServoIntake servoIntake;
    public Spindexer spindexer;
    public Trapa trapa;
    private int step;
    public boolean time1 = true, ok = false;

    public ElapsedTime timer = new ElapsedTime();
    public ElapsedTime timer2 = new ElapsedTime();
    public ElapsedTime totaltime = new ElapsedTime();

    public double add90x = -3.47, add90y = -4;

    public Pose robotpose = new Pose();
    public Pose startPose = new Pose(0, 0, Math.toRadians(90));
    //public Pose pivot = new Pose( -10, 8, 90);
    public Pose scorePose = new Pose(40, 1, Math.toRadians(0));
    //public Pose pivotScorePose = new Pose(3, 20, Math.toRadians(90));

    public Pose spike2 = new Pose(-30, 39.3, Math.toRadians(90));
    public Pose backtoshootspike2 = new Pose(-9, 0, Math.toRadians(90));

    public Pose pivots2 = new Pose(-25, 18, Math.toRadians(90));
    public Pose pivots3 = new Pose(-10, 30, Math.toRadians(90));
    public Pose leave1 = new Pose(-30, 0, Math.toRadians(90));

    //public Pose spike2 = new Pose(-85, 45, 90);
    //public Pose spike2control = new Pose(7, 80,90);
    //public Pose pivot = new Pose(0, 10, 90);
    //public Pose scorePose = new Pose(23, 8, 31);

    public Path leaving;
    public Path scorePath;
    public Path backtoshoot;
    public Path intakespike2;
    public Path backtoshoot2;
    public Path backtointake;
    public Path backtoshoot3;
    public Path leave;

    @Override
    public void init() {
        alliance = -1;
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
        objects.init_camera(robot);
        objects.init(robot);

        commands = new Commands();
        commands.init(objects, robot);

        transfer = objects.transfer;
        trapa = objects.trapa;
        activeIntake = objects.activeIntake;
        spindexer = objects.spindexer;
        servoIntake = objects.servoIntake;


        intake = new Intake(activeIntake, trapa, spindexer, servoIntake);
        detection = new Detection(objects);
        outtake = new Outtake(objects.turret, objects.shoot, objects.hood, objects.camera);
        transfer = new Transfer(objects.intake.spindexer, objects.trapa, objects.activeIntake, outtake, objects.shoot);

        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        follower.setHeading(Math.toRadians(90));

        //  objects.turret.setTargetPosition(90);// -110, 330;
        goalX = -324.5; goalY = 146;
        time1 = true;
        //objects.turret.setTargetPosition(-90);


        buildPath();
    }

    public void buildPath() {

        scorePath = new Path(new BezierLine(startPose, scorePose));
        backtoshoot = new Path(new BezierLine(scorePose, startPose));
        intakespike2 = new Path(new BezierCurve(startPose, pivots2, spike2));
        backtoshoot2 = new Path(new BezierCurve(spike2, pivots3, backtoshootspike2));
        backtointake = new Path(new BezierLine(backtoshootspike2, scorePose));
        backtoshoot3 = new Path(new BezierLine(scorePose, backtoshootspike2));
        leave = new Path(new BezierLine(backtoshootspike2, leave1));

        leaving = new Path(new BezierLine(startPose, leave1));

        scorePath.setConstantHeadingInterpolation(Math.toRadians(0));
        backtoshoot.setConstantHeadingInterpolation(Math.toRadians(0));
        intakespike2.setTangentHeadingInterpolation();
        backtoshoot2.setTangentHeadingInterpolation();
        backtoshoot2.reverseHeadingInterpolation();
        backtointake.setConstantHeadingInterpolation(Math.toRadians(0));
        backtoshoot3.setConstantHeadingInterpolation(Math.toRadians(0));
        leave.setConstantHeadingInterpolation(Math.toRadians(0));
        step = 0;
    }
    @Override
    public void start() {
        detection.setGoalOffsets(-15, 20);
        timer.reset();
        timer2.reset();
        totaltime.reset();
    }
    @Override
    public void loop() {
        //if (totaltime.seconds() < 29) {
            update();

            follower.update();
            telemetry.addData("Pos", follower.getPose());
            telemetry.addData("TrapaState", trapa.getState());
            telemetry.addData("GoalX", goalX);
            telemetry.addData("GoalY", goalY);
            telemetry.addData("STATE", step);
            telemetry.addData("UnghiTurreta", objects.turret.getTurretAngle());
            telemetry.addData("X Y Z", follower.getPose());

            transfer.update();
            intake.update();
            objects.update2();
            commands.update();
            robot.update();
        //} else {
        //    objects.camera.resetDetection();
        //    objects.turret.setTargetPosition(0);
        //}

        //detection.update();
    }
    public void update() {
        robotpose = follower.getPose();
        telemetry.addData("XAUTO", robotpose.getX());
        telemetry.addData("YAUTO", robotpose.getY());
        telemetry.addData("HAUTO", robotpose.getHeading());
        switch (step){
            case 0:
                if(objects.shoot.getSpeedDifference() > 20) break;
                transfer.setState(Transfer.StateTransfer.INIT);
                step = 1;
                break;
            case 1:
                break;
        }

    }
}
