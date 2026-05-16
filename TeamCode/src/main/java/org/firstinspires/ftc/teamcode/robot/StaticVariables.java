package org.firstinspires.ftc.teamcode.robot;

//import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class StaticVariables {
    public static HardwareMap hardwareMap;
    public static Telemetry telemetry;
    public static Gamepad gamepad, lastgamepad = new Gamepad(), gamepad2, lastgamepad2 = new Gamepad();
    public static double robotX, robotY, robotH;

    public static double battery;

    public static int chamber_1, chamber_2, chamber_3, nr_artefacts, alliance;
    public static double goalX, goalY, goalHeight;

    public static double robotVelocity, robotVelocityAngle;

    public static int pattern;
    public static boolean detect;

    //public static FtcDashboard dashboard = FtcDashboard.getInstance();
    //public static Telemetry dashboardTelemetry = dashboard.getTelemetry();

    public static void init(HardwareMap hm, Telemetry tm, Gamepad gm, Gamepad gm2) {
        hardwareMap = hm;
        telemetry = tm;
        gamepad = gm;
        gamepad2 = gm2;
        robotX = 0; robotY = 0; robotH = 90;
        chamber_1 = 0; chamber_2 = 0; chamber_3 = 0; nr_artefacts = 0;
        battery = 14;
        goalX = 0; goalY = 0; goalHeight = 0;
        robotVelocity = 0; robotVelocityAngle = 0;
        pattern = 0;
    }
}
