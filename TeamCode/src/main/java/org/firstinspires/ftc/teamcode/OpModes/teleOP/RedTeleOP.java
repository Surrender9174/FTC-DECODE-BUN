package org.firstinspires.ftc.teamcode.OpModes.teleOP;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;
import org.firstinspires.ftc.teamcode.Commands;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

@TeleOp(name = "RedTeleOP")
public class RedTeleOP extends OpMode {
    private RobotHardware hardware;
    private AllObjects objects;

    private  Commands commands;


    @Override
    public void init() {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        hardware = new RobotHardware();
        hardware.init();

        objects = new AllObjects();
        objects.init(hardware);

        alliance = -1;
    }
    @Override
    public void start(){
        commands = new Commands();
        commands.init(objects);
    }
    @Override
    public void loop() {
        hardware.update();
        objects.update();
        commands.update();
    }
}