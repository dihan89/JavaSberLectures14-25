package lecture14CuncurrentTest;

import lecture14Cuncurrent.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class Lecture14Test {
    @Test
    public  void test(){
        HardWorkerImpl workerDelegate = new HardWorkerImpl();
        System.out.println(workerDelegate.createCube(5).toString());
        // File rootDir = new File("cacheStrange");
        HardWorker worker = (HardWorker) Proxy.newProxyInstance
                (ClassLoader.getSystemClassLoader(), workerDelegate.getClass().getInterfaces(),
                        new CacheableInvocationHandlerOLDVERSION(workerDelegate));
    }
}
