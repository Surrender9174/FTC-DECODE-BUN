package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;
import org.firstinspires.ftc.teamcode.Functions.Commands;
@TeleOp(name = "RedTeleOP")
public class RedTeleOP extends OpMode {
    private RobotHardware robot;
    private AllObjects objects;

    private  Commands commands;
    @Override
    public void init() {
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);
        alliance = -1;

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
        objects.init_camera(robot);
    }
    @Override
    public void start(){
        objects.init(robot);

        commands = new Commands();
        commands.init(objects, robot);
    }
    @Override
    public void loop() {
        commands.update();
        robot.update();
        objects.update();
    }
}