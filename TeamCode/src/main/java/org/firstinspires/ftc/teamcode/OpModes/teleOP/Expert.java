package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.alliance;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;
import org.firstinspires.ftc.teamcode.Functions.Commands;
@TeleOp
@Configurable
public class Expert extends OpMode {
    private RobotHardware robot;
    private AllObjects objects;

    private  Commands commands;
    public static double addX = -10, addY = 15;
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

        //commands.detection.setCoeffs(5, 15);
        commands.detection.setGoalOffsets(addX, addY);
    }
    @Override
    public void loop() {
        commands.update();
        objects.update();
        robot.update();

    }
}