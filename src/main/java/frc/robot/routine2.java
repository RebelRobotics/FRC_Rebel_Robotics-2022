package frc.robot;

import frc.robot.commands.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class routine2 extends SequentialCommandGroup {
     
    public routine2() {
        addRequirements();
        addCommands(
            new goStraight(24, 90),
            new goStraight(24, 0),
            new goStraight(48, -90),
            new goStraight(24, 0),
            new goStraight(24, 90),
            new goToAngle(0, 1)
        );
    }
    
}
