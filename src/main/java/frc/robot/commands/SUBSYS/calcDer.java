package frc.robot.commands.SUBSYS;

import frc.robot.commands.goToAngle;

public class calcDer implements Runnable{
    private boolean shouldStop = false;

    double Err;
    goToAngle command;
    Thread T;

    public double der = 0;

    public synchronized void stop() {
        this.shouldStop = true;
    }

    public calcDer( goToAngle cmd) {
        Err = cmd.error;
        command = cmd;
        T = new Thread() {
            public void run() {
                System.out.println("THREADDING");
                try {Thread.sleep(2000);}
                catch (Exception E) {
                    System.out.println(E+"\n THREAD SLEEP FAILED");
                }
            }
        };
    }
    public void run() {
        while (shouldStop==false) {
            long oldTIme = System.currentTimeMillis();
            double oldError = command.error;
            try {Thread.sleep(20);}
            catch (Exception e) {System.out.println(e);}
            der = (command.error-oldError)/(System.currentTimeMillis()-oldTIme);
        
        }
    }
    
    
}
