package org.firstinspires.ftc.teamcode.Objects.Indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Spindexer {
    private CRServo servo1, servo2;
    private AnalogInput sensor;
    private DcMotorEx encoder;
    public static double kP=0, kI=0, kD=0, kF=0;
    public static double chamber1=0, chamber2=0, chamber3=0, intake=0;
    private double currentPosition, targetPos, power;
    private double speed, error;
    private double currentReading, encoderOffset;
    private boolean rapidfire, useKs;
    public double constantChange;
    public enum states
    {
        Chamber1,
        Chamber2,
        Chamber3,
        Intake,
        RapidFire;
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
