package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;

public class ShootCommand extends Command {
    private final Arm arm;
    private final Shooter shooter;
    private boolean isTransitRunning;
    private boolean isNoteOuted;
    private Timer timer;
    private boolean hasStartedTimer;

    private double shooterSetpoint;

    public ShootCommand(Arm arm, Shooter shooter, double shooterSetpoint) {
        this.arm = arm;
        this.shooter = shooter;
        this.shooterSetpoint = shooterSetpoint;
        addRequirements(this.arm);
    }

    @Override
    public void initialize() {
        isTransitRunning = false;
        isNoteOuted = false;
        timer = new Timer();
        hasStartedTimer = false;
    }

    @Override
    public void execute() {
        // 启动发射器（开环控制）
        shooter.setShooterOpenloop(-0.6);  // 80% 速度
        
        // 开始计时
        if (!hasStartedTimer) {
            timer.start();
            hasStartedTimer = true;
        }
        
        // 0.5秒后启动transit
        if (timer.get() >= 0.5 && !isTransitRunning) {
            shooter.setTransitSpeed(1.0);  // 100% 速度
            isTransitRunning = true;
            System.out.println("Transit started after 0.5s!");
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setShooterOpenloop(0);
        shooter.setTransitSpeed(0);
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}