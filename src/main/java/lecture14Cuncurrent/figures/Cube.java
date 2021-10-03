package lecture14Cuncurrent.figures;

import java.io.Serializable;

public class Cube extends Parallelepiped implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Double inscribedBallRadius;
    public Cube(int edge) {
        super(edge, edge, edge);
        inscribedBallRadius = 0.5 * edge;
    }

    public Cube() {
        super();
    }
    public int getEdge(){
        return getEdge1();
    }

    Double computeInscribedBallRadius() {
        return inscribedBallRadius;
    }
    @Override
    public String toString(){
        return String.format("Cube: %d", getEdge());
    }

}
