package org.firstinspires.ftc.teamcode.detection;

public class Detection {
    private Camera camera;
    private Turret turret;

    private double goalX, goalY, goalZ, goalXrobot, goalYrobot, goalZrobot, goalXfield, goalYfield, goalZfield;
    private static double xoffset, yoffset;

    public void update(){
        if(!camera.detected())
            return;



        goalX = camera.getGoalX();
        goalY = camera.getGoalY();

        goalXrobot = goalX * Math.sin(Math.toRadians(turret.getAngle()));

    }
}
