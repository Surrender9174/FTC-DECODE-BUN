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
public class AutoTestBlue extends OpMode {
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
    public int step;
    public boolean time1 = true;
    public ElapsedTime timer = new ElapsedTime();


    public Pose startPose = new Pose(56, 8, 90);
    public Pose pivot = new Pose(0, 40, 90);
    public Pose scorePose = new Pose(9, 60, 90);
    public Pose spike2 = new Pose(-85, 45, 90);
    public Pose spike2control = new Pose(7, 80,90);
    //public Pose pivot = new Pose(0, 10, 90);
    //public Pose scorePose = new Pose(23, 8, 31);


    public Path scorePath;
    public Path pathSpike2;

    public void init(){
        alliance = 1;
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

        goalX = -75; goalY = 15;

        buildPath();
    }

    public void buildPath(){
        scorePath = new Path(new BezierCurve(startPose, pivot , scorePose));
        pathSpike2 = new Path(new BezierCurve(scorePose, spike2control, spike2));

        //scorePath.setLinearHeadingInterpolation(90, 180);
        scorePath.setTangentHeadingInterpolation();
        pathSpike2.setTangentHeadingInterpolation();
        step = 0;
    }

    public void loop(){
        update();

        follower.update();
        telemetry.addData("Pos", follower.getPose());
        telemetry.addData("TrapaState", trapa.getState());
        telemetry.addData("GoalX", goalX);
        telemetry.addData("GoalY", goalY);
        telemetry.addData("STATE", step);
        telemetry.addData("UnghiTurreta", objects.turret.getTurretAngle());

        transfer.update();
        intake.update();
        objects.update2();
        commands.update();
        robot.update();
        //detection.update();
    }

    public void update(){

        follower.getPose();
        switch (step){
            case 0:
                follower.followPath(scorePath);
                spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
                trapa.setState(Trapa.StateTrapa.OUTTAKE);
                servoIntake.setState(ServoIntake.StariServoIntake.OUTTAKE);
                step = 1;
                break;
            case 1:
                if(!follower.isBusy()){
                    if(time1) {
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(timer.seconds() > 0.85) {
                        spindexer.setState(Spindexer.StateSpindexer.INTAKE);
                        servoIntake.setState(ServoIntake.StariServoIntake.INTAKE);
                        //activeIntake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);
                        trapa.setState(Trapa.StateTrapa.INTAKE);
                        step = 2;
                    }
                }
                else{
                    timer.reset();
                }
                break;
            case 2:
                activeIntake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);
                follower.followPath(pathSpike2);
                timer.reset();
                step = 3;
                break;
            case 3:
                if(timer.seconds() > 1.65){
                    activeIntake.setState(ActiveIntake.ActiveIntakeStates.INIT);
                    spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
                    step = 4;
                }
                break;
            case 4:
                follower.followPath(scorePath);
                //objects.trapa.setState(Trapa.StateTrapa.OUTTAKE);
                step = 5;
                break;
            case 5:
                if(!follower.isBusy()){
                    transfer.setState(Transfer.StateTransfer.INIT);
                    if(timer.seconds() > 0.75)
                        step = 6;
                }
                else{
                    timer.reset();
                }
        }
    }
}
