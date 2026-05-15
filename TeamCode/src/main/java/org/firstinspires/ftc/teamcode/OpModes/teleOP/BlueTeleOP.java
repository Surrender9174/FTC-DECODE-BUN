package org.firstinspires.ftc.teamcode.OpModes.teleOP;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;
import org.firstinspires.ftc.teamcode.Functions.Commands;
@TeleOp(name = "BlueTeleOP")
public class BlueTeleOP extends OpMode {
    private RobotHardware robot;
    private AllObjects objects;

    private  Commands commands;
    @Override
    public void init() {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
    }
    @Override
    public void start(){
        objects.init(robot);

        commands = new Commands();
        commands.init(objects);
    }
    @Override
    public void loop() {
        robot.update();
        objects.update();
        commands.update();
    }
}