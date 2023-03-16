import commandLine.Commander;
import consoleParser.Parser;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class Main {
    //863205
    public static void main(String[] args) {
        Parser prs = new Parser("Main parser thread",Thread.currentThread(), syncho);
        Commander com = new Commander("Main commander thread",Thread.currentThread(), syncho);
        prs.start();
        com.start();
        //try{Thread.sleep(5000);}catch (InterruptedException ex){;}
        //com.printAll();
        while(prs.isAlive()){}//I'll change it to prs.join(), but later
    }
    Condition cond = new Condition() {
        @Override
        public void await() throws InterruptedException {

        }

        @Override
        public void awaitUninterruptibly() {

        }

        @Override
        public long awaitNanos(long nanosTimeout) throws InterruptedException {
            return 0;
        }

        @Override
        public boolean await(long time, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public boolean awaitUntil(Date deadline) throws InterruptedException {
            return false;
        }

        @Override
        public void signal() {

        }

        @Override
        public void signalAll() {

        }
    }
}