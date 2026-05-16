package org.firstinspires.ftc.teamcode.Objects.Shooter;

import static com.pedropathing.math.MathFunctions.clamp;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;

@Configurable
public class Shoot {
    public DcMotorEx motor_w_encoder;
    public DcMotorEx motor_wtht_encoder;

    private double Kp = 0.033, Ks = 0.0051;
    private double error, power, currentSpeed, targetSpeed;
    private static double maxVelocity = 2100;
    private boolean useKs;

    public Shoot(RobotHardware robot){
        motor_w_encoder = robot.motorShooter5;
        motor_wtht_encoder = robot.motorShooter6;

        motor_w_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_w_encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor_w_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor_w_encoder.setDirection(DcMotorSimple.Direction.REVERSE);

        motor_wtht_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor_wtht_encoder.setDirection(DcMotorSimple.Direction.FORWARD);
        motor_wtht_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void update(){
        currentSpeed = motor_w_encoder.getVelocity();
        error = targetSpeed - currentSpeed;
        power = Kp * error + Ks * targetSpeed;
        power = clamp(power / battery, -1, 1);
        if (currentSpeed - targetSpeed >= 80 && power >= 0) power = -0.01;

        setPowers(power);
    }

    private void setPowers(double power){
        motor_wtht_encoder.setPower(power);
        motor_w_encoder.setPower(power);
    }

    private void setTargetSpeed(double targetSpeed){
        this.targetSpeed = targetSpeed;
    }
}
