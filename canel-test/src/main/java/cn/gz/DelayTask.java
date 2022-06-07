package cn.gz;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DelayTask {
    public static void main(String[] args) throws InterruptedException {
//        netty();
//        timer();
        delayQueue();
    }

    private static void netty() {
        /**
         * 参数依次为
         * 1.ThreadFactory 自定义线程工厂，用于创建线程执行TimerTask
         * 2.tickDuration  间隔多久走到下一槽（相当于时钟走一格），值越小，时间轮精度越高
         * 3.unit 定义tickDuration的时间单位
         * 4.ticksPerWheel 一圈有多个槽
         * 5.leakDetection 是否开启内存泄漏检测。
         * 6. maxPendingTimeouts 最多待执行的任务个数。0或负数表示无限制。
         */
        Timer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 1000, TimeUnit.MILLISECONDS, 10);
        System.out.println("开始添加任务:" + new Date());
        //延迟任务，5秒后执行
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                System.out.println("任务开始执行:" + new Date());
            }
        }, 5, TimeUnit.SECONDS);
    }

    private static void timer() {
        System.out.println("timer:" + new Date());
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println("timer:" + new Date());
            }
        }, 1000);

        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println("timer2:" + new Date());
            }
        }, 1000, 1000);
    }

    private static void delayQueue() throws InterruptedException {
        DelayQueue<DelayedUser> user = new DelayQueue<>();
        AtomicInteger i = new AtomicInteger();
        CompletableFuture.runAsync(() -> {
            for (int j = 0; j < 1000; j++) {
               // CompletableFuture.runAsync(() -> {
                  //  while (true) {
                        user.put(new DelayedUser("delay " + i.getAndIncrement(), new Random().nextLong() % 1000+1));
//                        try {
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                   // }
                //});
            }
        });

        System.out.println("===============");
        Thread.sleep(3000);

        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    DelayedUser delayedUser = user.take();
                    System.out.println("user:" + delayedUser.getName() + " , " + new Date());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        synchronized (DelayTask.class){
            DelayTask.class.wait();
        }
    }

    public static class DelayedUser implements Delayed {
        private String name;
        private long avaibleTime;

        public String getName() {
            return name;
        }

        public long getAvaibleTime() {
            return avaibleTime;
        }

        public DelayedUser(String name, long delayTime) {
            this.name = name;
            //avaibleTime = 当前时间+ delayTime
            this.avaibleTime = delayTime + System.currentTimeMillis();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            //判断avaibleTime是否大于当前系统时间，并将结果转换成MILLISECONDS
            long diffTime = avaibleTime - System.currentTimeMillis();
            return unit.convert(diffTime, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            //compareTo用在DelayedUser的排序
            return (int) (this.avaibleTime - ((DelayedUser) o).getAvaibleTime());
        }
    }
}
