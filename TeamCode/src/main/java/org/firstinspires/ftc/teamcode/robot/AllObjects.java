package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.detection.Turret;

public class AllObjects {
    private ActiveIntake intake;
    private Chassis chassis;
    private Turret turret;

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        intake = new ActiveIntake(robot);
        turret = new Turret(robot);
    }

    public void update() {
        chassis.updateFieldCentric();
        intake.update();
        turret.update();
    }
}
