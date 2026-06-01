package org.firstinspires.ftc.teamcode.Functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.gamepad;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.lastgamepad;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Indexer.Transfer;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Shoot;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.Functions.Detection;

public class Commands {
    private RobotHardware robot;
    private Chassis chassis;
    private Intake intake;
    private Transfer transfer;
    public Detection detection;
    private Turret turret;
    private Camera camera;
    private Shoot shoot;
    private Hood hood;


    public void init(AllObjects objects, RobotHardware robot){
        camera = objects.camera;
        chassis = objects.chassis;
        intake = objects.intake;
        transfer = objects.transfer;
        detection = new Detection(objects);
        turret = objects.turret;
        this.robot = robot;
    }
    public void update()
    {
        chassis.setMovement(gamepad.left_stick_x, -gamepad.left_stick_y, -gamepad.right_stick_x);
        if(gamepad.right_trigger > 0.1) intake.setState(Intake.StateIntake.INTAKE);
        else if(gamepad.left_trigger > 0.1) intake.setState(Intake.StateIntake.OUTTAKE);
        else intake.setState(Intake.StateIntake.INIT);

        if(robot.isStable() && turret.isStable()) detection.initiateDetection();

        if(gamepad.square && !lastgamepad.square) transfer.switchReverse();

        if(gamepad.right_bumper/* && transfer.CasianSafeProff()*/) transfer.setState(Transfer.StateTransfer.INIT);

        if(gamepad.options && !lastgamepad.options){
            robot.odometry.resetPosAndIMU();

            camera.resetDetection();

            turret.setTargetPosition(0);

            //turret.useUsePID();
        }

        if(gamepad.triangle && !lastgamepad.triangle){
            turret.dontUsePID();
        }

        if (gamepad.dpad_left && !lastgamepad.dpad_left) {
            turret.setPower(0.15);
        }

        if (!gamepad.dpad_left && lastgamepad.dpad_left) {
            turret.setPower(0);
        }

        if (gamepad.dpad_right && !lastgamepad.dpad_right) {
            turret.setPower(-0.15);
        }

        if (!gamepad.dpad_right && lastgamepad.dpad_right) {
            turret.setPower(0);
        }

        turret.update();
        detection.update();
        //transfer.update();
    }
}
