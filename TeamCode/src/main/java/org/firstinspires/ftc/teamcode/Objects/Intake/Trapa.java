package org.firstinspires.ftc.teamcode.Objects.Intake;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;
public class Trapa {
    private Servo servoTrapa;

    public enum StateTrapa {
        INTAKE,
        OUTTAKE;
    }
    private static final double POS_INTAKE = 0, POS_OUTTAKE = 0;

    public Trapa(RobotHardware robot){
        servoTrapa = robot.servoTrapa;
    }
    public void update(){

        telemetry.addData("pos", servoTrapa.getPosition());
        telemetry.update();
    }
}
