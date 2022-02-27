package frc.robot.commands;

import frc.robot.robotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.CommandBase;


public class goStraight extends CommandBase{
    private float m_inches;
    private double m_angle;

    public double avgPos;
    private double AutonomousSpeed;
    private double K;
    private double error;
    private double Rot;
    private double distance;
    private double Rspeed;
    private double Lspeed;

    public goStraight(float inches, double angle) {
        m_inches = inches;
        m_angle = angle;
    }

    @Override
    public void initialize() {
        Lspeed = 0.0;
        Rspeed = 0.0;

        robotMap.LDrive3.setSelectedSensorPosition(0);
        robotMap.LDrive1.setSelectedSensorPosition(0);
        robotMap.RDrive4.setSelectedSensorPosition(0);
        robotMap.RDrive2.setSelectedSensorPosition(0);

        distance = ((m_inches)/18.85)*10.75; 
        Rot = 0.0; 
        error = m_angle+Rot;
        AutonomousSpeed = 0.3; // AutonomousSpeed * configSpeed (in CONST) = total speed of falcons
        K = 0.0614173; // gains
        avgPos = 0.0; // Calculate the average distance traveld in motor rotations

        robotMap.RDrive2.set(ControlMode.PercentOutput, AutonomousSpeed);
        robotMap.LDrive1.set(ControlMode.PercentOutput, AutonomousSpeed);

    }
  
    @Override
    public void execute() {
        
        robotMap.Lposition = robotMap.LDrive1.getSelectedSensorPosition();
        robotMap.Rposition = robotMap.RDrive2.getSelectedSensorPosition();
        robotMap.LPos = robotMap.Lposition/2048;
        robotMap.Rpos = robotMap.Rposition/2048;
        avgPos = (robotMap.LPos + robotMap.Rpos)/2;

        Rot = robotMap.imu.getAngle();
        error = m_angle+Rot;
        
        Lspeed = AutonomousSpeed - (K*error);
        Rspeed = AutonomousSpeed + (K*error);

        robotMap.RDrive2.set(ControlMode.PercentOutput, Rspeed);
        robotMap.LDrive1.set(ControlMode.PercentOutput, Lspeed);
    }

    @Override
    public void end(boolean interrupted) {
        robotMap.RDrive2.set(ControlMode.PercentOutput, 0);
        robotMap.LDrive1.set(ControlMode.PercentOutput, 0);     
    }

    @Override
    public boolean isFinished() {
        return (avgPos >= distance);
    }
    
    public void getDebug() {
        System.out.println(
            "avgPos = " + avgPos + "\n" +
            "distance = "+distance+"\n"+
            "isFinished = "+isFinished()+"\n"            
        );
    }


}


