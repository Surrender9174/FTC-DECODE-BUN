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
public class BlueFarNita extends OpMode {
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

    public enum Stari{

        PRELOAD,
        HUMAN,
        SPIKE3,
        CICLE1,
        CICLE2,
        CICLE3,
        LEAVE;
    }
    public enum StepsIntake{
        MOVEINTAKE,
        MOVESHOOT;
    }


    public ElapsedTime timer = new ElapsedTime();
    public ElapsedTime timer2 = new ElapsedTime();
    public ElapsedTime totaltime = new ElapsedTime();

    public double add90x = -3.47, add90y = -4;

    public Pose robotpose = new Pose();
    public Pose startPose = new Pose(0, 0, Math.toRadians(90));
    //public Pose pivot = new Pose( -10, 8, 90);
    public Pose humanPose = new Pose(40, -5, Math.toRadians(90));
    public Pose humanPose2 = new Pose(36, -4, Math.toRadians(90));
    public Pose humanPose3 = new Pose(40, 0, Math.toRadians(90));
    public Pose spike3Pose = new Pose(40, -30, Math.toRadians(90));
    public Pose spike3Pivot1 = new Pose(0, -20, Math.toRadians(90));
    public Pose startPoseAfterspike = new Pose(-5, 0, Math.toRadians(90));
    //public Pose spike3Pivot2 = new Pose(45, 0, Math.toRadians(90));
    public Pose leavePose = new Pose(20, -20, Math.toRadians(90));

    public PathChain humanPathChain;
    public Path humanPath1, humanPath2, humanPath3;
    public Path backtoshoot;
    public Path spike3Path;
    public Path backtoshoot2;
    public Path leave;

    public Stari state;

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

        //transfer = objects.transfer;
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

        //  objects.turret.setTargetPosition(90);// -110, 330;
        //goalX = 305.5; goalY = 125;
        time1 = true;
        //objects.turret.setTargetPosition(-90);


        buildPath();
    }

    public void buildPath() {
        humanPath1 = new Path(new BezierLine(startPose, humanPose));
        humanPath2 = new Path(new BezierLine(humanPose, humanPose2));
        humanPath3 = new Path(new BezierLine(humanPose2, humanPose3));
        backtoshoot = new Path(new BezierLine(humanPose3, startPose));
        spike3Path = new Path(new BezierCurve(startPose, spike3Pivot1, spike3Pose));
        backtoshoot2 = new Path(new BezierLine(spike3Pose, startPoseAfterspike));
        leave = new Path(new BezierLine(startPoseAfterspike, leavePose));

        humanPath1.setConstantHeadingInterpolation(Math.toRadians(0));
        humanPath2.setConstantHeadingInterpolation(Math.toRadians(0));
        humanPath3.setConstantHeadingInterpolation(Math.toRadians(0));
        backtoshoot.setConstantHeadingInterpolation(Math.toRadians(0));
        spike3Path.setTangentHeadingInterpolation();
        backtoshoot2.setConstantHeadingInterpolation(0);
        leave.setConstantHeadingInterpolation(Math.toRadians(0));

        humanPathChain = new PathChain(humanPath1, humanPath2, humanPath3);

        step = 0;
    }
    @Override
    public void start() {
        //detection.setGoalOffsets(-15, 20);
        time1 = true;
        timer.reset();
        timer2.reset();
        totaltime.reset();
    }
    @Override
    public void loop() {
        update();

        telemetry.addData("Step", step);

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

        robot.update();
    }


    public void update() {
        /*switch (state){
            case PRELOAD:
                if(Shoot() == 1){
                    state = Stari.HUMAN;
                }
                break;
            case HUMAN:
                MoveChain(humanPathChain);
                BackToShoot(backtoshoot);
                if(!follower.isBusy()){
                    Shoot();
                }
                state = Stari.SPIKE3;
                break;
            case SPIKE3:
                MovePath(spike3Path);
                BackToShoot(backtoshoot2);
                if(!follower.isBusy()){
                    Shoot();
                }
                state = Stari.CICLE1;
                break;
            case CICLE1:
                MoveChain(humanPathChain);
                BackToShoot(backtoshoot);
                if(!follower.isBusy()){
                    Shoot();
                }
                state = Stari.CICLE2;
                break;
            case CICLE2:
                MoveChain(humanPathChain);
                BackToShoot(backtoshoot);
                if(!follower.isBusy()){
                    Shoot();
                }
                state = Stari.CICLE3;
                break;
            case CICLE3:
                MoveChain(humanPathChain);
                BackToShoot(backtoshoot);
                if(!follower.isBusy()){
                    Shoot();
                }
                state = Stari.LEAVE;
                break;
            case LEAVE:
                MovePath(leave);
                camera.resetDetection();
                turret.setTargetPosition(0);
                break;
        }

        */
        switch (step){
            case -1:
                break;
            case 0:
                if(time1 && Math.abs(shooter.getSpeedDifference()) <= 20){
                    transfer.setState(Transfer.StateTransfer.INIT);
                    time1 = false;
                }
                if(transfer.getState() == Transfer.StateTransfer.FINISH) {
                    timer.reset();
                    step = 1;
                }
                break;
            case 1:
                intake.setState(Intake.StateIntake.INTAKE);
                if(timer.seconds() > 0.3){
                    follower.followPath(humanPathChain);
                    timer.reset();
                    step = 2;
                }

                break;
            case 2:
                if(!follower.isBusy()){
                    if(timer.seconds() > 0.4){
                        intake.setState(Intake.StateIntake.INIT);
                        time1 = true;
                        step = 3;
                    }
                    else if(timer.seconds() > 0.1)
                        intake.setState(Intake.StateIntake.OUTTAKE);
                }
                else{
                    timer.reset();
                }
                break;
            case 3:
                follower.followPath(backtoshoot);
                timer.reset();
                step = 4;
                break;
            case 4:
                if(!follower.isBusy()){
                    if(time1 && Math.abs(shooter.getSpeedDifference()) <= 20 && timer.seconds() > 0.3){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(transfer.getState() == Transfer.StateTransfer.FINISH) {
                        timer.reset();
                        step = 5;
                    }
                }
                else{
                    timer.reset();
                }
                break;
            case 5:
                intake.setState(Intake.StateIntake.INTAKE);
                if(timer.seconds() > 0.3) {
                    follower.followPath(spike3Path);
                    timer.reset();
                    step = 6;
                }
                break;
            case 6:
                if(!follower.isBusy()){
                    if(timer.seconds() > 0.4){
                        intake.setState(Intake.StateIntake.INIT);
                        time1 = true;
                        step = 7;
                    }
                    else if(timer.seconds() > 0.1) intake.setState(Intake.StateIntake.OUTTAKE);
                }
                else{
                    timer.reset();
                }
                break;
            case 7:
                follower.followPath(backtoshoot2);
                timer.reset();
                step = 8;
                break;
            case 8:
                if(!follower.isBusy()){
                    if(time1 && Math.abs(shooter.getSpeedDifference()) <= 20 && timer.seconds() > 0.3){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(transfer.getState() == Transfer.StateTransfer.FINISH) {
                        timer.reset();
                        step = 9;
                    }
                }
                else{
                    timer.reset();
                }
                break;
            case 9:
                intake.setState(Intake.StateIntake.INTAKE);
                if(timer.seconds() > 0.3){
                    follower.followPath(humanPathChain);
                    timer.reset();
                    step = 10;
                }
            case 10:
                if(!follower.isBusy()){
                    if(timer.seconds() > 0.4){
                        intake.setState(Intake.StateIntake.INIT);
                        time1 = true;
                        step = 11;
                    }
                    else if(timer.seconds() > 0.1) intake.setState(Intake.StateIntake.OUTTAKE);
                }
                else{
                    timer.reset();
                }
                break;
            case 11:
                follower.followPath(backtoshoot2);
                timer.reset();
                step = 12;
            case 12:
                if(!follower.isBusy()){
                    if(time1 && Math.abs(shooter.getSpeedDifference()) <= 20 && timer.seconds() > 0.3){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }
                    if(transfer.getState() == Transfer.StateTransfer.FINISH) {
                        timer.reset();
                        step = 13;
                    }
                }
                else{
                    timer.reset();
                }
                break;
            case 13:
                follower.followPath(leave);
                camera.resetDetection();
                turret.setTargetPosition(0);
                step = -1;
                break;
        }

    }
    public void MovePath(Path path){
        intake.setState(Intake.StateIntake.INTAKE);
        if(timer.seconds() > 0.3){
            follower.followPath(path);
            timer.reset();
        }
    }
    public void MoveChain(PathChain pathChain){
        intake.setState(Intake.StateIntake.INTAKE);
        if(timer.seconds() > 0.3){
            follower.followPath(pathChain);
            timer.reset();
        }
    }

    public void BackToShoot(Path path){
        if(timer.seconds() > 0.4){
            intake.setState(Intake.StateIntake.INIT);
            time1 = true;
            follower.followPath(backtoshoot);
            timer.reset();
        }
        else if(timer.seconds() > 0.1)
            intake.setState(Intake.StateIntake.OUTTAKE);
    }

    public void Shoot(){
        if(time1 && Math.abs(shooter.getSpeedDifference()) <= 20 && timer.seconds() > 0.3){
            transfer.setState(Transfer.StateTransfer.INIT);
            time1 = false;
        }
        if(transfer.getState() == Transfer.StateTransfer.FINISH) {
            time1 = true;
            timer.reset();
        }
    }
}
