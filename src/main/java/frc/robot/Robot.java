// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //compressor test

  
  //motors,left
  private final PWMSparkMax m_LeftDriveL = new PWMSparkMax(0);
  private final PWMSparkMax m_LeftDriveF = new PWMSparkMax(1);
  
  //motors,right
  private final PWMSparkMax m_RightDriveL = new PWMSparkMax(2);
  private final PWMSparkMax m_RightDriveF = new PWMSparkMax(3);
  
  //motors, in/out
  private final PWMSparkMax m_MotorOutL = new PWMSparkMax(4);
  private final PWMSparkMax m_MotorInF = new PWMSparkMax(5);
  
  //motor,up/down
  private final PWMSparkMax m_MotorMover = new PWMSparkMax(6);
  
  //encoders,none rn.
  private static final int kEncoderPortA = 0;
  private static final int kEncoderPortB = 1;
  private Encoder m_encoder;

  //Controller two's movement
  private final DifferentialDrive m_robotMove =
      new DifferentialDrive(m_MotorOutL::set, m_MotorMover::set);
  
  //Controller one's movement
  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive(m_LeftDriveL::set, m_RightDriveL::set);
  
  //controller one, driving
  private final XboxController m_DriveControl = new XboxController(0);
  
  //controller two, gamimer
  private final XboxController m_OperatorControl = new XboxController(1);
  
  //a timer, cuz why not
  private final Timer m_timer = new Timer();
  
  //Pneumatic ports
    private final Compressor m_compressor = new Compressor(PneumaticsModuleType.CTREPCM);
  //followers of right/left/in-out
  public Robot() {

    m_LeftDriveL.addFollower(m_LeftDriveF);
    m_RightDriveL.addFollower(m_RightDriveF);
    m_MotorOutL.addFollower(m_MotorInF);
   
    //IDK what SendableRegistry does at this time
      SendableRegistry.addChild(m_robotDrive, m_LeftDriveL);
      SendableRegistry.addChild(m_robotDrive, m_RightDriveL);
      SendableRegistry.addChild(m_robotMove, m_MotorMover);
  }


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_encoder = new Encoder(kEncoderPortA, kEncoderPortB);
    m_encoder.setDistancePerPulse((Math.PI * 6) / 360.0);
    m_RightDriveL.setInverted(true);
    m_compressor.enableDigital();
  }
  
  public void Compressor(int module, PneumaticsModuleType moduleType){
    
    m_compressor.isEnabled();
    
    
    }
  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Encoder", m_encoder.getDistance());
  }
  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (m_timer.get() < 2.0) {
      m_robotDrive.arcadeDrive(0.5,0.0, false);
      m_robotMove.arcadeDrive(1,0.5,true);
    } else {
      m_robotDrive.stopMotor();
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //drivetrain controls
    m_robotDrive.arcadeDrive(-m_DriveControl.getLeftY(), -m_DriveControl.getRightX());
    //gaming controls
    m_robotMove.arcadeDrive(-m_OperatorControl.getLeftX(), -m_OperatorControl.getRightX());
    if (m_DriveControl.getAButton()) {
           
    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
