package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.createFollower;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.driveConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.followerConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.localizerConstants;
import static org.firstinspires.ftc.teamcode.pedroPathing.Constants.pathConstraints;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Command;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@Autonomous
public class AutoTest extends OpMode {
    public Follower follower;
    public RobotHardware robot;
    public AllObjects objects;

    public int step;

    public Pose startPose = new Pose(0, 0, 90);
    public Pose scorePose = new Pose(10,20, 90);
    public Pose pivot = new Pose(20, 30, 90);

    public Path scorePath;


    public void init(){
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
        objects.init_camera(robot);

        follower = createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        buildPath();
    }

    public void buildPath(){
        scorePath = new Path(new BezierCurve(startPose, pivot, scorePose));
        step = 0;
    }

    public void start(){
        objects.init(robot);
    }

    public void loop(){
        follower.update();

        update();

    }

    public void update(){
        switch (step){
            case 0:
                follower.followPath(scorePath);
                step = 1;
                break;
            case 1:
                if(!follower.isBusy()){
                    objects.transfer.setState(Transfer.StateTransfer.INIT);
                    step = 2;
                }
                break;
            case 2:
                break;
        }
    }
}
