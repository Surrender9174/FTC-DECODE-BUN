package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import android.sax.StartElementListener;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Spindexer {
    private CRServo servospin1, servospin2;
    private DcMotor encoder;
    private AnalogInput position;
    private ElapsedTime timer = new ElapsedTime();
    private static double kp = 0, kd = 0, ks = 0;
    private double error, currentSpeed, power;
    private double currentPosition, lastPosition;

    private double initPosAnalog = 150;
    private double initPosEncoder, offset;

    private double targetPosition = 0;
    public static final double POS_INTAKE = 2, POS_CHAMBERFRONT = 10, POS_CHAMBERRIGHT = 20, POS_CHAMBERLEFT = -10;
    public boolean UseKs, FirstFrame = true;
    public static double K = 22.7555555555556;
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
        encoder = robot.motorShooter6;

        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        state = StateSpindexer.INTAKE;
        //targetPositon = POS_INTAKE;
        UseKs = false;

        FirstFrame = true;
        setSpeed(0);

        //initPosEncoder = (position.getVoltage() / 3.214 * 360) % 180 * 2;
        //offset = (initPosEncoder - initPosAnalog) * K;


    }
    public void update(){
        if(FirstFrame){
            initPosEncoder = (position.getVoltage() / 3.214 * 360) % 180 * 2;
            offset = (initPosEncoder - initPosAnalog) * K;

            FirstFrame = false;
        }
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

        currentPosition = (encoder.getCurrentPosition() + offset) % 8192;

        error = targetPosition - currentPosition;

        if (error > 180 * K) error = error - 360 * K;
        if (error < -180 * K) error = error + 360 * K;

        currentSpeed = (currentPosition - lastPosition) / timer.seconds();

        power = kp * error + (-currentSpeed) * kd;

        if(error >= 4) UseKs = true;
        else if (error <= 3) UseKs = false;

        if(UseKs) power = power + ks;

        setSpeed(power / battery);


        lastPosition = currentPosition;
        timer.reset();

        telemetry.addData("CurrentPosition", (currentPosition / K));
    }
    public void setSpeed(double power){
        servospin1.setPower(power);
        servospin2.setPower(power);
    }

    public void setTargetPosition(double target){
        this.targetPosition = target * K;
    }
}
