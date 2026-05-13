package org.firstinspires.ftc.teamcode.objects.indexer;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.chamber_1;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.chamber_2;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.chamber_3;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.pattern;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.RobotHardware;
public class Spindexer {
    private CRServo servo1, servo2;
    private AnalogInput position;

    public static double Kp = 0.00009, Ki = 0, Kd = 0.000024, Ks = 0.034;

    public enum SpinnerStates {
        CHAMBER_1,
        CHAMBER_2,
        CHAMBER_3,
        INTAKE,
        RAPID_FIRE;
    }

    private SpinnerStates state, lastState;

    public static double POSITION_CHAMBER_1 = 268;
    private final double POSITION_CHAMBER_2 = 20;
    private final double POSITION_CHAMBER_3 = 140;
    public static double POSITION_INTAKE = 255;

    private double currentPosition, lastPosition, lastTargetPosition, error, power, integralSum, speed;
    private boolean useKs, rapidFire = false;

    private double rapidFireSpeed;

    private boolean freeRotation = false;

    public static double targetPosition = 0;
    private ElapsedTime timer = new ElapsedTime();

    public Spindexer(RobotHardware robot) {
        servo1 = robot.servoSpindexer1;
        servo2 = robot.servoSpindexer2;
        position = robot.spindexerPosition;

        state = SpinnerStates.INTAKE;
        targetPosition = POSITION_INTAKE;
    }

    public void update() {
        if (state != lastState) {
            switch (state) {
                case CHAMBER_1:
                    targetPosition = POSITION_CHAMBER_1;
                    rapidFire = false;

                    break;

                case CHAMBER_2:
                    targetPosition = POSITION_CHAMBER_2;
                    rapidFire = false;

                    break;

                case CHAMBER_3:
                    targetPosition = POSITION_CHAMBER_3;
                    rapidFire = false;

                    break;

                case INTAKE:
                    targetPosition = POSITION_INTAKE;
                    rapidFire = false;

                    break;

                case RAPID_FIRE:
                    rapidFire = true;

                    break;

            }
        }

        lastState = state;

        currentPosition = position.getVoltage() / 3.22 * 360 * 2;

        if (currentPosition >= 360) currentPosition = currentPosition - 360;

        currentPosition = 360 - currentPosition;

        if (targetPosition != lastTargetPosition) {
            integralSum = 0;
        }

        error = targetPosition - currentPosition;

        if (error > 180) error = error - 360;
        if (error < -180) error = error + 360;

        if (error < -50 && !freeRotation) error = error + 360;

        integralSum = integralSum + error * timer.seconds();

        speed = currentPosition - lastPosition;

        if (speed > 180) speed = speed - 360;
        if (speed < -180) speed = speed + 360;

        speed = speed / timer.seconds();

        if (Math.abs(error) < 1) useKs = false;
        if (Math.abs(error) > 3) useKs = true;

        power = Kp * error + Ki * integralSum + Kd * (-speed);

        if (useKs) power = power + Ks * Math.signum(error);

        if (rapidFire) power = rapidFireSpeed;

        setPower(power);

        lastPosition = currentPosition;
        lastTargetPosition = targetPosition;
        timer.reset();

        telemetry.addData("position", position);

    }

    private void setPower(double power) {
        servo1.setPower(power);
        servo2.setPower(power);
    }

    public void goToNextChamber() {
        switch (state) {
            case CHAMBER_1:
                state = SpinnerStates.CHAMBER_2;

                break;

            case CHAMBER_2:
                state = SpinnerStates.CHAMBER_3;

                break;

            case CHAMBER_3:
                state = SpinnerStates.CHAMBER_1;

                break;
        }
    }

    public boolean targetReached() {
        return (Math.abs(targetPosition - currentPosition) < 10);
    }
    public void setSpeed(double speed) {
        rapidFireSpeed = speed;
    }

    public void setState(SpinnerStates state) {
        this.state = state;

        if (this.state != lastState) {
            switch (this.state) {
                case CHAMBER_1:
                    targetPosition = POSITION_CHAMBER_1;
                    rapidFire = false;

                    break;

                case CHAMBER_2:
                    targetPosition = POSITION_CHAMBER_2;
                    rapidFire = false;

                    break;

                case CHAMBER_3:
                    targetPosition = POSITION_CHAMBER_3;
                    rapidFire = false;

                    break;

                case INTAKE:
                    targetPosition = POSITION_INTAKE;
                    rapidFire = false;

                    break;

                case RAPID_FIRE:
                    rapidFire = true;

                    break;

            }
        }

        lastState = this.state;
    }

    public void setPattern() {
        switch (pattern) {
            case 1:
                if (chamber_1 == 2) setState(SpinnerStates.CHAMBER_1);
                else if (chamber_2 == 2) setState(SpinnerStates.CHAMBER_2);
                else if (chamber_3 == 2) setState(SpinnerStates.CHAMBER_3);
                else setState(SpinnerStates.CHAMBER_1);

                break;

            case 2:
                if (chamber_1 == 2) setState(SpinnerStates.CHAMBER_2);
                else if (chamber_2 == 2) setState(SpinnerStates.CHAMBER_3);
                else if (chamber_3 == 2) setState(SpinnerStates.CHAMBER_1);
                else setState(SpinnerStates.CHAMBER_1);

                break;

            case 3:
                if (chamber_1 == 2) setState(SpinnerStates.CHAMBER_3);
                else if (chamber_2 == 2) setState(SpinnerStates.CHAMBER_1);
                else if (chamber_3 == 2) setState(SpinnerStates.CHAMBER_2);
                else setState(SpinnerStates.CHAMBER_1);

                break;

            default:
                state = SpinnerStates.CHAMBER_1;

                break;
        }
    }

    public SpinnerStates getState() {
        return state;
    }
    public void enableFreeRotation() {
        freeRotation = true;
    }

    public void disableFreeRotation() {
        freeRotation = false;
    }

}
