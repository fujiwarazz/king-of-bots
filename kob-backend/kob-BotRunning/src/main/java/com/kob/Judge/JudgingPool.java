package com.kob.Judge;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author peelsannaw
 * @create 10/01/2023 14:01
 */
public class JudgingPool extends Thread {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Judge> queue = new LinkedList<>();
//    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    public void addOneJudge(Long userId, String botCode, String input) {
        lock.lock();
        try {
            queue.add(new Judge(userId, botCode, input));
            //唤醒被lock阻塞的队列
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 编译java代码
     * @param judge
     */
    private void consumeJudge(Judge judge) {
        Consumer consumer = new Consumer();
        consumer.startWithTimeOut(2000L,judge);

    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (queue.size() == 0) {
                try {
                    //阻塞线程，并且会设防锁，为了避免死锁
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Judge remove = queue.remove();
                lock.unlock();
                //比较耗时，因此需要先解锁
                consumeJudge(remove);
            }
        }
    }
}
