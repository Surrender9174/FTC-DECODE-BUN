package org.firstinspires.ftc.teamcode.robot;

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
    private Shoot shoot;
    private Hood hood;
    private Spindexer spindexer;
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
        transfer = new Transfer(spindexer, trapa, activeIntake);
        outtake = new Outtake(turret, shoot, hood, spindexer, trapa, camera);
    }

    public void update() {
        chassis.updateFieldCentric();
        //intake.update();
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
