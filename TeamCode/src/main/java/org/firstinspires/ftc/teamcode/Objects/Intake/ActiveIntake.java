package org.firstinspires.ftc.teamcode.Objects.Intake;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;

public class ActiveIntake {
    private DcMotor motor;

    public enum ActiveIntakeStates {
        INIT,
        INTAKE,
        OUTTAKE;
    }

    private ActiveIntakeStates state, lastState;

    private final double INTAKE_POWER = 1;
    private final double OUTTAKE_POWER = -1;

    public ActiveIntake(RobotHardware robot) {
        motor = robot.motorIntake;

        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update() {
        if (state != lastState) {
            switch (state) {
                case INIT:
                    motor.setPower(0);

                    break;

                case INTAKE:
                    motor.setPower(INTAKE_POWER);

                    break;

                case OUTTAKE:
                    motor.setPower(OUTTAKE_POWER);

                    break;
            }
        }

        lastState = state;
    }

    public void setState (ActiveIntakeStates state) {
        this.state = state;
    }

    public ActiveIntakeStates getState() {
        return state;
    }
}
