package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;

public class Detection {
    private Camera camera;
    private Turret turret;

    private double goalZ, goalXrobot, goalYrobot, goalZrobot, goalXfield, goalYfield, goalZfield;
    private static double xoffset, yoffset;

    public static double turretRadius, additionalDistance;

    public void update(){
        if(!camera.detected())
            return;

        goalX = camera.getGoalX();
        goalY = camera.getGoalY();

        goalXrobot = Math.cos(Math.toRadians(turret.getTurretAngle())) * turretRadius + goalX * Math.cos(Math.toRadians(turret.getTurretAngle() - 90)) - goalY * Math.sin(Math.toRadians(turret.getTurretAngle() - 90));
        goalYrobot = additionalDistance + Math.sin(Math.toRadians(turret.getTurretAngle())) * turretRadius + goalX * Math.sin(Math.toRadians(turret.getTurretAngle() - 90)) + goalY * Math.cos(Math.toRadians(turret.getTurretAngle() - 90));

        goalXfield = robotX + Math.cos(Math.toRadians(robotH)) * goalXrobot - Math.sin(Math.toRadians(robotH)) * goalYrobot;
        goalYfield = robotY + Math.sin(Math.toRadians(robotH)) * goalYrobot - Math.sin(Math.toRadians(robotH)) * goalXrobot;
    }
}
