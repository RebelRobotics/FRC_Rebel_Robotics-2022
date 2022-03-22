package frc.robot.commands.SUBSYS;

import frc.robot.commands.goToAngle;

public class calcDer implements Runnable{
    private boolean shouldStop = false;
    double Err;
    goToAngle command;

    public double der = 0;
    public double I=0;

    public synchronized void stop() {
        this.shouldStop = true;
    }

    public synchronized void start() {
        this.shouldStop = false;
    }

    public calcDer(goToAngle cmd) {
        Err = cmd.error;
        command = cmd;
    }

    public void run() {
        while (true) {
            if (shouldStop==false) {
                long oldTIme = System.currentTimeMillis();
                double oldError = command.error;

                try {Thread.sleep(50);}
                catch (Exception e) {System.out.println(e);}

                if (I<1000) {I = I + command.error;}
                System.out.println("I = "+I); 
                System.out.println("___________EXECUTING THREAD_________");

                der = (command.error-oldError)/
                    (System.currentTimeMillis()-oldTIme);
                //System.out.println("der = "+der+" = "+command.error+" - "+oldError+" / "+System.currentTimeMillis()+" - "+oldTIme);
            }
        }
    }
}
