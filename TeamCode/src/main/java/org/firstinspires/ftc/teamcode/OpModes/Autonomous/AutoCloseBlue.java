package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Functions.Commands;
import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.ServoIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

import java.util.concurrent.TimeUnit;

@Autonomous
public class AutoCloseBlue extends OpMode {
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
    public boolean time1 = true;

    public ElapsedTime timer = new ElapsedTime();


    public Pose startPose = new Pose(0, 0 , Math.toRadians(90));
    //public Pose pivot = new Pose( -10, 8, 90);
    public Pose PreloadPoseControl = new Pose(12, -22);
    public Pose PreloadPoseend = new Pose(9,  -35, Math.toRadians(70));

    public Pose getSpike1Control = new Pose(8, -50);
    public Pose getSpike1end = new Pose(-18, -50, Math.toRadians(0));

    public Pose shootSpike1Control = new Pose(2, -56);
    public Pose shootSpike1end = new Pose(20, -43, Math.toRadians(60));

    public Pose getSpike2Control = new Pose(18, -68);
    public Pose getSpike2end = new Pose(-20, -76, Math.toRadians(10));

    public Pose shootSpike2control = new Pose(27, -78);
    public Pose shootSpike2end = new Pose(17, -45, Math.toRadians(100));


    //public Pose spike2 = new Pose(-85, 45, 90);
    //public Pose spike2control = new Pose(7, 80,90);
    //public Pose pivot = new Pose(0, 10, 90);
    //public Pose scorePose = new Pose(23, 8, 31);


    private Path PreloadPath;
    private Path getSpike1;
    private Path shootSpike1;
    private Path getSpike2;
    private Path shootSpike2;

    @Override
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
//
//
//
        intake = new Intake(activeIntake, trapa, spindexer, servoIntake);
        detection = new Detection(objects);
        outtake = new Outtake(objects.turret, objects.shoot, objects.hood, objects.camera);
        transfer = new Transfer(objects.intake.spindexer, objects.trapa, objects.activeIntake, outtake, objects.shoot);

        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        //  objects.turret.setTargetPosition(90);

        // Checked should be good
        goalX = -15; goalY = 10;
        time1 = true;

        buildPath();
    }
    public void buildPath(){
        PreloadPath = new Path(new BezierCurve(startPose , PreloadPoseControl, PreloadPoseend));
        //backtoshoot = new Path(new BezierCurve(scorePose, startPose));
        //pathSpike2 = new Path(new BezierCurve(scorePose, spike2control, spike2));

        PreloadPath.setLinearHeadingInterpolation(Math.toRadians(90),  Math.toRadians(70));
        //scorePath.setLinearHeadingInterpolation(robotH, robotH+90);
        //backtoshoot.setConstantHeadingInterpolation(90);
        //pathSpike2.setTangentHeadingInterpolation();

        getSpike1 = new Path(new BezierCurve(PreloadPoseend, getSpike1Control, getSpike1end));

        getSpike1.setLinearHeadingInterpolation(Math.toRadians(70), Math.toRadians(0));

        shootSpike1 = new Path(new BezierCurve(getSpike1end, shootSpike1Control, shootSpike1end));

        shootSpike1.setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(60));

        getSpike2 = new Path(new BezierCurve(shootSpike1end, getSpike2Control, getSpike2end));

        shootSpike2 = new Path(new BezierCurve(getSpike2end, shootSpike2control, shootSpike2end));

        step = 0;
    }

    public void start(){
        timer.reset();

        follower.followPath(PreloadPath);
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
        telemetry.addData("X Y Z", follower.getPose());
//
        transfer.update();
        intake.update();
        objects.update2();
        commands.update();
        robot.update();
        detection.update();
    }

    public void update(){
        switch (step){
            case 0:
                if(!follower.isBusy())
                {
                    step = 1;
                    timer.reset();
                }
                break;
            case 1:
                telemetry.addData("AutoState", "Shooting Preload...");
                transfer.setState(Transfer.StateTransfer.INIT);
                if(transfer.getState() == Transfer.StateTransfer.IDLE)
                {
                    intake.setState(Intake.StateIntake.INTAKE);
                    follower.followPath(getSpike1);
                    timer.reset();
                    step = 2;
                }
                break;
            case 2:
                telemetry.addData("AutoState", "GettingSpike1");
                if(!follower.isBusy())
                {
                    intake.setState(Intake.StateIntake.OUTTAKE);
                    follower.followPath(shootSpike1);
                    step = 3;
                }
                break;
            case 3:
                intake.setState(Intake.StateIntake.INIT);
                telemetry.addData("AutoState", "Going to Shooting Zone...");
                if(!follower.isBusy()) {
                    transfer.setState(Transfer.StateTransfer.INIT);
                    if(transfer.getState() == Transfer.StateTransfer.IDLE)
                    {
                        telemetry.addData("AutoState", "Shooting Spike 1");
                        intake.setState(Intake.StateIntake.INTAKE);
                        follower.followPath(getSpike2);
                        step = 4;
                    }
                }
                break;
            case 4:
                telemetry.addData("AutoState", "Getting Spike 2...");
                if(!follower.isBusy())
                {
                    follower.followPath(shootSpike2);
                    intake.setState(Intake.StateIntake.INIT);
                    timer.reset();
                    step = 5;
                }
                break;
            case 5:
                telemetry.addData("AutoState","Going to shooting zone...");
                if(!follower.isBusy())
                {
                    telemetry.addData("AutoState", "Shooting Spike 2");
                    transfer.setState(Transfer.StateTransfer.INIT);
                    if(transfer.getState() == Transfer.StateTransfer.IDLE){
                        telemetry.addData("AutoState", "Finishing up...");
                        follower.pausePathFollowing();
                        outtake.resetTurret();
                        step=6;
                    }
                }
                break;
            case 6:
                telemetry.addData("AutoState","Finished!");
                break;
        }
    }
}
