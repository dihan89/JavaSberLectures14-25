package lecture14CuncurrentTest;

import lecture14Cuncurrent.Cacheable;
import lecture14Cuncurrent.figures.Cube;
import lecture14Cuncurrent.figures.Parallelepiped;

import java.util.List;

public interface HardWorker {
    @Cacheable(saveTofile = true)
    Cube createCube(int edge);

    @Cacheable
    Parallelepiped createParallelepiped(int edge1, int edge2, int edge3);

    @Cacheable(saveTofile = true, indexes = {0,2})
    Parallelepiped createParallelepiped2(int edge1, int edge2, int edge3);

    @Cacheable(saveTofile = true, indexes = {0,2},  folderNameCache = "ab-create3")
    Parallelepiped createParallelepiped3(int edge1, int edge2, int edge3);

    @Cacheable(saveTofile = true, maxLengthList = 3)
    List<Cube> createCubes(List<Integer> edges);

    @Cacheable(saveTofile = true, zip = true)
    Parallelepiped createParallelepipedZip(int edge1, int edge2, int edge3);
}
