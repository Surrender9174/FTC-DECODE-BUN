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
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@Autonomous
public class BlueFar extends OpMode {
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
    public Shoot shooter;
    public Camera camera;
    public Turret turret;

    private int step;
    public boolean time1 = true, ok = false;

    public ElapsedTime timer = new ElapsedTime();
    public ElapsedTime timer2 = new ElapsedTime();
    public ElapsedTime totaltime = new ElapsedTime();

    public double add90x = -3.47, add90y = -4;

    public Pose startPose = new Pose(0, 0, Math.toRadians(90));
    //ublic Pose startHumanPose = new Pose(40, -10, Math.toRadians(90));
    public Pose startHumanPose1 = new Pose(45.5, -2, Math.toRadians(90));
    public Pose HumanPose1 = new Pose(37, 0, Math.toRadians(90));
    public Pose shoot1 = new Pose(0, 5, Math.toRadians(90));
    public Pose spike3 = new Pose(10, 10, Math.toRadians(90));
    public Pose pivot1 = new Pose(20, 10, Math.toRadians(90));
    public Path StartHumanPose;
    public Path StartHumanPose1;
    public Path HumanPose;
    public Path backtoshoot;
    public Path Spike3;

    @Override
    public void init() {
        alliance = 1;
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
        objects.init_camera(robot);
        objects.init(robot);

        commands = new Commands();
        commands.init(objects, robot);

        camera = objects.camera;
        shooter = objects.shoot;
        trapa = objects.trapa;
        turret = objects.turret;
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

        //objects.turret.setTargetPosition(110);
        goalX = 310;
        goalY = 120;
        time1 = true;
        servoIntake.setState(ServoIntake.StariServoIntake.INTAKE);

        buildPath();
    }

    public void buildPath() {
        StartHumanPose = new Path(new BezierLine(startPose, startHumanPose1));
        StartHumanPose1 = new Path(new BezierLine(startHumanPose1, startPose));
        backtoshoot = new Path(new BezierLine(startHumanPose1, shoot1));
        Spike3 = new Path(new BezierCurve(shoot1, pivot1, spike3));

        StartHumanPose.setConstantHeadingInterpolation(Math.toRadians(0));
        StartHumanPose1.setConstantHeadingInterpolation(Math.toRadians(0));
        backtoshoot.setConstantHeadingInterpolation(90);
        Spike3.setTangentHeadingInterpolation();
        step = 0;
    }

    public void start() {
        time1 = true;
        timer.reset();
        timer2.reset();
        totaltime.reset();
    }

    public void loop() {
        update();
        follower.update();

        shooter.update();
        turret.update();
        camera.update();

        outtake.update();

        transfer.update();

        spindexer.update();
        trapa.update();
        servoIntake.update();
        activeIntake.update();

        intake.update();

        commands.update();
        robot.update();
    }

    public void update() {
        switch (step) {
            case -1:
                if (timer.seconds() > 29)
                    step = 0;
                break;
            case 0:
                if (time1 && Math.abs(shooter.getSpeedDifference()) <= 20) {
                    transfer.setState(Transfer.StateTransfer.INIT);
                    time1 = false;
                }
                if (transfer.getState() == Transfer.StateTransfer.FINISH) {
                    spindexer.setState(Spindexer.StateSpindexer.INTAKE);
                    timer.reset();
                    step = 1;
                }
                break;
            case 1:
                intake.setState(Intake.StateIntake.INTAKE);
                if (timer.seconds() > 0.3) {
                    follower.followPath(StartHumanPose);
                    timer.reset();
                    step = 2;
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    if (timer.seconds() > 0.8) {
                        intake.setState(Intake.StateIntake.OUTTAKE);
                        if (timer.seconds() > 1.0) {
                            timer.reset();
                            intake.setState(Intake.StateIntake.INIT);
                            follower.followPath(StartHumanPose1);
                        }
                    }
                    break;
                }
        }
    }
}
            /*case 2:
                follower.followPath(StartHumanPose1);
                step = 3;
                timer.reset();
                break;
            case 3:
                if(timer.seconds() > 10){
                    follower.followPath(backtoshoot);
                    time1 = true;
                    timer.reset();
                    step = 4;
                }
                break;
            case 4:
                if(!follower.isBusy()){
                    if (time1 && Math.abs(shooter.getSpeedDifference()) <= 20)
                    {
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if (transfer.getState() == Transfer.StateTransfer.FINISH)
                    {
                        spindexer.setState(Spindexer.StateSpindexer.INTAKE);
                        timer.reset();
                        step = 5;
                    }
                }
                break;
            case 5:
                intake.setState(Intake.StateIntake.INTAKE);
                if(timer.seconds() > 0.3){
                    //follower.followPath();
                }
                break;
        }
    }
}
            /*case 0:
                if(time1 &&  timer.seconds() > 2){
                    transfer.setState(Transfer.StateTransfer.INIT);
                    time1 = false;
                }
                if(timer.seconds() > 3.3){
                    timer.reset();
                    step = 1;
                }
                break;
            case 1:
                intake.setState(Intake.StateIntake.INTAKE);
                time1 = true;
                if(timer.seconds() > 0.2){
                    follower.followPath(scorePath);
                    timer.reset();
                    step = 2;
                }
                break;
            case 2:
                if(!follower.isBusy() || timer.seconds() > 3){
                    intake.setState(Intake.StateIntake.OUTTAKE);
                    if(timer.seconds() > 3.3){
                        intake.setState(Intake.StateIntake.INIT);
                        timer.reset();
                        step = 3;
                    }
                }
                break;
            case 3:
                if(timer.seconds() > 0.3){
                    follower.followPath(backtoshoot);
                    timer.reset();
                    step = 4;
                }
                break;
            case 4:
                if(!follower.isBusy() && timer.seconds() > 3.2){
                    if(time1){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(timer.seconds() > 4){
                        timer.reset();
                        step = 5;
                    }
                }
                break;
            case 5:
                intake.setState(Intake.StateIntake.INTAKE);
                time1 = true;
                if(timer.seconds() > 0.3){
                    follower.followPath(intakespike2);
                    timer.reset();
                    step = 6;
                }
                break;
            case 6:
                if(!follower.isBusy() && timer.seconds() > 2.5){
                    intake.setState(Intake.StateIntake.OUTTAKE);
                    if(timer.seconds() > 2.8){
                        intake.setState(Intake.StateIntake.INIT);
                        timer.reset();
                        step = 7;
                    }
                }
                break;
            case 7:
                if(timer.seconds() > 0.3){
                    follower.followPath(backtoshoot2);
                    timer.reset();
                    step = 8;
                }
            case 8:
                if(!follower.isBusy() && timer.seconds() > 3){
                    if(time1){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(timer.seconds() > 4){
                        timer.reset();
                        step = 9;
                    }
                }
                break;
            case 9:
                if(timer.seconds() > 0.5){
                    follower.followPath(backtointake);
                    intake.setState(Intake.StateIntake.INTAKE);
                    timer.reset();
                    step = 10;
                }
                break;
            case 10:
                if(timer.seconds() > 1.5){
                    intake.setState(Intake.StateIntake.OUTTAKE);
                    follower.followPath(backtoshoot3);
                    if(timer.seconds() > 2.0)
                        intake.setState(Intake.StateIntake.INIT);
                    timer.reset();
                    time1 = true;
                    step = 11;
                }
                break;
            case 11:
                if(!follower.isBusy() && timer.seconds() > 0.5){
                    if(timer.seconds() > 1 && time1) {
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(timer.seconds() > 3){
                       timer.reset();
                       step = 12;
                    }
                }
                break;
            case 12:
                follower.followPath(leave);
                if(timer.seconds() > 0.5){
                    follower.pausePathFollowing();
                    robot.odometry.resetPosAndIMU();

                    objects.camera.resetDetection();

                    objects.turret.setTargetPosition(0);
                }
                break;
        }
            /*case 0:
                if(timer.seconds() > 1){
                    if(time1){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                        //timer2.reset();
                    }
                    if(timer.seconds() > 4) {
                        timer.reset();
                        step = 1;
                    }
                }
                break;
            case 1:
                time1 = true;
                intake.setState(Intake.StateIntake.INTAKE);
                if(timer.seconds() > 0.2){
                    follower.followPath(scorePath);
                    step = 2;
                }
                break;
            case 2:
                if(!follower.isBusy()){
                    if(timer.seconds() > 1){
                        intake.setState(Intake.StateIntake.OUTTAKE);
                        if(timer.seconds() > 4){
                            step = 3;

                        }
                    }
                }
                else{
                    timer.reset();
                }
                break;
            case 3:
                intake.setState(Intake.StateIntake.INIT);
                follower.followPath(backtoshoot);
                step = 4;
                timer.reset();
                break;
            case 4:
                if(!follower.isBusy()){
                    if(time1 && timer.seconds() > 0.9){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;

                    }
                    if(timer.seconds() > 4){
                        step = 5;
                        timer.reset();
                    }

                }
                else{
                    timer.reset();
                }

                break;
            case 5:
                intake.setState(Intake.StateIntake.INTAKE);
                time1 = true;
                if (timer.seconds() > 0.2){
                    follower.followPath(intakespike2);
                    step = 6;
                }
                break;
            case 6:
                if(!follower.isBusy()){
                    if(timer.seconds() > 1){
                        intake.setState(Intake.StateIntake.OUTTAKE);
                    }
                    if(timer.seconds() > 1.2){
                        timer.reset();
                        step = 7;
                    }
                }
                else{
                    timer.reset();
                }

                break;
            case 7:
                intake.setState(Intake.StateIntake.INIT);
                follower.followPath(backtoshoot2);
                timer.reset();
                step = 8;

                break;
            case 8:
                if(!follower.isBusy()){
                    if(time1 && timer.seconds() > 2){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                }
                break;
        }
    }*/
