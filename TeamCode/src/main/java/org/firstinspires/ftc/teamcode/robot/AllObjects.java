package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.detection.Camera;
import org.firstinspires.ftc.teamcode.detection.Turret;

public class AllObjects {
    public ActiveIntake intake;
    public Chassis chassis;
    public Turret turret;
    private Camera camera;

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        intake = new ActiveIntake(robot);
        camera = new Camera(robot);
        turret = new Turret(robot);
    }

    public void update() {
        chassis.updateFieldCentric();
        intake.update();
        turret.update();
        camera.update();
    }
}
