package org.firstinspires.ftc.teamcode.Functions;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.gamepad;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Intake.Intake;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Camera;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Turret;
import org.firstinspires.ftc.teamcode.robot.AllObjects;

public class Commands {
    private Chassis chassis;
    private Intake intake;
    private Camera camera;
    private Turret turret;
    private Detection detection;

    public void init(AllObjects objects){
        chassis = objects.chassis;
        intake = objects.intake;
        detection = new Detection(objects);
    }
    public void update()
    {
        chassis.setMovement(gamepad.left_stick_x, -gamepad.left_stick_y, -gamepad.right_stick_x);
        if(gamepad.right_trigger > 0.1) intake.setState(Intake.StateIntake.INTAKE);
        else if(gamepad.left_trigger > 0.1) intake.setState(Intake.StateIntake.OUTTAKE);
        else intake.setState(Intake.StateIntake.INIT);

    }
    detection.update();
}