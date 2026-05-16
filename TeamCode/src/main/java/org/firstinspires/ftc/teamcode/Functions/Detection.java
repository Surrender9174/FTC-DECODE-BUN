package org.firstinspires.ftc.teamcode.Functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalHeight;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.goalY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

public class Detection {
    public Camera camera;
    public Turret turret;

    public double cameraX, cameraY, cameraAngle;
    public double goalXRobot, goalYRobot, goalYCamera, goalXCamera;
    public double cameraDistance = 0, additionalValue = 0, additionalGoalHeight = 30;
    public double additionalGoalX, additionalGoalY;
    public double lastGoalX, lastGoalY, last_lastGoalX, last_lastGoalY;
    public boolean started = false;

    public Detection(AllObjects objects){
        camera = objects.camera;
        turret = objects.turret;


    }

    public void update(){
        if(started) {
            started = false;

            camera.detectGoal();

            if(!camera.detected()) return;

            cameraX = camera.getGoalX();
            cameraY = camera.getGoalY();

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

            telemetry.addData("Searching", started);
            telemetry.addData("X", goalX);
            telemetry.addData("Y", goalY);

        }
    }
    public void startDectection(){
        started = true;
    }

    public void setOffsets(double ad){

    }
}
