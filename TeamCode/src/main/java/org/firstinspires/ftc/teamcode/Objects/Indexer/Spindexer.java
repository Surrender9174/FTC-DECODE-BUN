package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import android.sax.StartElementListener;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class Spindexer {
    private CRServo servospin1, servospin2;
    private AnalogInput position;
    private ElapsedTime timer = new ElapsedTime();
    private double kp, ki, kd, ks;
    private double error, currentSpeed;
    private double targetPositon, currentPosition, lastTargetPosition;

    public static final double POS_INTAKE = 0, POS_CHAMBERFRONT = 10, POS_CHAMBERRIGHT = 20, POS_CHAMBERLEFT = -10;
    public enum StateSpindexer{
        CHAMBERFRONT,
        INTAKE,
        CHAMBERLEFT,
        CHAMBERRIGHT;
    }

    private StateSpindexer state, laststate;

    public Spindexer(RobotHardware robot){
        servospin1 = robot.servoSpindexer1;
        servospin2 = robot.servoSpindexer2;

        position = robot.spindexerPosition;

        state = StateSpindexer.INTAKE;
        targetPositon = POS_INTAKE;

    }
    public void update(){
        if(state != laststate){
            switch (state){
                case INTAKE:
                    targetPositon = POS_INTAKE;

                    break;
                case CHAMBERFRONT:
                    targetPositon = POS_CHAMBERFRONT;

                    break;
                case CHAMBERLEFT:
                    targetPositon = POS_CHAMBERLEFT;

                    break;
                case CHAMBERRIGHT:
                    targetPositon = POS_CHAMBERRIGHT;

                    break;
            }

        }
        laststate = state;

        currentPosition = position.getVoltage() / 3.22 * 360 * 2;

        if (currentPosition >= 360) currentPosition = currentPosition - 360;

        currentPosition = 360 - currentPosition;

        error = targetPositon - currentPosition;

        if (error > 180) error = error - 360;
        if (error < -180) error = error + 360;

        currentSpeed = targetPositon - currentSpeed;

        if (currentSpeed > 180) currentSpeed = currentSpeed - 360;
        if (currentSpeed < -180) currentSpeed = currentSpeed + 360;

        servospin1.setPower(0.1);
        servospin2.setPower(0.1);

        //telemetry.addData("Analog", position.getVoltage());

        //telemetry.update();
    }
}
