package lecture14Cuncurrent;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;


public class CacheableInvocationHandler implements InvocationHandler {
    private final Object delegate;
    private final Map<String, Map<ArgumentsObjs, Object>> cache;
    private final File rootDirectory;
    private final static ReadWriteLock lockerRAM = new ReentrantReadWriteLock();
    private final static Lock readerLock = lockerRAM.readLock();
    private final static Lock writerLock = lockerRAM.writeLock();
    private final static ReadWriteLock lockerFILE = new ReentrantReadWriteLock();
    private final static Lock readerLockFile = lockerFILE.readLock();
    private final static Lock writerLockFile = lockerFILE.writeLock();
    private final static Map<String, Pair> mutexes = new ConcurrentHashMap<>();
    private final static Set<String> emptyMutexes = new ConcurrentSkipListSet<>();


    public CacheableInvocationHandler(Object delegate) {
        this(delegate, new File("cache"));
    }

    public CacheableInvocationHandler(Object delegate, File directory) {
        this.delegate = delegate;
        cache = new ConcurrentHashMap<>();
        this.rootDirectory = directory;
        if (!rootDirectory.exists())
            rootDirectory.mkdir();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cacheable.class))
            return method.invoke(delegate, args);
        Cacheable annotationCash = method.getAnnotation(Cacheable.class);
        return (annotationCash.saveTofile()) ? invokeFromCacheFile(method, args, annotationCash) :
                invokeFromCacheRAM(method, args, annotationCash);
    }


    private Object invokeFromCacheFile(Method method, Object[] args, Cacheable cacheableAnnotation) {
        clearMutexes();
        File directory = (cacheableAnnotation.folderNameCache().equals("")) ?
                new File(rootDirectory, getFullName(method)) :
                new File(rootDirectory, cacheableAnnotation.folderNameCache());
        writerLockFile.lock();
        if (!directory.exists())
            directory.mkdir();
        writerLockFile.unlock();
        int[] indexes = cacheableAnnotation.indexes();
        ArgumentsObjs arg = Arrays.equals(indexes, new int[]{-1}) ? new ArgumentsObjs(args) :
                new ArgumentsObjs(args, indexes);
        Object returnResult;
        Map<ArgumentsObjs, Object> cacheWithSameHashCode = null;
        String fileName = Integer.toString(arg.hashCode());
        File fileWithResult = new File(directory,
                fileName + (cacheableAnnotation.zip() ? ".zip" : ".ser"));
        String fileNameFull = String.format("%s%s", directory.getName(), fileName);
        synchronized (mutexes) {
            if (mutexes.containsKey(fileNameFull)) {
                synchronized (mutexes.get(fileNameFull).mutex){
                    mutexes.get(fileNameFull).count++;
                }
            } else {
                mutexes.put(fileNameFull, new Pair(new Object(), 1));
            }
        }

        synchronized (mutexes.get(fileNameFull).mutex) {

            try {
                if (fileWithResult.exists()) {
                    try (ObjectInputStream reader = cacheableAnnotation.zip() ?
                            new ObjectInputStream(new ByteArrayInputStream(ZipArchiver.expand(
                                    fileWithResult))) :
                            new ObjectInputStream(new FileInputStream(fileWithResult))) {
                        // System.out.println("CACHE FILE EXISTS");
                        try {
                            cacheWithSameHashCode = (Map<ArgumentsObjs, Object>) reader.readObject();
                        } catch (ClassNotFoundException exc) {
                            System.out.printf("Object in file %s is not Map<>:  %s", fileWithResult, exc.getMessage());
                        }
                        assert cacheWithSameHashCode != null;
                        returnResult = cacheWithSameHashCode.get(arg);
                        if (returnResult != null) {
                            // System.out.println("FROM CACHE FILE");
                            return returnResult;
                        }
                    } catch (IOException exc) {
                        System.out.println("This file is not correct: " + fileWithResult.getName() + " " + exc.getMessage());
                        cacheWithSameHashCode = new HashMap<>();
                    }
                } else {
                    cacheWithSameHashCode = new HashMap<>();
                }


                returnResult = getResultFromDelegate(delegate, method, args);
                Object cacheResult = List.class.isAssignableFrom(method.getReturnType()) ?
                        getReducantList((List<?>) returnResult, cacheableAnnotation) : returnResult;
                cacheWithSameHashCode.put(arg, cacheResult);
                if (cacheWithSameHashCode.size() > 1) {
                    cacheWithSameHashCode.entrySet().
                            forEach(System.out::println);
                }
                // System.out.println("NEW RESULT");
                if (!fileWithResult.exists() || fileWithResult.delete())
                    if (cacheableAnnotation.zip()) {
                        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                            ObjectOutputStream writer = new ObjectOutputStream(bos);
                            writer.writeObject(cacheWithSameHashCode);
                            ZipArchiver.compress(bos.toByteArray(), directory, fileName, "ser");
                            CacheProxy.fileCacheCount.getAndIncrement();
                        } catch (IOException exc) {
                            System.out.println("Can not write object! " + exc.getMessage());
                        }
                    } else
                        try (FileOutputStream fos = new FileOutputStream(fileWithResult)) {
                            ObjectOutputStream writer = new ObjectOutputStream(fos);
                            writer.writeObject(cacheWithSameHashCode);
                            CacheProxy.fileCacheCount.getAndIncrement();
                        } catch (IOException exc) {
                            System.out.println("Can not write object! " + exc.getMessage());
                        }
            } finally {
                mutexes.get(fileNameFull).count--;
                emptyMutexes.add(fileNameFull);
            }

        }
        return returnResult;
    }

    private Object invokeFromCacheRAM(Method method, Object[] args, Cacheable cacheableAnnotation) {
        int[] indexes = cacheableAnnotation.indexes();
        ArgumentsObjs arg = Arrays.equals(indexes, new int[]{-1}) ? new ArgumentsObjs(args) :
                new ArgumentsObjs(args, indexes);
        String methFullName = getFullName(method);
        readerLock.lock();
        Map<ArgumentsObjs, Object> cacheMethod = cache.get(methFullName);
        readerLock.unlock();
        if (cacheMethod == null) {
            writerLock.lock();
            cache.putIfAbsent(methFullName, new HashMap<>());
            writerLock.unlock();
        }
        readerLock.lock();
        Object result = cache.get(methFullName).get(arg);
        readerLock.unlock();
        if (result != null) {
            //  System.out.println("Cache Res " + result + "    "+Thread.currentThread().getName());
            return result;
        }
        Object returnResult =
                getResultFromDelegate(delegate, method, args);
        Object cacheResult = List.class.isAssignableFrom(method.getReturnType()) ?
                getReducantList((List<?>) returnResult, cacheableAnnotation) : returnResult;
        writerLock.lock();
        try {
            if (!cache.get(methFullName).containsKey(arg)) {
                cache.get(methFullName).put(arg, cacheResult);
                CacheProxy.ramCacheCount.getAndIncrement();
                //System.out.println("New Res " + returnResult + "    "+Thread.currentThread().getName());
                return returnResult;
            } else {
                //  System.out.println("Cache Res " + returnResult + "    "+Thread.currentThread().getName());
                return cache.get(methFullName).get(arg);
            }
        } catch (Exception e) {
            return null;
        } finally {
            writerLock.unlock();
        }

    }

    private String getFullName(Method method) {
        Class<?>[] arrayParameterTypes = method.getParameterTypes();
        StringBuilder name = new StringBuilder(method.getName());
        for (Class<?> arrayParameterType : arrayParameterTypes)
            name.append("-").append(arrayParameterType.getName());
        return name.toString();
    }

    public List<?> getReducantList(List<?> list, Cacheable cachAnnotaton) {
        if (cachAnnotaton.maxLengthList() >= list.size())
            return (ArrayList.class.isAssignableFrom(list.getClass()) ||
                    LinkedList.class.isAssignableFrom(list.getClass())) ? list : new ArrayList<>(list);
        return new ArrayList<Object>(list.subList(0, cachAnnotaton.maxLengthList()));
    }

    private Object getResultFromDelegate(Object delegate, Method method, Object[] args) {
        method.setAccessible(true);
        try {
            return method.invoke(delegate, args);
        } catch (IllegalAccessException exc) {
            System.out.printf("There is a problem with access to method %s:  %s", method.getName(), exc.getMessage());
        } catch (InvocationTargetException exc) {
            System.out.printf("There is a problem in the method %s:  %s", method.getName(), exc.getMessage());
        } finally {
            method.setAccessible(false);
        }
        return null;
    }

    private void clearMutexes() {
        synchronized (mutexes) {
            emptyMutexes.forEach(s -> {
                synchronized (mutexes.get(s).mutex) {
                    if (mutexes.containsKey(s) && mutexes.get(s).count < 1) {
                        mutexes.remove(s);
                        emptyMutexes.remove(s);
                    }
                }
            });
        }

    }

    static class Pair {
        public Object mutex;
        public int count;

        Pair(Object o, int count) {
            this.mutex = o;
            this.count = count;
        }
    }
}