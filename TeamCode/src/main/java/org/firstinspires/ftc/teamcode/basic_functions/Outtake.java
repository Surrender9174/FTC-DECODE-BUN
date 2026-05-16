package org.firstinspires.ftc.teamcode.basic_functions;

import org.firstinspires.ftc.robotcontroller.external.samples.externalhardware.RobotHardware;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Detection;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

public class Outtake {
    private Camera camera;
    private Detection detection;
    private Turret turret;
    private Shoot shoot;

    public Outtake(Turret turret, Shoot shoot, Camera camera){
        this.turret = turret;
        this.shoot = shoot;
        this.detection = new Detection(camera, turret);
    }

    public void update(){

    }

}
