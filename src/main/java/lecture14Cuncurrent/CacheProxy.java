package lecture14Cuncurrent;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheProxy {
    public static volatile AtomicInteger ramCacheCount = new AtomicInteger(0);
    public static volatile AtomicInteger fileCacheCount = new AtomicInteger(0);
    public static Object cache (Object delegate){
        return cache (delegate, new File ("cache"));
    }
    public static Object cache (Object delegate , File directoryCache){
        return Proxy.newProxyInstance (ClassLoader.getSystemClassLoader(), delegate.getClass().getInterfaces(),
                        new CacheableInvocationHandler(delegate ,directoryCache));
    }
}
