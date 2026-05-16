package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;

public class AllObjects {
    public ActiveIntake activeIntake;
    public Intake intake;
    public Trapa trapa;
    public Chassis chassis;
    public Turret turret;
    public Camera camera;
    private Shoot shoot;
    private Hood hood;
    private Spindexer spindexer;
    //private Outtake outtake;

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

        shoot = new Shoot(robot);
        hood = new Hood(robot);
        camera = new Camera(robot);

        intake = new Intake(activeIntake, trapa, spindexer);
        transfer = new Transfer(spindexer, trapa);
        //outtake = new Outtake(shoot, turret, camera, hood);
    }

    public void update() {
        chassis.updateFieldCentric();
        intake.update();
        activeIntake.update();

        trapa.update();
        spindexer.update();

        shoot.update();
        hood.update();
        intake.update();
        transfer.update();
        //outtake.update();
    }
}
