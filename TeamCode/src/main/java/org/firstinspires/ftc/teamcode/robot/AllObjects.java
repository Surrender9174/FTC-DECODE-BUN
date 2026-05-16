package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Detection;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;

public class AllObjects {
    public ActiveIntake activeIntake;
    public Intake intake;
    public Trapa trapa;
    public Chassis chassis;
    public Turret turret;
    private Camera camera;
    private Shoot shoot;
    private Spindexer spindexer;
    private Detection detection;

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        activeIntake = new ActiveIntake(robot);
        trapa = new Trapa(robot);
        spindexer = new Spindexer(robot);

        camera = new Camera(robot);
        turret = new Turret(robot);
        shoot = new Shoot(robot);

        intake = new Intake(activeIntake, trapa, spindexer);
        detection = new Detection(camera, turret);
    }

    public void update() {
        chassis.updateFieldCentric();
        intake.update();
        spindexer.update();
        activeIntake.update();

        trapa.update();
        turret.update();
        camera.update();
        shoot.update();
        detection.update();
    }
}
