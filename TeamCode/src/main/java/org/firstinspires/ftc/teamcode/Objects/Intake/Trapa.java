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

    private StateTrapa state, laststate;
    private static final double POS_INTAKE = 0, POS_OUTTAKE = 0;

    public Trapa(RobotHardware robot) {
        servoTrapa = robot.servoTrapa;
        state = StateTrapa.OUTTAKE;
    }

    public void update() {
        if (state != laststate) {
            switch (state) {
                case INTAKE:
                    servoTrapa.setPosition(POS_INTAKE);
                    break;
                case OUTTAKE:
                    servoTrapa.setPosition(POS_OUTTAKE);
                    break;
            }
        }
        laststate = state;


        telemetry.addData("pos", servoTrapa.getPosition());
        telemetry.update();
    }

    public void setState(StateTrapa state) {
        this.state = state;
    }
}
