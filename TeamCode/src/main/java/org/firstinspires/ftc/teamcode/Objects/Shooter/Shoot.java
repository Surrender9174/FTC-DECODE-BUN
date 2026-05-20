package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static com.pedropathing.math.MathFunctions.clamp;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.PanelTelemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Shoot {
    public DcMotorEx motor_w_encoder;
    public DcMotorEx motor_wtht_encoder;

    private static double Kp = 0.06, Kv = 0.0057;
    private double error, power, currentSpeed, lastSpeed, lastPower;
    public static double targetSpeed = 0;
    private double maxVelocity = 2100;
    private boolean useKs;

    public Shoot(RobotHardware robot){
        motor_w_encoder = robot.motorShooter5;
        motor_wtht_encoder = robot.motorShooter6;

        motor_w_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_w_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor_w_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor_w_encoder.setDirection(DcMotorSimple.Direction.REVERSE);

        motor_wtht_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor_wtht_encoder.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_wtht_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lastSpeed = 0;
    }

    public void update(){
        currentSpeed = motor_w_encoder.getVelocity();

        //currentSpeed = 0.8 * lastSpeed + 0.2 * currentSpeed;

        error = targetSpeed - currentSpeed;

        power = Kp * error + Kv * targetSpeed;

        power = clamp(power / battery, -1, 1);

        if (currentSpeed - targetSpeed >= 80 && power >= 0) power = -0.01;

        if (Math.abs(power - lastPower) > 0.02)
            setPowers(power);

        lastPower = power;
        //lastSpeed = currentSpeed;

        PanelTelemetry.addData("ShooterPower", power);
        PanelTelemetry.addData("CurrentVelocity", currentSpeed);
        PanelTelemetry.addData("TargetVelocity", targetSpeed);
    }

    private void setPowers(double power){
        motor_wtht_encoder.setPower(power);
        motor_w_encoder.setPower(power);
    }

    public void setTargetSpeed(double targetSpeed){
        targetSpeed = clamp(targetSpeed, 0, maxVelocity);

        this.targetSpeed = targetSpeed;
    }

    public double getSpeedDifference(){
        return targetSpeed - currentSpeed;
    }
}
