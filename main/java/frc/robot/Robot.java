// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI; //I connected using SPI
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  //  private DriveStrait m_myRobotds;
  private Safty safty;
  private XboxController m_driver;
  private XboxController m_opp;
  private double math_drive_gyro;
 
  //auto selector
  private static final String kDefaultAuto = "Default";
  private static final String kSpeakerSafeShot = "Speaker Shot";
  private static final String kconer2d = "Corner auto 1d side";
  private static final String kconer1d = "Corner auto 2d side";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
  private static final String streamstanderd = "Standard";
  private static final String streamPiPMain = "Limelight Cam";
  private static final String streamPiPSecondary = "Other Cam";
  private final SendableChooser<String> m_limechoser = new SendableChooser<>();
  
  private static final AHRS gyro = new AHRS(SPI.Port.kMXP); //add as a global variable near the top
  
  
  private double getAngleDiff (double angle1, double angle2) {
    var retval2 = 0.0;
    var retVal = 0.0;
    if(angle1-angle2<0) {
      if(angle1-angle2<-180) {
        retVal = angle1 + (360 - angle2);
      } else {
        retVal = angle1 - angle2 ; 
    } 
    } else {
      if((angle1- angle2) >180 ) {
       retVal = angle1-angle2-360; 
      } else {
        retVal = angle1 - angle2;
      }
    }
    retval2 = retVal / 50;
    return retval2;
  }
  // private static final MotorController m_right1Motor = new CANSparkMax(1,MotorType.kBrushless);
  private static final int m_left1Motorid = 1;
  private CANSparkMax m_left1Motor;
  // private DriveStrait m_left1Motords;
  // private final MotorController m_right2Motor = new CANSparkMax(2,MotorType.kBrushless);
  private static final int m_left2Motorid = 2;
  private CANSparkMax m_left2Motor;
  //private DriveStrait m_left2Motords;
  // private final MotorController m_left1Motor = new CANSparkMax(3,MotorType.kBrushless);
  private static final int m_right1Motorid = 3;
  private CANSparkMax m_right1Motor;
  //private DriveStrait m_right1Motords;
  // private final MotorController m_left2Motor = new CANSparkMax(4,MotorType.kBrushless);
  private static final int m_right2Motorid = 4;
  private CANSparkMax m_right2Motor;
  //private DriveStrait m_right2motords;
  //MotorControllerGroup m_leftMotor = new MotorControllerGroup(m_left1Motor, m_left2Motor);
  //MotorControllerGroup m_rightMotor = new MotorControllerGroup(m_right1Motor, m_right2Motor);
  private Spark m_RampUppy;

  // private final MotorController m_intake1Motor = new CANSparkMax(14,MotorType.kBrushless);
  private static final int m_intake1Motorid = 14;
  private CANSparkMax m_intake1Motor;
  // private final MotorController m_intake2Motor = new CANSparkMax(5,MotorType.kBrushless);
  private static final int m_intake2Motorid = 13;
  private CANSparkMax m_intake2Motor;
  // private final MotorController m_advancerMotor = new CANSparkMax(13,MotorType.kBrushless);
  private static final int m_advancerMotoroid = 5;
  private CANSparkMax m_advancerMotor;
  // private final MotorController m_shooter1Motor = new Spark(0);
  private static final int m_shooter1Motorid = 16;
  private CANSparkMax m_shooter1Motor;
  // private final MotorController m_shooter2Motor = new Spark(1);
  private static final int m_shooter2Motorid = 15;
  private CANSparkMax m_shooter2Motor;
  // private final MotorController m_shoulderintakeMotor = new CANSparkMax(9,MotorType.kBrushless);
  private static final int m_shoulderintakeMotorid = 9;
  private CANSparkMax m_shoulderintakeMotor;
  // private final MotorController m_shouldermoveMotor = new CANSparkMax(10,MotorType.kBrushless);
  private static final int m_shouldermoveMotorid = 10;
  private CANSparkMax m_shouldermoveMotor;
  // private final MotorController m_shoulderrotateMotor = new CANSparkMax(3,MotorType.kBrushless);
  private static final int m_shoulderrotateMotorid = 11;
  private CANSparkMax m_shoulderrotateMotor;
  // private final MotorController m_climbMotor = new CANSparkMax(12,MotorType.kBrushless);
  private static final int m_climbMotorid = 12;
  private CANSparkMax m_climbMotor;
 
  private double starttime;
  private double heading;
  private double ismoving;
  private double step;
  private double limelightCam;
  //limelight stuff
 
  
  @Override
  public void robotInit() {
   
    //auto selector
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Speaker Shot", kSpeakerSafeShot);
    m_chooser.addOption("Corner auto 2d side", kconer2d);
    m_chooser.addOption("Corner auto 1d side", kconer1d);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_limechoser.setDefaultOption("Standard", streamstanderd);
    m_limechoser.addOption("Limelight Cam", streamPiPMain);
    m_limechoser.addOption("Other Cam", streamPiPSecondary);
    SmartDashboard.putData("Lime light camra", m_limechoser);

    m_right1Motor = new CANSparkMax(m_right1Motorid, MotorType.kBrushless);
    m_RampUppy = new Spark(0);
    m_right2Motor = new CANSparkMax(m_right2Motorid, MotorType.kBrushless);
    m_left1Motor = new CANSparkMax(m_left1Motorid, MotorType.kBrushless);
    m_left2Motor = new CANSparkMax(m_left2Motorid, MotorType.kBrushless);
    m_intake1Motor = new CANSparkMax(14, MotorType.kBrushless);
    m_intake2Motor = new CANSparkMax(13, MotorType.kBrushless);
    m_shooter2Motor = new CANSparkMax(m_shooter2Motorid, MotorType.kBrushless);
    m_shooter1Motor = new CANSparkMax(m_shooter1Motorid, MotorType.kBrushless);
    m_advancerMotor = new CANSparkMax(m_advancerMotoroid, MotorType.kBrushless);
    
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_right1Motor.setInverted(true);
    m_right2Motor.follow(m_right1Motor);
    m_left2Motor.follow(m_left1Motor);
    m_myRobot = new DifferentialDrive(m_left1Motor, m_right1Motor);
    m_shooter1Motor.setInverted(true);
    m_intake2Motor.follow(m_intake1Motor);
    m_driver = new XboxController(0);
    m_opp = new XboxController(1);
    //limelight stuff
    // Make sure you only configure port forwarding once in your robot code.
    // Do not place these function calls in any periodic functions
    for (int port = 5800; port <= 5807; port++) {
      PortForwarder.add(port, "limelight.local", port);
    }
  }
 
  @Override
  public void autonomousInit() {
    starttime = Timer.getFPGATimestamp();
    heading = gyro.getYaw();
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }
  
  @Override
  public void disabledPeriodic() {
    // safty.checkifsafe();
    double yaw =  gyro.getYaw();
    double pitch = gyro.getPitch();
    double roll =  gyro.getRoll();
    SmartDashboard.putNumber("gyro pitch", Math.round(pitch));
    SmartDashboard.putNumber("gyro yaw", Math.round(yaw));
    SmartDashboard.putNumber("gyro roll", Math.round(roll));
    //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");

    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
  }
 
 
 
  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kSpeakerSafeShot:
    // safty.checkifsafe();
    //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
    double time = Timer.getFPGATimestamp();
    if (time - starttime < 1.3 ) {
      m_left1Motor.set(.2);
      m_right1Motor.set(.2);
      m_intake1Motor.set(-1);
    }
    if (time - starttime > 2){
      m_intake1Motor.set(0);
      m_left1Motor.set(-.1);
      m_right1Motor.set(-.2);
    }
    if (time - starttime > 3) {
      m_left1Motor.set(0);
    }
    if (time - starttime > 3.01){
      if ( x >= 5) {
        m_left1Motor.set(.1);
     
      if (x <= 5){
        m_left1Motor.set(0);
      }
      } else if (x <= -5) {
      m_right1Motor.set(.1);
      if (x>=-5){
        m_right1Motor.set(0);
      }
      } else {
      double time1 = Timer.getFPGATimestamp();
      m_shooter1Motor.set(1);
      m_shooter2Motor.set(1);
      if (System.currentTimeMillis() - time1 < 2){
        m_advancerMotor.set(1);
      }
    }
  }
      break;
      case kDefaultAuto:
      default:
        double yaw =  gyro.getYaw();
        double pitch = gyro.getPitch();
        double roll =  gyro.getRoll();
        SmartDashboard.putNumber("gyro pitch", Math.round(pitch));
        SmartDashboard.putNumber("gyro yaw", Math.round(yaw));
        SmartDashboard.putNumber("gyro roll", Math.round(roll));
        double timed = Timer.getFPGATimestamp();
        ismoving = Math.signum(Math.abs(m_right1Motor.getAppliedOutput())) + Math.signum(Math.abs(m_left1Motor.getAppliedOutput())); 
        // signum means if its obove 1 returns 1 if below 1 returns -1 and if 0 returns 0 
        //so it will tell me if its moving 2, if its not moving 0 and, if its turning 2
        SmartDashboard.putNumber("Step", step);              
        if(timed - starttime < 3) {
          m_shooter1Motor.set(-.6);
          m_shooter2Motor.set(.6);
          step = 1;
        } else {
          m_shooter1Motor.set(0);
          m_shooter2Motor.set(0);
          step = 1.5;
        }
        if(timed - starttime > 2) {
          m_advancerMotor.set(-1);
          step = 2;
        }
        if (timed - starttime > 3.3 ) {
         step = 3;
          m_right1Motor.set(.3);
          m_left1Motor.set(.3);
          m_intake1Motor.set(1);
          m_advancerMotor.set(-0.4);
        }
        
        if (timed - starttime > 5){
          step = 4;
          m_intake1Motor.set(0);
          m_right1Motor.set(-.3);
          m_left1Motor.set(-.3);
        }
        if (timed - starttime > 5.7) {
          m_advancerMotor.set(0);
          m_advancerMotor.set(1);
        }
        if ( timed - starttime > 6) {
          m_advancerMotor.set(0);
        }
        if (timed - starttime > 6.9) {
          step = 5;
          m_left1Motor.set(0);
          m_right1Motor.set(0);
          m_shooter1Motor.set(-.75);
          m_shooter2Motor.set(.75);
          m_advancerMotor.set(0);
        }
        if (timed - starttime > 8.3) {
          m_advancerMotor.set(-1);
          
          step = 6;
        }
        if (timed - starttime > 9) {
          m_advancerMotor.set(0);
          m_right1Motor.set(-.3);
          m_left1Motor.set(-.3);
          m_intake1Motor.set(0);
          m_shooter1Motor.set(0);
          m_shooter2Motor.set(0);
        }
        if (timed - starttime > 10.9) {
          m_right1Motor.set(0);
          m_left1Motor.set(0);
        }
        break;

        case kconer1d:
        pitch = gyro.getPitch();
        yaw = gyro.getYaw();
        roll = gyro.getRoll();
        SmartDashboard.putNumber("gyro pitch", Math.round(pitch));
        SmartDashboard.putNumber("gyro yaw", Math.round(yaw));
        SmartDashboard.putNumber("gyro roll", Math.round(roll));
        double timed2 = Timer.getFPGATimestamp();
        ismoving = Math.signum(Math.abs(m_right1Motor.getAppliedOutput())) + Math.signum(Math.abs(m_left1Motor.getAppliedOutput())); 
        // signum means if its obove 1 returns 1 if below 1 returns -1 and if 0 returns 0 
        //so it will tell me if its moving 2, if its not moving 0 and, if its turning 2
        SmartDashboard.putNumber("Step", step);           
        if(timed2 - starttime < 5) {
          m_shooter1Motor.set(-.8);
          m_shooter2Motor.set(.8);
          step = 1;
        } else {
          m_shooter1Motor.set(0);
          m_shooter2Motor.set(0);
          step = 1.5;
        }
        if(timed2 - starttime > 4) {
          m_advancerMotor.set(-1);
          step = 2;
        }
        if (timed2 - starttime > 7.3 ) {
         step = 3;
         m_advancerMotor.set(0);
          m_right1Motor.set(.3);
          m_left1Motor.set(.3);
          m_intake1Motor.set(1);
        }
        if (timed2 - starttime > 9.7) {
          step = 4;
          m_right1Motor.set(-.3);
          m_left1Motor.set(-.3);
        }
        if (timed2 - starttime > 11) {
          step = 5;
          m_left1Motor.set(0);
          m_right1Motor.set(0);
          m_shooter1Motor.set(-.75);
          m_shooter2Motor.set(.75);
        }
        if (timed2 - starttime > 13) {
          m_advancerMotor.set(-1);
          step = 6;
        }

        break;
          case kconer2d:

        break;
      }
   
   
  }
  @Override
  public void teleopInit() {
  heading = gyro.getYaw();
  }


  @Override
  public void teleopPeriodic() {
    //gyro stuff
    double yaw = gyro.getAngle();
    double pitch =gyro.getPitch();
    double roll = gyro.getRoll();
    SmartDashboard.putNumber("gyro pitch", Math.round(pitch));
    SmartDashboard.putNumber("gyro yaw", Math.round(yaw));
    SmartDashboard.putNumber("gyro roll", Math.round(roll));
    //limelight stuff
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);
    SmartDashboard.putNumber("x", m_driver.getLeftX());
    SmartDashboard.putNumber("y", m_driver.getLeftY());
    if ((Math.abs(m_driver.getLeftX()) > 0.1) || (Math.abs(m_driver.getLeftY()) > 0.115625 )) {
      heading = Math.round(gyro.getYaw());
      SmartDashboard.putNumber("Heading:", heading);
    }
    if (m_opp.getLeftBumper() || m_driver.getLeftBumper()){
      m_intake1Motor.set(1);
      m_advancerMotor.set(-1);
    } else if (m_opp.getRightBumper()|| m_driver.getRightBumper()) {
      m_intake1Motor.set(-1);
      m_advancerMotor.set(1);
    } else {
      m_intake1Motor.set(0);
      m_advancerMotor.set(0);
    }
    if (m_opp.getAButton() || m_driver.getAButton()){
      m_shooter1Motor.set(-1);
      m_shooter2Motor.set(1);
    } else if (m_opp.getYButton() || m_driver.getYButton()) {
      m_shooter1Motor.set(1);
      m_shooter2Motor.set(-1);
    } else if (m_opp.getBButton() || m_driver.getBButton()) {
      m_shooter1Motor.set(-0.1);
      m_shooter2Motor.set(0.5);
    } else {
      m_shooter1Motor.set(0);
      m_shooter2Motor.set(0);
    }
    m_shooter1Motor.set(m_opp.getLeftTriggerAxis());
    m_shooter2Motor.set(m_opp.getRightTriggerAxis());
    m_RampUppy.set(m_opp.getRightY()*-5);
    m_myRobot.arcadeDrive( m_driver.getLeftY(), ((m_driver.getLeftX() )/-1));
  }
}