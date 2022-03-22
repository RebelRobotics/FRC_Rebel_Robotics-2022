package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.robotMap;
import frc.robot.commands.SUBSYS.spinShooter;
import frc.robot.commands.SUBSYS.spinIntake;

public class visionShoot extends SequentialCommandGroup {
    public visionShoot(spinShooter spinShooter, spinIntake spinIntake) {
        addRequirements();
        addCommands(
            new goToAngle(robotMap.jevois.getAngle(), 1, 0.001, 2),
            new InstantCommand(spinShooter::spin, spinShooter),
            new WaitCommand(0.5),
            new InstantCommand(spinIntake::spin, spinIntake),
            new WaitCommand(2),
            new InstantCommand(spinShooter::stop, spinShooter)
        );
    }
}
