package lecture14CuncurrentTest;

import lecture14Cuncurrent.*;
import lecture14Cuncurrent.figures.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Lect14TestWithoutThreads {

    HardWorker worker;

    @BeforeEach
    public void prepare() {
        HardWorkerImpl workerDelegate = new HardWorkerImpl();
        worker = (HardWorker) CacheProxy.cache(workerDelegate);
    }

    @Test
    public void testCube() {
        System.out.println(worker.createCube(7));
        System.out.println(worker.createCube(11));
        System.out.println(worker.createCube(7));
        System.out.println(worker.createCube(14));
        System.out.println(worker.createCube(10));
        System.out.println(worker.createCube(10));
    }

    @Test
    public void testParallelepiped() {
        System.out.println(worker.createParallelepiped(1,2,3));
        System.out.println(worker.createParallelepiped(11,22,33));
        System.out.println(worker.createParallelepiped(1,2,3));
        System.out.println(worker.createParallelepiped(1,2,3));
        System.out.println(worker.createParallelepiped(7,8,9));
        System.out.println(worker.createParallelepiped(8,8,8));
    }

    @Test
    public void testParallelepiped2() {
        System.out.println(worker.createParallelepiped2(1,2,3));
        System.out.println(worker.createParallelepiped2(11,22,33));
        System.out.println(worker.createParallelepiped2(1,2,3));
        System.out.println(worker.createParallelepiped2(1,6,3));
        System.out.println(worker.createParallelepiped2(11,22,17));
        System.out.println(worker.createParallelepiped2(8,8,8));
    }

    @Test
    public void testParallelepiped3withRenamedFolder() {
        System.out.println(worker.createParallelepiped3(1,2,3));
        System.out.println(worker.createParallelepiped3(11,22,33));
        System.out.println(worker.createParallelepiped3(1,2,3));
        System.out.println(worker.createParallelepiped3(1,6,3));
        System.out.println(worker.createParallelepiped3(11,22,17));
        System.out.println(worker.createParallelepiped3(8,8,8));
    }

    private void printList(List<?> list){
        for (var elem : list){
            System.out.print(elem+" ");
        }
        System.out.println();
    }

    @Test
    public void testCreateCubes() {
        List<Cube> cubes1 = worker.createCubes(new ArrayList<>(Arrays.asList(1,7,3,5,6)));
        printList(cubes1);
        List<Cube> cubes2 = worker.createCubes(new ArrayList<>(Arrays.asList(1,7,8,5,6)));
        printList(cubes2);
        List<Cube> cubes3 = worker.createCubes(new ArrayList<>(Arrays.asList(1,7,3,5,9, 10)));
        printList(cubes3);
        List<Cube> cubes4 = worker.createCubes(new ArrayList<>(Arrays.asList(1,7,8,5,6)));
        printList(cubes4);
        List<Cube> cubes5 = worker.createCubes(new ArrayList<>(Arrays.asList(2,7,3,5,6)));
        printList(cubes5);

    }

    @Test
    public void testParallelepipedZip() {
        System.out.println(worker.createParallelepipedZip(1,2,3));
        System.out.println(worker.createParallelepipedZip(11,22,33));
        System.out.println(worker.createParallelepipedZip(1,2,3));
        System.out.println(worker.createParallelepipedZip(1,2,3));
        System.out.println(worker.createParallelepipedZip(7,8,9));
        System.out.println(worker.createParallelepipedZip(8,8,8));
    }
}


