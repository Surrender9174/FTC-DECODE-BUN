package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.panels.Panels;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@TeleOp(name = "SpindexerTest")
public class SpindexerCalibration extends OpMode {
    private RobotHardware robot;
    private Spindexer spindexer;
    @Override
    public void init()
    {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        spindexer = new Spindexer(robot);
    }
    @Override
    public void loop()
    {
        robot.update();
        spindexer.update();
        telemetry.update();
    }
}
