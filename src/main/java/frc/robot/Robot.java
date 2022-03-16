package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.Command;

import java.nio.Buffer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.goStraight;
import frc.robot.commands.SUBSYS.manualShoot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;






public class Robot extends TimedRobot {


  SendableChooser<Command> choose = new SendableChooser<>();
    goStraight gofivefeet = new goStraight(60, 0);
    routine1 route1 = new routine1();
    routine2 route2 = new routine2();
  manualShoot shoot = new manualShoot(0.3);
  



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

    robotMap.shooter.set(ControlMode.PercentOutput, 0.3);
    
  }

  @Override
  public void teleopPeriodic() {
  //final byte[] mbyte = new byte[] { (byte)0x730a};
    if (robotMap.joystick.getRawButton(1)) {
      //robotMap.jevois.writeString("s");
      //System.out.println(robotMap.jevois.writeString("s"));
      //System.out.println(mbyte);
      //robotMap.jevois.write(mbyte, 1);
      
      //System.out.println("BYTES RECIVED = "+robotMap.jevois.getBytesReceived());
    }
    //System.out.println(robotMap.jevois.readString());
    //robotMap.jevois.flush();
    //System.out.println(parseJevois(jevois.readString())[0]);
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

  public static void shoot(double speed) {
    if (robotMap.shooter.getMotorOutputPercent()/speed > 0.9) {
      robotMap.feeder.set(0.4);
    } else {
      robotMap.shooter.set(ControlMode.PercentOutput, speed);
      robotMap.feeder.set(0);
    } 
  }

  public static double  f(double in) {return Math.pow(in, 2)+5;}

  public String[] parseJevois(String input) {
    input.strip();
    try {
    String[] STRdata = input.split(",");
    return STRdata;
    } catch (Exception e) {
      System.out.println("FALIED TO PARSE DATA");}
      return null;
    //Integer[] data = {1,1,1};
    //for (int i=0; i<3; i++) {data[i]= Integer.parseInt(STRdata[i]);}

  }

}
