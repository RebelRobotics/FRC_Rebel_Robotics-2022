package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.commands.vision;


public class robotMap {
  
  //// JOYSTICS 
  public static final Joystick joystick = new Joystick(1);
  public static final Joystick joystick2 = new Joystick(0);
  
  //// GYRO 
  public static final ADIS16470_IMU imu = new ADIS16470_IMU();
 

  //// DRIVE MOTORS
  public static final TalonFX LDrive1 = new TalonFX(1);
  public static final TalonFX RDrive2 = new TalonFX(2);
  public static final TalonFX LDrive3 = new TalonFX(3);
  public static final TalonFX RDrive4 = new TalonFX(4);
    public static double Lvelocity = LDrive1.getSelectedSensorVelocity(); // *** possibly depricate or add to thread
    public static double Rvelocity = RDrive2.getSelectedSensorVelocity();
    public static double Lposition = LDrive1.getSelectedSensorPosition();
    public static double Rposition = RDrive2.getSelectedSensorPosition();
    public static double Lrpm = Lvelocity*10*60/2048;
    public static double Rrpm = Rvelocity*10*60/2048;
    public static double LPos = 0;
    public static double Rpos = 0;

  //// SHOOTER MOTORS
  public static TalonFX shooter = new TalonFX(8);
  public static CANSparkMax shooterIntake = new CANSparkMax(5, MotorType.kBrushless);

  //// SHOOTER HOOD
  private static DoubleSolenoid h1 = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 4, 5);
  private static DoubleSolenoid h2 = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 2, 3);
    public static void raiseHood() {
      h1.set(Value.kForward);
      h2.set(Value.kForward);
    }
    public static void lowerHood() {
      h1.set(Value.kReverse);
      h2.set(Value.kReverse);
    }

    //// INTAKE PISTON
    static DoubleSolenoid intake = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 1);
    static CANSparkMax intakeSpark = new CANSparkMax(15, MotorType.kBrushless);

  //// JEVOIS 
  public static SerialPort jevoisPort = new SerialPort(9600, Port.kUSB1);
  public static vision jevois = new vision(jevoisPort);

    public static TalonFX climber = new TalonFX(9);


  static void autoInit() {
    // ZERO THE ENCODERS
    LDrive1.setSelectedSensorPosition(0);
    RDrive2.setSelectedSensorPosition(0);
    LDrive3.setSelectedSensorPosition(0);
    RDrive4.setSelectedSensorPosition(0);

    // invert motors for autonomous
    RDrive2.setInverted(false);
    RDrive4.setInverted(false);
    LDrive1.setInverted(true);
    LDrive3.setInverted(true);

    // brake mode is so that the motors will stop and minimize coasting
    RDrive2.setNeutralMode(NeutralMode.Brake);
    LDrive1.setNeutralMode(NeutralMode.Brake);
    RDrive4.setNeutralMode(NeutralMode.Brake);
    LDrive3.setNeutralMode(NeutralMode.Brake);
    
    // sets curret axis to 0
    imu.reset();
  }
  
  static void roboInit() {
    ////// CONFIG DRIVE MOTORS //////
    LDrive1.configFactoryDefault(30);
    LDrive3.configFactoryDefault(30);
    RDrive2.configFactoryDefault(30);
    RDrive4.configFactoryDefault(30);
    RDrive2.configPeakOutputForward(CONST.driveSpeed, 30);
    RDrive2.configPeakOutputReverse(-CONST.driveSpeed, 30);
    RDrive4.configPeakOutputForward(CONST.driveSpeed, 30);
    RDrive4.configPeakOutputReverse(-CONST.driveSpeed, 30);
    LDrive1.configPeakOutputForward(CONST.driveSpeed, 30);
    LDrive1.configPeakOutputReverse(-CONST.driveSpeed, 30);
    LDrive3.configPeakOutputForward(CONST.driveSpeed, 30);
    LDrive3.configPeakOutputReverse(-CONST.driveSpeed, 30);

    // zero encoders
    LDrive3.setSelectedSensorPosition(0);
    LDrive1.setSelectedSensorPosition(0);
    RDrive4.setSelectedSensorPosition(0);
    RDrive2.setSelectedSensorPosition(0);

    // ensure the 2 drive motors are going in the correct direction
    RDrive2.setInverted(true);
    RDrive4.setInverted(true);
    LDrive3.follow(robotMap.LDrive1);
    RDrive4.follow(robotMap.RDrive2);


    ///// CONFIG SHOOTER FALCON MOTOR
    shooter.configFactoryDefault();
    shooter.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    shooter.configPeakOutputForward(1);
    shooter.configPeakOutputReverse(-1);
    // config shooter PID loop 
    shooter.config_kF(0, 0);
    shooter.config_kP(0, 0.6); 
    shooter.config_kI(0, 0.0);
    shooter.config_kD(0, 0.1); // shooter has flywheel so derivative is not as important
    shooter.configAllowableClosedloopError(0, 0.01);




    // CONFIG SHOOTER INTAKE (neo)
  

    // CALIBRATE GYRO 
    imu.calibrate();
    

    // RESET SERIAL PORT
    jevoisPort.reset();
  }
}

