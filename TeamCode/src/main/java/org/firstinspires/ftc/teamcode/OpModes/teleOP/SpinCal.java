package org.firstinspires.ftc.teamcode.OpModes.teleOP;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Objects.Indexer.Spindexer;
import org.firstinspires.ftc.teamcode.robot.AllObjects;
import org.firstinspires.ftc.teamcode.robot.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.StaticVariables;

@TeleOp
@Configurable
public class SpinCal extends OpMode {
    public RobotHardware robot;
    public AllObjects objects;
    public Spindexer spindexer;

    public static boolean mode = true;
    public void init(){
        StaticVariables.init(hardwareMap, telemetry, gamepad1, gamepad2);

        robot = new RobotHardware();
        robot.init();

        objects = new AllObjects();
        objects.init(robot);

        spindexer = objects.spindexer;
    }
    public void loop(){
        if(mode) spindexer.setState(Spindexer.StateSpindexer.INTAKE);
        else spindexer.setState(Spindexer.StateSpindexer.CHAMBERFRONT);
        spindexer.update();
    }
}
