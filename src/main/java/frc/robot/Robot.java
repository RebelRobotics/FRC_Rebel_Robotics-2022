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
//import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator.Validity;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;


public class Robot extends TimedRobot {



  // so we can choose between autonomous routines
  SendableChooser<Command> choose = new SendableChooser<>();
    goStraight gofivefeet = new goStraight(60, 0);
    routine1 route1 = new routine1();
    routine2 route2 = new routine2();
  
  private goToAngle lockOn; // global lock on command 
    public double f(double a) {return a+3;} // convert distance into shooter power *** not complete


  @Override
  public void robotInit() {
    
    robotMap.roboInit();
    
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

    if (choose.getSelected() != null) { // execute selected command
      choose.getSelected().schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // reset inversion from autonomous 
    robotMap.RDrive2.setInverted(true);
    robotMap.RDrive4.setInverted(true);
    robotMap.LDrive1.setInverted(false);
    robotMap.LDrive3.setInverted(false);

    // idle speed for shooter falcon to minimize voltage dip
    robotMap.shooter.set(ControlMode.PercentOutput, 0.3);

  }

  @Override
  public void teleopPeriodic() {
    drive();
  }


  @Override
  public void disabledInit() {
    robotMap.RDrive2.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive1.setNeutralMode(NeutralMode.Coast);
    robotMap.RDrive4.setNeutralMode(NeutralMode.Coast);
    robotMap.LDrive3.setNeutralMode(NeutralMode.Coast);
  }

  @Override
  public void disabledPeriodic() {}

  ///// SHOOTER FUNCTION ////// intended to be constantly called
  public void SHOOT(double speed) {
    if (robotMap.shooter.getMotorOutputPercent()/speed > 0.9) {
      robotMap.shooterIntake.set(0.4);
    } else {
      robotMap.shooter.set(ControlMode.PercentOutput, speed);
      robotMap.shooterIntake.set(0);
    } 
  }


  //////// TELEOP DRIVE CODE //////// called in teleop periodic
  public void drive() {
      //////////////////// CALCULATE VARIABLES AND DEADBANDS ////////////////////
      double power = robotMap.joystick.getRawAxis(1); 
          if (Math.abs(power) < CONST.DEADZONE) { power = 0; }
      double turn = robotMap.joystick.getRawAxis(2);
          if (Math.abs(turn) < CONST.DEADZONE) { turn = 0.0; }
      double slider = robotMap.joystick.getRawAxis(3);
      double slider2 = robotMap.joystick2.getRawAxis(3);
      double motor_power = (slider+1)/2; // slider +1/2 is so the entire slider can be used with no negative zone
      double turn_power = (slider2+1)/2;
    
      ///// SET MOTOR POWER
      robotMap.LDrive1.set(ControlMode.PercentOutput, (power+(turn*turn_power))*motor_power);
      robotMap.RDrive2.set(ControlMode.PercentOutput, (power-(turn*turn_power))*motor_power);
    
    
      //////// BUTTON MAPPINGS /////////
      // shooter code
      robotMap.jevois.updateVision();
      if (robotMap.joystick.getRawButton(1)) { /// SHOOT
        if (robotMap.jevois.getTracking() && lockOn != null) { // IF JEVOIS IS TRACKING GIVE CONTROL TO MACHINE
          
          if (!lockOn.isScheduled()) {
            lockOn = new goToAngle(robotMap.jevois.getAngle(), 1, 2);
            lockOn.schedule();
          }
    
          if (lockOn.isFinished()) {SHOOT(f(robotMap.jevois.getDistance()));}
        }
        else { // IF NOT TRACKING USE MANUAL CONTROL
          if (robotMap.joystick.getRawButton(3)) {/*** raise hood*/}
          else {/*** lower hood*/}
          SHOOT(0.4);
        } // *** add manual driver power
      } else {
        robotMap.shooter.set(ControlMode.PercentOutput, 0.3);
        robotMap.shooterIntake.set(0);
      }
    
      // intake code
      if (robotMap.joystick.getRawButton(2)) {
        robotMap.intake.set(Value.kReverse);
        
        robotMap.intakeSpark.set(-0.7);
        // *** start intake motors
      } else {
        robotMap.intakeSpark.set(0);
        robotMap.intake.set(Value.kForward);}
    
        System.out.println("=="+robotMap.climber.getSelectedSensorPosition()/2048);
      }

      

}