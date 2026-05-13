package org.firstinspires.ftc.teamcode.OpModes.teleOP;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@TeleOp(name = "RedTeleOP")
public class RedTeleOP extends OpMode {
    private RobotHardware hardware;
    private AllObjects objects;

    @Override
    public void init() {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        hardware = new RobotHardware();
        hardware.init();

        objects = new AllObjects();
        objects.init(hardware);
    }
    @Override
    public void loop() {
        hardware.update();
        objects.update();
    }
}