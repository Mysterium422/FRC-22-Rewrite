package frc.robot.commands.auto;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.DriveStraight;
import frc.robot.commands.RunFlyWheel;
import frc.robot.commands.SetIntakeExtended;
import frc.robot.commands.operatorcommands.TeleFeed;
import frc.robot.commands.operatorcommands.TeleIntake;
import frc.robot.subsystems.drivetrain.DriveBase;
import frc.robot.subsystems.flywheel.VarFlyWheel;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.transversal.Transversal;
import frc.robot.subsystems.uptake.Uptake;

public class TwoCargoAuto extends ParallelCommandGroup {
    double DRIVE_SPEED = 0.3;

    public TwoCargoAuto(DriveBase drive, Intake intake, Transversal transversal, Uptake uptake,
            VarFlyWheel flywheel) {
        addCommands(
                // Prepare Intake
                sequence(
                        // Extend intake
                        new SetIntakeExtended(intake, true),

                        // Wait for intake extend
                        new WaitCommand(0.2),

                        // Run intake
                        new TeleIntake(intake, () -> -7.0)),

                // Start shooter
                new RunFlyWheel(flywheel, Constants.SHOOTER_UP_RPM, true),

                // Auto Drive Sequence
                sequence(
                        // Drive to pick up cargo
                        new DriveStraight(drive, Units.feetToMeters(3), DRIVE_SPEED),

                        // Align to hub using vision
                        // new PositionForHub(vision, drive).withTimeout(3),

                        // Shoot cargo
                        new TeleFeed(transversal, uptake, () -> 9.0).withTimeout(5),

                        // Back up a little further out of the tarmac
                        new DriveStraight(drive, Units.feetToMeters(2), DRIVE_SPEED)));
    }
}
