package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.PanelTelemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.panel;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import android.sax.StartElementListener;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import static androidx.core.math.MathUtils.clamp;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Spindexer {
    private CRServo servospin1, servospin2;
    private DcMotor encoder;
    private AnalogInput position;
    private ElapsedTime timer = new ElapsedTime();
//CU /BATTERY    private static double kp = -0.00247, kd = -0.0003, ks = -0.12;
    private static double kp = -0.00017, kd = -0.0000128, ks = -0.0152, addp = -0.00021, addd = -0.0000188, adds = 0;
    public static double CasianSafe = 200;
    private double error, currentSpeed, power;
    private double currentPosition, lastPosition;
    public static double initPosAnalog = 165;
    private double initPosEncoder, offset;
    public static double K = 22.7555555555556;
    private double targetPosition = 0;
    private double transferSpeed = -14;
    private double batterySpin;
    public static double POS_INTAKE = 0, POS_CHAMBERFRONT = -1300, POS_CHAMBERRIGHT = 20, POS_CHAMBERLEFT = -10;
    public boolean UseKs, FirstFrame, shooting = false, freeSpin, resetBaterry = true;
    public enum StateSpindexer{
        CHAMBERFRONT,
        INTAKE,
        CHAMBERLEFT,
        CHAMBERRIGHT,
        AFTERTRASNFER,
        SHOOTING;
    }
    public enum StateTrans{
        FIRST,
        RESET;
    }
    private StateSpindexer state, laststate;
    private StateSpindexer stateTrans;

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
        freeSpin = false;
        resetBaterry = true;
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
                    shooting = false;
                    resetBaterry = false;
                    kp = -0.00021;
                    ks = -0.011;
                    kd = -0.000012;

                    break;
                case CHAMBERFRONT:
                    targetPosition = POS_CHAMBERFRONT;
                    shooting = false;
                    resetBaterry = true;
                    kp = addp;
                    kd = addd;
                    ks = adds;

                    break;
                case CHAMBERLEFT:
                    targetPosition = POS_CHAMBERLEFT;
                    shooting = false;
                    resetBaterry = true;

                    break;
                case CHAMBERRIGHT:
                    targetPosition = POS_CHAMBERRIGHT;
                    shooting = false;
                    resetBaterry = true;

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

        if(Math.abs(error) >= 2*K) UseKs = true;
        else if (Math.abs(error) <= 1*K) UseKs = false;

        power = kp * error + kd * (-currentSpeed);

        if(UseKs) power = power + Math.signum(error) * ks;

        if(shooting) power = transferSpeed;

        //if(resetBaterry) batterySpin = battery;

        //power = power / batterySpin;

        power = clamp(power, -1, 1);

        if(!freeSpin){
            setSpeed(power);
        }

        lastPosition = currentPosition;
        timer.reset();


        telemetry.addData("Power", power);
        telemetry.addData("TransferPower", transferSpeed);

        PanelTelemetry.addData("SpinPos" , currentPosition);
        PanelTelemetry.addData("TargetPos", targetPosition);
        PanelTelemetry.addData("CurrentSpeed", currentSpeed);
        PanelTelemetry.addData("Error", error);
    }
    public void setSpeed(double power){
        servospin1.setPower(power);
        servospin2.setPower(power);
    }

    public void setState(StateSpindexer state){
        this.state = state;
    }

    public void setTransferSpeed(double x){
        transferSpeed = x;
    }

    /*public boolean SafeForCasian(){
        if(state == StateSpindexer.CHAMBERFRONT && error < CasianSafe) return true;
        return false;
    }*/
}
