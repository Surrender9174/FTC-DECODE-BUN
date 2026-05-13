package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.detection.Turret;
import org.firstinspires.ftc.teamcode.objects.indexer.Spindexer;

public class AllObjects {
    public ActiveIntake intake;
    public Chassis chassis;
    public Turret turret;
    public Spindexer spindexer;

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        intake = new ActiveIntake(robot);
        turret = new Turret(robot);
        spindexer = new Spindexer(robot);
    }

    public void update() {
        chassis.updateFieldCentric();
        intake.update();
        turret.update();
        spindexer.update();
    }
}
