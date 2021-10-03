package lecture14CuncurrentTest;
import lecture14Cuncurrent.figures.Cube;
import lecture14Cuncurrent.figures.Parallelepiped;

import java.util.ArrayList;
import java.util.List;

public class HardWorkerImpl implements HardWorker {
    public Cube createCube(int edge){
        return new Cube(edge);
    }
    public Parallelepiped createParallelepiped(int edge1, int edge2, int edge3){
        return new Parallelepiped(edge1, edge2, edge3);
    }
    public Parallelepiped createParallelepiped2(int edge1, int edge2, int edge3){
        return new Parallelepiped(edge1, edge2, edge3);
    }

    public Parallelepiped createParallelepiped3(int edge1, int edge2, int edge3){
        return new Parallelepiped(edge1, edge2, edge3);
    }

    public ArrayList<Cube> createCubes(List<Integer> edges){
        ArrayList<Cube> cubes = new ArrayList<>(edges.size());
        for (int edge : edges)
            cubes.add(new Cube(edge));
        return cubes;
    }
    public Parallelepiped createParallelepipedZip(int edge1, int edge2, int edge3){
        return new Parallelepiped(edge1, edge2, edge3);
    }
}
