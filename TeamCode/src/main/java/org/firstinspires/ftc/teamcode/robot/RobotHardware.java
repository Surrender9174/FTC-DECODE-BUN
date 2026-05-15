package org.firstinspires.ftc.teamcode.robot;

import static org.firstinspires.ftc.teamcode.robot.StaticVariables.hardwareMap;
//import static org.firstinspires.ftc.teamcode.Robot.StaticVariables.lastgamepad2;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.battery;
//import static org.firstinspires.ftc.teamcode.Robot.StaticVariables.dashboardTelemetry;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.gamepad;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.gamepad2;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.lastgamepad;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.lastgamepad2;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotH;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotVelocity;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotVelocityAngle;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotX;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.robotY;
import static org.firstinspires.ftc.teamcode.robot.StaticVariables.telemetry;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

import java.util.List;

public class RobotHardware {
    List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

    private LynxModule controlHub;

    public Limelight3A limelight;

    public DigitalChannel pressurePlate;

    public DcMotor motorFrontRight, motorFrontLeft,motorBackRight, motorBackLeft;
    public DcMotor motorIntake;
    public DcMotorEx motorShooter5, motorShooter6, motorTurret;

    public Servo servoHood, servoTrapa;
    public CRServo servoSpindexer1, servoSpindexer2;
    public AnalogInput spindexerPosition;

    public RevColorSensorV3 sensorChamberFront1, sensorChamberFront2, sensorChamberRight1, sensorChamberRight2, sensorChamberLeft1, sensorChamberLeft2;
    public IMU imu;
    public GoBildaPinpointDriver odometry;
    private Pose2D pos;

    private ElapsedTime timer = new ElapsedTime();
    private ElapsedTime speedTimer = new ElapsedTime();
    private ElapsedTime stableTimer = new ElapsedTime();

    private int cnt = 0, lastCnt = 0, error = 0;
    private double speed, lastBattery;

    private double lastRobotH, lastRobotX, lastRobotY;

    private double notready = 0, calibrating = 0, ceva = 0, bun = 0;

    public void init() {
        // HUBS
        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        controlHub = hardwareMap.get(LynxModule.class, "Control Hub");

        //CHASSIS
        motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");

//        imu = hardwareMap.get(IMU.class, "imu");
//
//        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.FORWARD;
//        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
//
//        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
//
//        imu.initialize(new IMU.Parameters(orientationOnRobot));
//        imu.resetYaw();

        // ODOMETRY
        odometry = hardwareMap.get(GoBildaPinpointDriver.class, "odometry");
        odometry.setOffsets(-113 ,-142, DistanceUnit.MM);
        odometry.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odometry.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odometry.resetPosAndIMU();
        odometry.recalibrateIMU();

//        computer = hardwareMap.get(GoBildaPinpointDriver.class, "computer");
//        computer.resetPosAndIMU();
//        computer.recalibrateIMU();

        //INTAKE
        motorIntake = hardwareMap.get(DcMotor.class, "motorIntake");

        pressurePlate = hardwareMap.get(DigitalChannel.class, "pressurePlate");

        //INDEXER
        servoSpindexer1 = hardwareMap.get(CRServo.class, "servoSpindexer1");
        servoSpindexer2 = hardwareMap.get(CRServo.class, "servoSpindexer2");
        servoTrapa = hardwareMap.get(Servo.class, "servoTrapa");

        sensorChamberFront1 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberFront1");
        sensorChamberFront2 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberFront2");
        sensorChamberRight1 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberRight1");
        sensorChamberRight2 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberRight2");
        sensorChamberLeft1 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberLeft1");
        sensorChamberLeft2 = hardwareMap.get(RevColorSensorV3.class, "sensorChamberLeft2");

        spindexerPosition = hardwareMap.get(AnalogInput.class, "1");

        //OUTTAKE
        motorTurret = hardwareMap.get(DcMotorEx.class, "motorTurret");
        motorShooter5 = hardwareMap.get(DcMotorEx.class, "motorShooter5");
        motorShooter6 = hardwareMap.get(DcMotorEx.class, "motorShooter6");

        servoHood = hardwareMap.get(Servo.class, "servoHood");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public void update() {
        //telemetry.addData("Baterie", battery);

        lastgamepad.copy(gamepad);
        lastgamepad2.copy(gamepad2);

        //telemetry.addData("FPS", lastCnt);

        if (timer.seconds() > 1) {
            lastCnt = cnt;
            cnt = 0;
            timer.reset();
        }
        else {
            cnt++;
        }

        battery = controlHub.getInputVoltage(VoltageUnit.VOLTS);
        battery = lastBattery * 0.85 + battery * 0.15; lastBattery = battery;

        odometry.update();

        pos = odometry.getPosition();

        speed = Math.max(Math.abs(robotH - lastRobotH) / speedTimer.seconds(), Math.sqrt(Math.pow(robotX - lastRobotX, 2) + Math.pow(robotY - lastRobotY, 2)) / speedTimer.seconds());

        robotVelocity = Math.sqrt(Math.pow(robotX - lastRobotX, 2) + Math.pow(robotY - lastRobotY, 2)) / speedTimer.seconds();
        robotVelocityAngle = Math.atan2(robotY - lastRobotY, robotX - lastRobotX);

        speedTimer.reset();

        if (speed > 5) stableTimer.reset();

        lastRobotX = robotX; lastRobotY = robotY; lastRobotH = robotH;

        if (!Double.isNaN(pos.getY(DistanceUnit.CM))) robotX = - pos.getY(DistanceUnit.CM);
        if (!Double.isNaN(pos.getX(DistanceUnit.CM))) robotY = pos.getX(DistanceUnit.CM);
        if (!Double.isNaN(pos.getHeading(AngleUnit.DEGREES))) robotH = pos.getHeading(AngleUnit.DEGREES) + 90;

        //telemetry.addData("X", robotX);
        //telemetry.addData("Y", robotY);
        //telemetry.addData("H", robotH);

<<<<<<< HEAD
=======
        //telemetry.update();
>>>>>>> origin/teo
        //dashboardTelemetry.update();


        for (LynxModule hub : allHubs) {
            hub.clearBulkCache();
        }
    }

    public boolean isStable() {
        return (stableTimer.seconds() > 0.3);
    }
}
