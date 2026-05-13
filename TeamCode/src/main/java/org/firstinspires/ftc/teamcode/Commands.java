package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.gamepad;

import org.firstinspires.ftc.teamcode.Objects.Drivetrain.Chassis;
import org.firstinspires.ftc.teamcode.Objects.Intake.ActiveIntake;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Commands {
    private Chassis chassis;
    private ActiveIntake intake;
    public void init(AllObjects objects){
        chassis = objects.chassis;
        intake = objects.intake;
    }
    public void update()
    {
        chassis.setMovement(gamepad.left_stick_x, -gamepad.left_stick_y, -gamepad.right_stick_x);
        if(gamepad.right_trigger > 0.1) intake.setState(ActiveIntake.ActiveIntakeStates.INTAKE);
        else if(gamepad.left_trigger > 0.1) intake.setState(ActiveIntake.ActiveIntakeStates.OUTTAKE);
        else intake.setState(ActiveIntake.ActiveIntakeStates.INIT);
    }
}