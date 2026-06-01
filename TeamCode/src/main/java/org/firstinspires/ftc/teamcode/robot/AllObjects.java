package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Functions.Detection;
import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.ServoIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.basic_functions.Outtake;

public class AllObjects {
    public ActiveIntake activeIntake;
    public ServoIntake servoIntake;
    public Intake intake;
    public Trapa trapa;
    public Chassis chassis;
    public Turret turret;
    public Camera camera;
    public Shoot shoot;
    public Hood hood;
    public Spindexer spindexer;
    private Outtake outtake;

    public Transfer transfer;

    public void init_camera(RobotHardware robot){
        camera = new Camera(robot);
    }

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        activeIntake = new ActiveIntake(robot);
        turret = new Turret(robot);
        trapa = new Trapa(robot);
        spindexer = new Spindexer(robot);
        servoIntake = new ServoIntake(robot);

        shoot = new Shoot(robot);
        hood = new Hood(robot);

        intake = new Intake(activeIntake, trapa, spindexer, servoIntake);
        outtake = new Outtake(turret, shoot, hood, camera);
        transfer = new Transfer(spindexer, trapa, activeIntake, outtake, shoot);
    }

    public void update() {
        chassis.updateFieldCentric();
        activeIntake.update();
        servoIntake.update();

        trapa.update();
        spindexer.update();

        turret.update();
        shoot.update();
        intake.update();
        transfer.update();
        outtake.update();
        camera.update();
    }
}
