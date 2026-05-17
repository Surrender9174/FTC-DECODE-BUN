package org.firstinspires.ftc.teamcode.Functions;

import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

public class Detection{
    private Camera camera;
    private Turret turret;
    private boolean initiateDetection, started;
    private double goalX, goalY, cameraAngle, goalYCamera, goalXCamera, goalXRobot, goalYRobot;
    private static double additionalGoalY, additionalGoalX, additionalGoalHeight, additionalValue, cameraDistance;
    private double goalHeight;
    private static double xoffset, yoffset;


    public Detection(AllObjects objects){
        camera = objects.camera;
        turret = objects.turret;

        initiateDetection = false;
    }

    public void update(){
        if(!initiateDetection)
            started = false;


        camera.detectGoal();

        goalX = camera.getGoalX();
        goalY = camera.getGoalY();

        cameraAngle = turret.getTurretAngle() + 270;

        goalXRobot = Math.cos(Math.toRadians(cameraAngle)) * cameraDistance + Math.cos(Math.toRadians(cameraAngle)) * goalXCamera - Math.sin(Math.toRadians(cameraAngle)) * goalYCamera;
        goalYRobot = additionalValue + Math.sin(Math.toRadians(cameraAngle)) * cameraDistance + Math.sin(Math.toRadians(cameraAngle)) * goalXCamera + Math.cos(Math.toRadians(cameraAngle)) * goalYCamera;

        goalX = robotX + Math.cos(Math.toRadians(robotH - 90)) * goalXRobot - Math.sin(Math.toRadians(robotH - 90)) * goalYRobot;
        goalY = robotY + Math.sin(Math.toRadians(robotH - 90)) * goalXRobot + Math.cos(Math.toRadians(robotH - 90)) * goalYRobot;

        goalX = goalX + additionalGoalX;
        goalY = goalY + additionalGoalY;
        goalHeight = camera.getGoalHeight() + additionalGoalHeight;

        telemetry.addData("GoalX", goalX);
        telemetry.addData("GoalY", goalY);
        telemetry.addData("CameraAngle", cameraAngle);
    }

    public void t_f_initiateDetection(){
        initiateDetection = true;
    }

    public void setOffsets(double additionalGoalX, double additionalGoalY){
        this.additionalGoalX = additionalGoalX;
        this.additionalGoalY = additionalGoalY;
    }
}