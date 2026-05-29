package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.PanelTelemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Objects.Shooter.Hood;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;
import org.firstinspires.ftc.teamcode.Functions.Commands;
@TeleOp(name = "Test")
@Configurable
public class Test extends OpMode {
    private RobotHardware robot;

    private Servo servoHood, servoTrapa;
    private DcMotorEx motor_w_encoder, motor_wtht_encoder;

    public static double hoodPoisiton = 0.11, trapaPosition = 0.45, shooterPower = 0;

    @Override
    public void init() {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);
        alliance = -1;

        robot = new RobotHardware();
        robot.init();

        servoHood = robot.servoHood;
        servoTrapa = robot.servoTrapa;

        motor_w_encoder = robot.motorShooter6;
        motor_wtht_encoder = robot.motorShooter5;

        motor_w_encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor_w_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor_w_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor_w_encoder.setDirection(DcMotorSimple.Direction.FORWARD);

        motor_wtht_encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor_wtht_encoder.setDirection(DcMotorSimple.Direction.REVERSE);
        motor_wtht_encoder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        servoHood.setPosition(hoodPoisiton);
        servoTrapa.setPosition(trapaPosition);

        motor_w_encoder.setPower(shooterPower);
        motor_wtht_encoder.setPower(shooterPower);

        telemetry.addData("ShooterVelocity", motor_w_encoder.getVelocity());
        telemetry.addData("curentMotorEncoder", motor_w_encoder.getCurrent(CurrentUnit.MILLIAMPS));
        telemetry.addData("curentMotorFaraEncoder", motor_wtht_encoder.getCurrent(CurrentUnit.MILLIAMPS));

        robot.update();

    }
}