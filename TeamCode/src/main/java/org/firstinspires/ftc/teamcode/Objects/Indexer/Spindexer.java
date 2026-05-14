package org.firstinspires.ftc.teamcode.Objects.Indexer;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Spindexer {
    private CRServo servospin1, servospin2;
    private AnalogInput position;
    private double kp, ki, kd, ks;
    private double targetPositon, currentPosition;
    public enum StateSpindexer{
        CHAMBERFRONT,
        INTAKE,
        CHAMBERLEFT,
        CHAMBERRIGHT;
    }

    private StateSpindexer state, laststate;

    public void init(RobotHardware robot){
        servospin1 = robot.servoSpindexer1;
        servospin2 = robot.servoSpindexer2;

    }
    public void update(){
        if(state != laststate){
            switch (state){
                case INTAKE:
                    targetPositon = 0;

                    break;
                case CHAMBERFRONT:
                    targetPositon = 10;

                    break;
                case CHAMBERLEFT:
                    targetPositon = 50;

                    break;
                case CHAMBERRIGHT:
                    targetPositon = -20;

                    break;
            }

        }
        laststate = state;

        current
    }
}
