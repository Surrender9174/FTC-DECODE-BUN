package org.firstinspires.ftc.teamcode.Functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.detect;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalHeight;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;

public class Detection {
    private Camera camera;
    private Turret turret;

    private double goalZ, goalXrobot, goalYrobot, goalZrobot, goalXfield, goalYfield, goalZfield;
    private static double xoffset, yoffset;

    public static double turretRadius, additionalDistance;

    public Detection(Camera camera, Turret turret)
    {
        this.camera = camera;
        this.turret = turret;
    }

    public void update(){
        if(!camera.detected())
            return;

        if(!detect) return;

        camera.searchGoal();
        camera.detectGoal();
        goalX = camera.getGoalX();
        goalY = camera.getGoalY();

        telemetry.addData("X", goalX);
        telemetry.addData("Y", goalY);

        goalXrobot = Math.cos(Math.toRadians(turret.getTurretAngle())) * turretRadius + goalX * Math.cos(Math.toRadians(turret.getTurretAngle() - 90)) - goalY * Math.sin(Math.toRadians(turret.getTurretAngle() - 90));
        goalYrobot = additionalDistance + Math.sin(Math.toRadians(turret.getTurretAngle())) * turretRadius + goalX * Math.sin(Math.toRadians(turret.getTurretAngle() - 90)) + goalY * Math.cos(Math.toRadians(turret.getTurretAngle() - 90));

        goalXfield = robotX + Math.cos(Math.toRadians(robotH)) * goalXrobot - Math.sin(Math.toRadians(robotH)) * goalYrobot;
        goalYfield = robotY + Math.sin(Math.toRadians(robotH)) * goalYrobot - Math.sin(Math.toRadians(robotH)) * goalXrobot;

        goalHeight = camera.getGoalHeight();

        goalX = goalXfield;
        goalY = goalYfield;

        detect = false;
    }
}
