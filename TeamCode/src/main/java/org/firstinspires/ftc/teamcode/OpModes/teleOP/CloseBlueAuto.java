package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Autonomous(name="CloseBlue")
public class CloseBlueAuto extends OpMode {
    private Follower follower;
    private RobotHardware hardware;
    private AllObjects objects;

    @Override
    public void init(){
        follower.setStartingPose(new Pose(0,0));
    }
    @Override
    public void loop() {

    }
    public void updateHardware(){

    }
}
