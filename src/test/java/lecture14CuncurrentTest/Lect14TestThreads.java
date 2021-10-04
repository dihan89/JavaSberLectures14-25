package lecture14CuncurrentTest;

import lecture14Cuncurrent.CacheProxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Lect14TestThreads {
    HardWorker worker;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @BeforeEach
    public void prepare() {
        HardWorkerImpl workerDelegate = new HardWorkerImpl();
        worker = (HardWorker) CacheProxy.cache(workerDelegate);
        executorService = Executors.newCachedThreadPool();
    }

    @Test
    public void threadsTest(){
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
           // System.out.println("testRAM");
            for (int i = 0; i < 500; ++i)
                worker.createCube(i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            //System.out.println("testRAM");
            for (int i = 10; i < 568; ++i)
                worker.createCube(i);
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 150; i < 195; ++i)
                worker.createCube(i);
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
           // System.out.println("testRAM");
            for (int i = 300; i < 799; ++i)
                worker.createCube(i);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 25; i < 85; ++i)
                worker.createCube(i);
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            //System.out.println("testRAM");
            for (int i = 623; i < 1000; ++i)
                worker.createCube(i);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

       try {
            executorService.awaitTermination(10_000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println(CacheProxy.fileCacheCount);
    }

    @Test
    public void threadsTest2(){
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            for (int i = 0; i < 15355; ++i)
                worker.createParallelepiped(i, i+1, i + 2);
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            for (int i = 14020; i < 22011; ++i)
                worker.createParallelepiped(i, i+1, i + 2);
            for (int i = 0; i < 1; ++i)
                worker.createParallelepiped(i, i+1, i + 2);
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            for (int i = 21020; i < 32080; ++i)
                worker.createParallelepiped(i, i+1, i + 2);

            for (int i = 6; i < 1205; ++i)
                worker.createParallelepiped(i, i+1, i + 2);
        });
        executorService.submit(()->{
            System.out.println("Thread" + Thread.currentThread().getName());
            //System.out.println("testRAM");
            for (int i = 29300; i < 40000; ++i)
                worker.createParallelepiped(i, i+1, i + 2);

        });
        try {
            executorService.awaitTermination(5_000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        System.out.println(CacheProxy.ramCacheCount);

    }
}
