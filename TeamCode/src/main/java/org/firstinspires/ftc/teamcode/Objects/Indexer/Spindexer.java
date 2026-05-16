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
    private states state;
    public Spindexer(RobotHardware hardware)
    {
        servo1 = hardware.servoSpindexer1;
        servo2 = hardware.servoSpindexer2;
        sensor = hardware.spindexerPosition;
        encoder = hardware.motorShooter6;

        state = states.Chamber1;
        constantChange = 8192 / 360;
        encoderOffset = degreestotick(sensor.getVoltage() / 3.3 * 2 * 360);
    }
    public void update()
    {
        switch(state)
        {
            case Chamber1:
                targetPos = chamber1;
                rapidfire = false;
                break;
            case Chamber2:
                targetPos = chamber2;
                rapidfire = false;
                break;
            case Chamber3:
                targetPos = chamber3;
                rapidfire = false;
                break;
            case Intake:
                targetPos = intake;
                rapidfire = false;
                break;
        }
        currentPosition = ticktodegrees((encoder.getCurrentPosition() + encoderOffset)%8192);
        telemetry.addData("AnalogPos:", ((sensor.getVoltage() / 3.3 * 360)%180)*2);
        speed = encoder.getVelocity();
        error = targetPos - currentPosition;


        if(error < 3) { useKs = true; }
        else if(error > 5) { useKs = false; }

        if(error < 180) error -= 360;
        else if(error > -180) error += 360;

        power = (-speed) * kD + error * kP;

        if(useKs) { power += Math.signum(error) * kF; }

        setPower(power);

        telemetry.addData("SensorVoltage ", currentReading);
        telemetry.addData("Position", ticktodegrees(currentPosition));
        telemetry.addData("EncoderReading", currentPosition);
    }
    public void setPower(double power)
    {
        servo1.setPower(power);
        servo2.setPower(power);
    }
    public double ticktodegrees(double ticks)
    {
        double degrees = ticks / constantChange;
        return degrees;
    }
    public double degreestotick(double degrees)
    {
        double ticks = degrees * constantChange;
        return ticks;
    }
}
