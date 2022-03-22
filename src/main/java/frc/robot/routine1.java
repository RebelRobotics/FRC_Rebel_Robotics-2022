package frc.robot;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class routine1 extends SequentialCommandGroup {
     
    public routine1() {
        addRequirements();
        addCommands(
            new goStraight(36, 30),
            new goStraight(72, -30),
            new goStraight(36, 30),
            new goStraight(124, 180),
            new goToAngle(180, 1, 0.001, 2)
        );
    }
    
}
