package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

import java.util.concurrent.TimeUnit;

@Autonomous(name="CloseBlue")
public class CloseBlueAuto extends OpMode {
    private Follower follower;
    private RobotHardware hardware;
    private AllObjects objects;

    // -------------- ExtraData for Auto -------------------

    private double initialGoalX, initialGoalY;

    private ElapsedTime timer = new ElapsedTime();

    // -------------- Enum with autoStates -----------------

    public enum autoStates{
        Preload,
        Scan,
        shootPreload,
        getSpike1,
        shootSpike1,
        getSpike2,
        getShootSpike2,
        shootSpike2,
        park;
    }
    private autoStates state;

    private boolean first=false;

    // -------------- Hardware and Classes Setup ------------------

    private Intake intake;
    private Outtake outtake;
    private Detection detection;
    private Transfer transfer;

    // -------------- Pedro Pathing setup -----------------

    // ----- Paths -----
    private Path startAuto;
    private Path getSpike2;
    private Path shootSpike2;

    // ----- Poses -----
    private Pose starting;
    private Pose startControl;
    private Pose startEndPose;
    private Pose getSpike2Control;
    private Pose getSpike2End;
    private Pose shootSpike2Control;
    private Pose shootSpike2End;
    public void initPoses()
    {
        starting = new Pose(34,133.2, Math.toRadians(270));
        startControl = new Pose(57.7, 111.1);
        startEndPose = new Pose(58.6,82, Math.toRadians(240));

        getSpike2Control = new Pose(60, 58.4);
        getSpike2End = new Pose(17.4, 57, Math.toRadians(170));

        shootSpike2Control = new Pose(47.2, 64.4);
        shootSpike2End = new Pose(58.2,81.7,Math.toRadians(240));
    }

    public void paths() {
        startAuto = new Path(new BezierCurve(starting, startControl, startEndPose));
        startAuto.setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(240));

        getSpike2 = new Path(new BezierCurve(startEndPose, getSpike2Control, getSpike2End));
        getSpike2.setLinearHeadingInterpolation(Math.toRadians(240), Math.toRadians(170));

        shootSpike2 = new Path(new BezierCurve(getSpike2End, shootSpike2Control, shootSpike2End));
        shootSpike2.setLinearHeadingInterpolation(Math.toRadians(170), Math.toRadians(240));
    }

    // ------------------------ Setting up the components ----------------------------

    @Override
    public void init(){
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);
        alliance = 1;

        hardware = new RobotHardware();
        hardware.init();

        objects = new AllObjects();
        objects.init_camera(hardware);
        objects.init(hardware);


        intake = objects.intake;
        transfer = objects.transfer;
        outtake = objects.outtake;
        detection = new Detection(objects);

        initPoses();

        follower = createFollower(hardwareMap);

        paths();
        state = autoStates.Scan;
    }

    @Override
    public void start(){
        detection.setGoalOffsets(25, -20);
        goalX = -356; goalY = 60;
        initialGoalX = goalX; initialGoalY = goalY;
        follower.setStartingPose(starting);
        follower.setHeading(Math.toRadians(270));
        follower.followPath(startAuto);
        timer.reset();
    }
    // ---------------------------------------------------------------------------------
    // ---------------------- Here is the code that runs everyloop ---------------------
    // ---------------------------------------------------------------------------------

    public void updateHardware(){

        intake.update();
        objects.update(); //Used to be update2, needs to be updated to the new codebase

        objects.turret.update();
        detection.update();
        if(hardware.isStable() && objects.turret.isStable()) detection.initiateDetection();
        transfer.update();

        hardware.update();
    }
    public void updateTelemetry(){
        telemetry.addLine("AutoDiagnosis");
        // --- GoalX/GoalY ---
        telemetry.addData("GoalX", goalX);
        telemetry.addData("GoalY", goalY);

        // --- Pedro path diagnostica ---
        telemetry.addData("state", state);
        telemetry.addData("isBusy", follower.isBusy());
        telemetry.addData("tValue", follower.getCurrentTValue());

        // --- shooter / transfer gate diagnostica ---
        telemetry.addData("transferState", transfer.getState());
        telemetry.addData("shooterTarget", org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot.targetSpeed);
        telemetry.addData("shooterSpeedDiff", objects.shoot.getSpeedDifference());

        telemetry.addLine("Other");
    }
    public void stateMachine() {
        switch(state){
            case Scan:
                if(!follower.isBusy()) {
                    follower.pausePathFollowing();
                    detection.initiateDetection();
                    state = autoStates.Preload;
                    timer.reset();
                }
                break;
            case Preload:
                if(initialGoalX != goalX) {
                    transfer.setState(Transfer.StateTransfer.INIT);
                    state = autoStates.shootPreload;
                    timer.reset();
                }
                break;
            case shootPreload:
                if(transfer.getState() == Transfer.StateTransfer.IDLE) {
                    intake.setState(Intake.StateIntake.INTAKE);
                    follower.followPath(getSpike2);
                    state = autoStates.getSpike2;
                }
                break;
            case getSpike2:
                if(!follower.isBusy())
                {
                    intake.setState(Intake.StateIntake.INIT);
                    follower.followPath(shootSpike2);
                    state = autoStates.shootSpike2;
                    first = true;
                }
                break;
            case shootSpike2:
                if(!follower.isBusy()) {
                    if(first)
                    {
                        follower.pausePathFollowing();
                        timer.reset();
                        first = false;
                    }
                    else if(timer.time(TimeUnit.SECONDS) > 0.4 && !first) {
                        transfer.setState(Transfer.StateTransfer.INIT);
                        state = autoStates.park;
                    }
                }
                break;
            case park:
                break;
        }
    }

    @Override
    public void loop() {
        follower.update();
        updateTelemetry();
        updateHardware();
        stateMachine();
    }

}
