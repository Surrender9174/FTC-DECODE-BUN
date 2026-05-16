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
    private static double kp = -0.0026, kd = -0.00028, ks = -0.15;
    private double error, currentSpeed, power;
    private double currentPosition, lastPosition;

    private double initPosAnalog = 150;
    private double initPosEncoder, offset;
    public static double K = 22.7555555555556;
    private double targetPosition = 0;
    public static final double POS_INTAKE = 5*K, POS_CHAMBERFRONT = -45*K, POS_CHAMBERRIGHT = 20, POS_CHAMBERLEFT = -10;
    public boolean UseKs, FirstFrame, shooting = false;
    public enum StateSpindexer{
        CHAMBERFRONT,
        INTAKE,
        CHAMBERLEFT,
        CHAMBERRIGHT,
        SHOOTING;
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
        targetPosition = POS_INTAKE;
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
       if(state != laststate){
            switch (state){
                case INTAKE:
                    targetPosition = POS_INTAKE;

                    break;
                case CHAMBERFRONT:
                    targetPosition = POS_CHAMBERFRONT;

                    break;
                case CHAMBERLEFT:
                    targetPosition = POS_CHAMBERLEFT;

                    break;
                case CHAMBERRIGHT:
                    targetPosition = POS_CHAMBERRIGHT;

                    break;
                case SHOOTING:
                    shooting = true;

                    break;
            }

        }
        laststate = state;

        currentPosition = (encoder.getCurrentPosition() + offset) % 8192;

        error =  targetPosition - currentPosition;

        if(currentPosition > 4096){
            currentPosition = -4096 + (currentPosition % 4096);
        }
        if(currentPosition < -4096){
            currentPosition = 4096 + (currentPosition % 4096);
        }

        if (error > 180 * K) error = error - 360 * K;
        if (error < -180 * K) error = error + 360 * K;

        currentSpeed = (currentPosition - lastPosition) / timer.seconds();

        if(Math.abs(error) >= 3*K) UseKs = true;
        else if (Math.abs(error) <= 1*K) UseKs = false;

        power = kp * error + kd * (-currentSpeed);

        if(UseKs) power = power + Math.signum(error) * ks;

        if(shooting)
            power = -14;

        setSpeed(power/battery);


        telemetry.addData("Power", power);
        telemetry.addData("Error", error);
        telemetry.addData("CurrentSpeed", currentSpeed);

        lastPosition = currentPosition;
        timer.reset();

        telemetry.addData("CurrentPosition", (currentPosition));
    }
    public void setSpeed(double power){
        servospin1.setPower(power);
        servospin2.setPower(power);
    }

    public void setState(StateSpindexer state){
        this.state = state;
    }

    public void disableShooting(){
        shooting = false;
    }
}
