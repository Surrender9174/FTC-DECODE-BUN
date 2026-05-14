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
    private static double kp, ki, kd, ks;
    private double error, currentSpeed, power;
    private double currentPosition, lastTargetPosition;

    private static double targetPosition = 0;
    public static final double POS_INTAKE = 0, POS_CHAMBERFRONT = 10, POS_CHAMBERRIGHT = 20, POS_CHAMBERLEFT = -10;
    public boolean UseKs;
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
        //targetPositon = POS_INTAKE;
        UseKs = false;

    }
    public void update(){
       /* if(state != laststate){
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
        laststate = state;*/

        currentPosition = (position.getVoltage() / 3.214 * 360) % 180 * 2;

        if (currentPosition >= 360) currentPosition = currentPosition - 360;

        currentPosition = 360 - currentPosition;

        error = targetPosition - currentPosition;

        if (error > 180) error = error - 360;
        if (error < -180) error = error + 360;

        currentSpeed = targetPosition - currentSpeed;

        if (currentSpeed > 180) currentSpeed = currentSpeed - 360;
        if (currentSpeed < -180) currentSpeed = currentSpeed + 360;

        currentSpeed = currentSpeed / timer.seconds();

        power = kp * error + (-currentSpeed) * kd;

        if(error >= 4) UseKs = true;
        else if (error <= 3) UseKs = false;

        if(UseKs) power = power + ks;

        setSpeed(power);

        telemetry.addData("Analog", currentPosition);
        telemetry.update();
    }
    public void setSpeed(double power){
        servospin1.setPower(power);
        servospin2.setPower(power);
    }
}
