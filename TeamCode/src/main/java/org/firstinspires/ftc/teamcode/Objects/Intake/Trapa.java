package org.firstinspires.ftc.teamcode.Objects.Intake;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;
@Configurable
public class Trapa {
    private Servo servoTrapa;

    public enum StateTrapa {
        INIT,
        INTAKE,
        OUTTAKE;
    }

    private StateTrapa state, laststate;
    private static double POS_INTAKE = 0.25, POS_OUTTAKE = 0.5;

    public Trapa(RobotHardware robot) {
        servoTrapa = robot.servoTrapa;

        state = StateTrapa.INTAKE;
    }

    public void update() {
        if (state != laststate) {
            switch (state) {
                case INIT:
                    break;
                case INTAKE:
                    servoTrapa.setPosition(POS_INTAKE);
                    break;
                case OUTTAKE:
                    servoTrapa.setPosition(POS_OUTTAKE);
                    break;
            }
        }

        telemetry.addData("ServoTrapaPos", servoTrapa.getPosition());

        laststate = state;
    }

    public void setState(StateTrapa state) {
        this.state = state;
    }
    public StateTrapa getState(){
        return state;
    }
    }
