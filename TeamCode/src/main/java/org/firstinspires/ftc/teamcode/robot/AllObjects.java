package org.firstinspires.ftc.teamcode.robot;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Intake.Trapa;
import org.firstinspires.ftc.teamcode.Objects.Turreta.Camera;
import org.firstinspires.ftc.teamcode.Objects.Turreta.Turret;

public class AllObjects {
    public ActiveIntake activeIntake;
    public Intake intake;
    public Trapa trapa;
    public Chassis chassis;
    public Turret turret;
    private Camera camera;
    private Spindexer spindexer;

    public Transfer transfer;

    public void init(RobotHardware robot) {
        chassis = new Chassis(robot);
        activeIntake = new ActiveIntake(robot);
        trapa = new Trapa(robot);
        camera = new Camera(robot);
        turret = new Turret(robot);
        spindexer = new Spindexer(robot);


        intake = new Intake(activeIntake, trapa, spindexer);
        transfer = new Transfer(spindexer, trapa);
    }

    public void update() {
        chassis.updateFieldCentric();
        activeIntake.update();
        trapa.update();
        turret.update();
        camera.update();
        spindexer.update();

        intake.update();
        transfer.update();
    }
}
