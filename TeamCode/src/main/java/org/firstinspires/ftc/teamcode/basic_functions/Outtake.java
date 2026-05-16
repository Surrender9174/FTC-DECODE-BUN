package org.firstinspires.ftc.teamcode.basic_functions;

import static androidx.core.math.MathUtils.clamp;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.detect;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.externalhardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

import java.util.concurrent.TimeUnit;

@Configurable
public class Outtake {
    private Camera camera;


    public static double shooterSpeed = 0, anglePosition = 0.09;
    public static double KangleAdjustment = 0;

    private Detection detection;
    private Turret turret;
    private Shoot shooter;
    private Hood hood;
    private double goalAngle, goalDistance;
    private double lastRobotX, lastRobotY;
    private ElapsedTime timerstatic = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    private double turretAngle;

    private boolean transferMode;

    public Outtake(Shoot shoot, Turret turret, Camera camera, Hood hood) {
        shooter = shoot;
        this.turret = turret;
        this.camera = camera;
        this.hood = hood;
        this.detection = new Detection(camera, turret);
        timerstatic.reset();
    }

    public void update() {
        camera.update();

        if (Math.abs(robotX - lastRobotX) > 3 || Math.abs(robotY - lastRobotY) > 3)
            timerstatic.reset();

        if (timerstatic.time(TimeUnit.SECONDS) > 2) detect = true;
        detection.update();

        goalAngle = Math.toDegrees(Math.atan2(goalY - robotY, goalX - robotX));
        goalDistance = Math.hypot(goalX - robotX, goalY - robotY);

        turretAngle = robotH - goalAngle + 180;

        if (turretAngle > 180) turretAngle = turretAngle - 360;
        if (turretAngle < -180) turretAngle = turretAngle + 360;

        turretAngle = clamp(turretAngle, -170, 160);

        turret.setTargetPosition(turretAngle);

        if (goalX != 0 && goalY != 0) {
            if (!camera.isTrackingMotif())
                turret.setTargetPosition(turretAngle);

//            shooter.setTargetSpeed(shooterSpeed);
//            hood.setPosition(anglePosition - shooter.speedDifference() * KangleAdjustment);

            /*if (transferMode) shooter.setTargetSpeed(getShooterSpeed(goalDistance));
            else shooter.setTargetSpeed(2000);

            hood.setPosition(getAngle(goalDistance) - shooter.speedDifference() * KangleAdjustment);
            */

            lastRobotX = robotX;
            lastRobotY = robotY;
            }
        }
    public double getShooterSpeed(double dist) {
        return dist;
    }
    public double getAngle(double angle){
        return angle;
    }
}
