package org.firstinspires.ftc.teamcode.Functions;


import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

@Configurable
public class Detection {
    private Camera camera;
    private Turret turret;
    private boolean detectStart=false;
    private double turretAngle;
    private double Xcamera, Ycamera, Xrobot, Yrobot, Xfield, Yfield;
    public Detection(AllObjects objects) {
        camera = objects.camera;
        turret = objects.turret;

        detectStart = false;
    }
    public void update() {
        if(!camera.detected()) return;

        if(!detectStart) return;

        camera.detectGoal();
        Xcamera = camera.getGoalX();
        Ycamera = camera.getGoalY();

        turretAngle = turret.getTurretAngle();

        Xrobot = Math.sin(Math.toRadians(turretAngle)) * 15 + Math.sin(Math.toRadians(turretAngle)) * Ycamera + Math.cos(Math.toRadians(turretAngle)) * Xcamera;
        Yrobot = Math.cos(Math.toRadians(turretAngle)) * 15 + Math.cos(Math.toRadians(turretAngle)) * Ycamera + Math.sin(Math.toRadians(turretAngle)) * Xcamera;

        Xfield = Math.sin(Math.toRadians(robotH)) * Yrobot + Math.cos(Math.toRadians(robotH)) * Xrobot + robotX;
        Yfield = 4 + Math.sin(Math.toRadians(robotH)) * Xrobot + Math.sin(Math.toRadians(robotH)) * Yrobot + robotY;
        detectStart = false;
    }
    public void startDetection(){
        detectStart = true;
    }
}
