package org.firstinspires.ftc.teamcode.basic_functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.detect;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.externalhardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

import java.util.concurrent.TimeUnit;

public class Outtake {
    private Camera camera;
    private Detection detection;
    private Turret turret;
    private Shoot shoot;
    private Hood hood;
    private double goalAngle, goalDistance;
    private double lastRobotX, lastRobotY;
    private ElapsedTime timerstatic = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    public Outtake(Shoot shoot, Turret turret, Camera camera)
    {
        this.shoot = shoot;
        this.turret = turret;
        this.detection = new Detection(camera, turret);
        timerstatic.reset();
    }

    public void update()
    {
        camera.update();
        detection.update();
        goalAngle = Math.toDegrees(Math.atan2(goalY - robotY, goalX - robotX));
        goalDistance = Math.hypot(goalX-robotX, goalY-robotY);
        turret.setTargetPosition(robotH - goalAngle + 180);
        if(Math.abs(robotX-lastRobotX) > 3 || Math.abs(robotY-lastRobotY) > 3) timerstatic.reset();

        if(timerstatic.time(TimeUnit.SECONDS) > 2) detect = true;

        lastRobotX = robotX;
        lastRobotY = robotY;

    }
}
