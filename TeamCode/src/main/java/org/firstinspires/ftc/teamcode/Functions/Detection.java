package org.firstinspires.ftc.teamcode.Functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalHeight;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

@Configurable
public class Detection {
    private Camera camera;
    private Turret turret;

    private boolean initiateDetection;

    public static double cameraDistance = 15.5, additionalValue = 4.3, additionalGoalHeight = 30;
    public static double additionalGoalX, additionalGoalY;
    private double cameraAngle;
    private double goalXCamera, goalYCamera, goalXRobot, goalYRobot;
    private double lastGoalX, lastGoalY, last_lastGoalX, last_lastGoalY;

    private ElapsedTime timer = new ElapsedTime();

    public Detection (AllObjects objects) {
        camera = objects.camera;
        turret = objects.turret;

        initiateDetection = false;
    }

    public void update() {
        telemetry.addData("goalXRobot", goalXRobot);
        telemetry.addData("goalYRobot", goalYRobot);
        telemetry.addLine("");

        if (initiateDetection) {
            initiateDetection = false;

            if (timer.seconds() < 0.5) return;

            camera.detectGoal();

            if (!camera.detected()) return;

            if (camera.isDead()) {
                goalX = last_lastGoalX;
                goalY = last_lastGoalY;

                return;
            }

            goalXCamera = camera.getGoalX();
            goalYCamera = camera.getGoalY();

            cameraAngle = turret.getTurretAngle() + 270;

            goalXRobot = Math.cos(Math.toRadians(cameraAngle)) * cameraDistance + Math.cos(Math.toRadians(cameraAngle)) * goalXCamera - Math.sin(Math.toRadians(cameraAngle)) * goalYCamera;
            goalYRobot = additionalValue + Math.sin(Math.toRadians(cameraAngle)) * cameraDistance + Math.sin(Math.toRadians(cameraAngle)) * goalXCamera + Math.cos(Math.toRadians(cameraAngle)) * goalYCamera;

            goalX = robotX + Math.cos(Math.toRadians(robotH - 90)) * goalXRobot - Math.sin(Math.toRadians(robotH - 90)) * goalYRobot;
            goalY = robotY + Math.sin(Math.toRadians(robotH - 90)) * goalXRobot + Math.cos(Math.toRadians(robotH - 90)) * goalYRobot;

            goalX = goalX + additionalGoalX;
            goalY = goalY + additionalGoalY;
            goalHeight = camera.getGoalHeight() + additionalGoalHeight;

            last_lastGoalX = lastGoalX; last_lastGoalY = lastGoalY;
            lastGoalX = goalX; lastGoalY = goalY;


            timer.reset();
        }
    }

    public void initiateDetection() {
        initiateDetection = true;
    }

    public void setGoalOffsets(double additionalGoalX, double additionalGoalY) {
        this.additionalGoalX = additionalGoalX;
        this.additionalGoalY = additionalGoalY;
    }


}
