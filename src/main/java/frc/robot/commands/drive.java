package frc.robot.commands;

import frc.robot.robotMap;
import frc.robot.CONST;
import com.ctre.phoenix.motorcontrol.ControlMode;


public class drive {
    static double power = robotMap.joystick.getRawAxis(1);
    static double turn = robotMap.joystick.getRawAxis(2);
    static double slider = robotMap.joystick.getRawAxis(3);
    public static double slider2 = robotMap.joystick2.getRawAxis(3);
    static double motor_power = (slider+1)/2;
    static double turn_power = (slider2+1)/2;

    public static void driveMethod() {
        power = robotMap.joystick.getRawAxis(1);
            if (Math.abs(power) < CONST.DEADZONE) { power = 0; }
        turn = robotMap.joystick.getRawAxis(2);
            if (Math.abs(turn) < CONST.DEADZONE) { turn = 0.0; }
        slider = robotMap.joystick.getRawAxis(3);
        slider2 = robotMap.joystick2.getRawAxis(3);
        motor_power = (slider+1)/2;
        turn_power = (slider2+1)/2;

        robotMap.LDrive1.set(ControlMode.PercentOutput, (power+(turn*turn_power))*motor_power);
        robotMap.RDrive2.set(ControlMode.PercentOutput, (power-(turn*turn_power))*motor_power);
        if (robotMap.joystick.getRawButton(1)) {
            robotMap.LDrive1.setSelectedSensorPosition(0);
            robotMap.RDrive2.setSelectedSensorPosition(0);
            robotMap.LDrive3.setSelectedSensorPosition(0);
            robotMap.RDrive4.setSelectedSensorPosition(0);
        }
        //System.out.println(
        //    robotMap.LDrive1.getSelectedSensorPosition()/2048+ "  "+
        //    robotMap.RDrive2.getSelectedSensorPosition()/2048+ "  "+
        //    robotMap.LDrive3.getSelectedSensorPosition()/2048+ "  "+
        //    robotMap.RDrive4.getSelectedSensorPosition()/2048
        //);
    
    }
}
