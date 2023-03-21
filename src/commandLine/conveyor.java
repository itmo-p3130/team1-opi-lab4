package commandLine;

import dataStruct.Answer;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public interface conveyor {
    ArrayList<String> comm = new ArrayList<String>();
    ArrayList<Answer> answ = new ArrayList<Answer>();
    ArrayList<command> commands_ready = new ArrayList<command>();
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
}