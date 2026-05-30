package org.firstinspires.ftc.teamcode.Objects.Intake;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;


@Configurable
public class ServoIntake {

    public Servo servoIntake;
    public enum StariServoIntake{
        INIT,
        INTAKE,
        OUTTAKE;
    }

    public static final double POS_INTAKE = 0.35, POS_OUTTAKE = 0.53;

    public StariServoIntake state, laststate;

    public ServoIntake(RobotHardware robot){
        this.servoIntake = robot.servoIntake;
        state = StariServoIntake.OUTTAKE;
    }

    public void update(){
       // servoIntake.setPosition(pos);
        if(state != laststate){
            switch (state){
                case INTAKE:
                    servoIntake.setPosition(POS_INTAKE);
                    break;
                case OUTTAKE:
                    servoIntake.setPosition(POS_OUTTAKE);
                    break;
            }
        }
        laststate = state;
    }
    public void setState(StariServoIntake state){
        this.state = state;
    }
}
