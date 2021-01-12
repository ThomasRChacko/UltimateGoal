package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.PerpetualCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.commands.RunCommand;
import org.firstinspires.ftc.teamcode.commands.drive.DefaultDriveCommand;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 *
 * NOTE: this has been refactored to use FTCLib's command-based
 */
@Config
@Disabled
@TeleOp(group = "drive")
public class LocalizationTest extends CommandOpMode {

    private Drivetrain drive;
    private DefaultDriveCommand driveCommand;
    private GamepadEx gamepad;

    @Override
    public void initialize() {
        drive = new Drivetrain(new SampleTankDrive(hardwareMap), telemetry);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gamepad = new GamepadEx(gamepad1);

        schedule(new WaitUntilCommand(this::isStarted).andThen(new RunCommand(
                () -> {
                    drive.setWeightedDrivePower(
                            new Pose2d(
                                    gamepad.getLeftY(),
                                    0,
                                    -gamepad.getRightX()
                            )
                    );
                    drive.update();
                    Pose2d poseEstimate = drive.getPoseEstimate();
                    telemetry.addData("x", poseEstimate.getX());
                    telemetry.addData("y", poseEstimate.getY());
                    telemetry.addData("heading", poseEstimate.getHeading());
                    telemetry.update();
                    
                }, drive // ignore requirements
        )));
    }

}