package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.pedropathing.follower.Follower;
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

@Autonomous
public class AutoFarBlue extends OpMode {
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
    public ElapsedTime timer2 = new ElapsedTime();

    public double add90x = - 3.47, add90y = -4;

    public Pose startPose = new Pose(0, 0 , 90);
    //public Pose pivot = new Pose( -10, 8, 90);
    public Pose scorePose = new Pose(-19,  40, 90);


    //public Pose spike2 = new Pose(-85, 45, 90);
    //public Pose spike2control = new Pose(7, 80,90);
    //public Pose pivot = new Pose(0, 10, 90);
    //public Pose scorePose = new Pose(23, 8, 31);


    public Path scorePath;
    public Path backtoshoot;

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



        intake = new Intake(activeIntake, trapa, spindexer, servoIntake);
        detection = new Detection(objects);
        outtake = new Outtake(objects.turret, objects.shoot, objects.hood, objects.camera);
        transfer = new Transfer(objects.intake.spindexer, objects.trapa, objects.activeIntake, outtake, objects.shoot);

        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);

      //  objects.turret.setTargetPosition(90);

        goalX = -253; goalY = 243;
        time1 = true;

        buildPath();
    }
    public void buildPath(){
        scorePath = new Path(new BezierLine(startPose , scorePose));
        backtoshoot = new Path(new BezierLine(scorePose, startPose));
        //pathSpike2 = new Path(new BezierCurve(scorePose, spike2control, spike2));

        scorePath.setConstantHeadingInterpolation(90);
        //scorePath.setLinearHeadingInterpolation(robotH, robotH+90);
        backtoshoot.setConstantHeadingInterpolation(90);
        //pathSpike2.setTangentHeadingInterpolation();
        step = 0;
    }

    public void start(){
        timer.reset();
        timer2.reset();
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

        transfer.update();
        intake.update();
        objects.update2();
        commands.update();
        robot.update();
        //detection.update();
    }

    public void update(){
        switch (step){
            case 0:
                if(timer.seconds() > 0.8){
                    if(time1 && timer.seconds() > 0.3){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                        timer2.reset();
                    }
                    if(timer2.seconds() > 2.3) {
                        step = 1;
                    }
                }
                break;
            case 1:
                time1 = true;
                intake.setState(Intake.StateIntake.INTAKE);
                follower.followPath(scorePath);
                step = 2;

                break;
            case 2:
                if(!follower.isBusy()){
                    if(timer.seconds() > 1){
                        intake.setState(Intake.StateIntake.OUTTAKE);
                        if(timer.seconds() > 1.2){
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
                break;
            case 4:
                if(!follower.isBusy()){
                    if(time1 && timer.seconds() > 0.2){
                        transfer.setState(Transfer.StateTransfer.INIT);
                        time1 = false;
                    }

                    step = 5;
                }
                else{
                    timer.reset();
                }
                break;
            case 5:

                break;
        }
    }
}
