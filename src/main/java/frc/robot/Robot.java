package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.Command;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.ejml.data.CMatrix;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.goStraight;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import frc.robot.commands.*;


public class Robot extends TimedRobot {

  SendableChooser<Command> choose = new SendableChooser<>();
    goStraight gofivefeet = new goStraight(60, 0);
    routine1 route1 = new routine1();
    routine2 route2 = new routine2();
  
  

  @Override
  public void robotInit() {
    robotMap.roboInit();
    
    choose.addOption("5 feet", gofivefeet);
    choose.addOption("route one", route1);
    choose.addOption("route two", route2);
    SmartDashboard.putData(choose);
    CameraServer.startAutomaticCapture();
    CameraServer.getVideo().getSource().setVideoMode(PixelFormat.kMJPEG, 320, 240, 24);
  }


  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    robotMap.autoInit(false); //initialize settings for autonomous

    if (choose.getSelected() != null) {
      choose.getSelected().schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    robotMap.RDrive2.setInverted(true);
    robotMap.RDrive4.setInverted(true);
    robotMap.LDrive1.setInverted(false);
    robotMap.LDrive3.setInverted(false);
  }

  @Override
  public void teleopPeriodic() {
    drive.driveMethod();
    
    robotMap.feeder.set(1);
    System.out.println("encoders = " + robotMap.feeder.getEncoder().getVelocity());
    

  }

  @Override
  public void disabledInit() {
    robotMap.LDrive1.setSelectedSensorPosition(0);
    robotMap.RDrive2.setSelectedSensorPosition(0);
    robotMap.LDrive3.setSelectedSensorPosition(0);
    robotMap.RDrive4.setSelectedSensorPosition(0);
    robotMap.RDrive2.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive1.setNeutralMode(NeutralMode.Coast);
    robotMap.RDrive4.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive3.setNeutralMode(NeutralMode.Coast);
  }

  @Override
  public void disabledPeriodic() {}


}
