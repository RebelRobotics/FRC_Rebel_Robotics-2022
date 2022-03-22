package frc.robot;

import frc.robot.commands.goStraight;
import frc.robot.commands.goToAngle;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.VideoMode.PixelFormat;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class Robot extends TimedRobot {

  // so we can choose between autonomous routines
  SendableChooser<Command> choose = new SendableChooser<>();
    goStraight gofivefeet = new goStraight(60, 0);
    routine1 route1 = new routine1();
    routine2 route2 = new routine2();
    goToAngle angle = new goToAngle(-20, 0.008, 0.00005, 0.01);
  
  private goToAngle lockOn; // global lock on command 
    public double f(double a) {return a+3;} // convert distance into shooter power *** not complete


  @Override
  public void robotInit() {
    
    robotMap.roboInit();
    System.out.println("Initial angle = "+robotMap.imu.getYComplementaryAngle());
    robotMap.imu.reset();
    
    // add auto routines
    choose.addOption("5 feet", gofivefeet);
    choose.addOption("route one", route1);
    choose.addOption("route two", route2);
    SmartDashboard.putData(choose);
    
    CameraServer.startAutomaticCapture(); // start camera server for driver cam

    // set video parameters
    CameraServer.getVideo().getSource().setVideoMode(PixelFormat.kMJPEG, 320, 240, 24);
  }


  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); // must be run for commands to be schedulesd 
  }

  @Override
  public void autonomousInit() {
    robotMap.autoInit(); 
    
    robotMap.RDrive2.configPeakOutputForward(CONST.driveSpeed, 30);
    robotMap.RDrive2.configPeakOutputReverse(-CONST.driveSpeed, 30);
    robotMap.RDrive4.configPeakOutputForward(CONST.driveSpeed, 30);
    robotMap.RDrive4.configPeakOutputReverse(-CONST.driveSpeed, 30);
    robotMap.LDrive1.configPeakOutputForward(CONST.driveSpeed, 30);
    robotMap.LDrive1.configPeakOutputReverse(-CONST.driveSpeed, 30);
    robotMap.LDrive3.configPeakOutputForward(CONST.driveSpeed, 30);
    robotMap.LDrive3.configPeakOutputReverse(-CONST.driveSpeed, 30);
    //if (choose.getSelected() != null) { // execute selected command
    //  choose.getSelected().schedule();
    //}
    angle.schedule();
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // reset inversion from autonomous 
    robotMap.RDrive2.setInverted(false);
    robotMap.RDrive4.setInverted(false);
    robotMap.LDrive1.setInverted(true);
    robotMap.LDrive3.setInverted(true);

    // idle speed for shooter falcon to minimize voltage dip
    robotMap.shooter.set(ControlMode.PercentOutput, CONST.teleopIdle);
  }


  @Override
  public void teleopPeriodic() {
    drive();
    handleShooting();
    checkButtons();
    
  }


  //////// TELEOP DRIVE CODE //////// called in teleop periodic
  public void drive() {
    ///////////////// CALCULATE VARIABLES AND DEADBANDS ////////////////////
    double power = robotMap.joystick.getRawAxis(1); 
        if (Math.abs(power) < CONST.DEADZONE) { power = 0; }
    double turn = robotMap.joystick.getRawAxis(2);
        if (Math.abs(turn) < CONST.DEADZONE) { turn = 0.0; }
    double slider = robotMap.joystick.getRawAxis(3);
    double slider2 = robotMap.joystick2.getRawAxis(3);
    double motor_power = (slider+1)/2; // slider +1/2 is so the entire slider can be used with no negative zone
    double turn_power = (slider2+1)/2;
  
    ///// SET MOTOR POWER
    robotMap.LDrive1.set(ControlMode.PercentOutput, (power-(turn*turn_power))*motor_power);
    robotMap.RDrive2.set(ControlMode.PercentOutput, (power+(turn*turn_power))*motor_power);
  }
  

  //////// BUTTON MAPPINGS /////////
  public void checkButtons() {
      // intake
      if (robotMap.joystick.getRawButton(2)) {
        //robotMap.intake.set(Value.kForward);
        robotMap.intakeSpark.set(-0.8);

          } else {
        robotMap.intakeSpark.set(0);
        robotMap.intake.set(Value.kReverse);
      }
  
      // climb
      if (robotMap.joystick.getRawButton(12)) {
        robotMap.climber.set(ControlMode.Position, 55);
      }

      
  }


  //// decides how to shoot with tracking or without
  public void handleShooting() {
      // shooter code
      robotMap.jevois.updateVision();
      if (robotMap.joystick.getRawButton(1)) { /// SHOOT
        if (robotMap.jevois.getTracking() && lockOn != null) { // IF JEVOIS IS TRACKING GIVE CONTROL TO MACHINE
          
          if (!lockOn.isScheduled()) {
            lockOn = new goToAngle(robotMap.jevois.getAngle(), 1,0.001 , 2);
            lockOn.schedule();
          }
    
          if (lockOn.isFinished()) {SHOOT(f(robotMap.jevois.getDistance()));}
        }
        else { // IF NOT TRACKING USE MANUAL CONTROL
          if (robotMap.joystick.getRawButton(3)) {robotMap.raiseHood();}
          else {robotMap.lowerHood();}
          SHOOT(0.4);
        } // *** add manual driver power
      } else {
        robotMap.shooter.set(ControlMode.PercentOutput, CONST.teleopIdle);
        robotMap.shooterIntake.set(0);
      }

    }


  ///// SHOOTER FUNCTION ////// intended to be constantly called
  public void SHOOT(double speed) {
    if (robotMap.shooter.getMotorOutputPercent()/speed > 0.9) {
      robotMap.shooterIntake.set(0.4);
    } else {
      robotMap.shooter.set(ControlMode.PercentOutput, speed);
      robotMap.shooterIntake.set(0);
    } 
  }

  @Override
  public void disabledInit() {
    robotMap.RDrive2.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive1.setNeutralMode(NeutralMode.Coast);
    robotMap.RDrive4.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive3.setNeutralMode(NeutralMode.Coast);
    
    System.out.println(robotMap.imu.getAngle()+ " == angle");

  }

  @Override
  public void disabledPeriodic() {}


  }