package lecture14Cuncurrent.figures;
import java.io.Serializable;
import java.util.Objects;


public class Parallelepiped implements Serializable {
    private static final long serialVersionUID = 1L;
    private int edge1;
    private int edge2;
    private int edge3;
    public static int num = 0;

    public int computeVolume() {
        return getEdge1() * getEdge2() * getEdge3();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj == this) return
                true;
        if (obj.getClass() != this.getClass())
            return false;
        Parallelepiped other = (Parallelepiped) obj;
        return edge1 == other.getEdge1() && edge2 == other.edge2 && edge3 == other.getEdge3();
    }
     @Override
     public int hashCode(){
         return Objects.hash(edge1, edge2, edge3);
     }

    public Parallelepiped(int edge1, int edge2, int edge3) {
        this.edge1 = edge1;
        this.edge2 = edge2;
        this.edge3 = edge3;

    }

    public Parallelepiped() {
        this.edge1 = 0;
        this.edge2 = 0;
        this.edge3 = 0;
    }

    public int getEdge1() {
        return edge1;
    }

    public int getEdge2() {
        return edge2;
    }

    public int getEdge3() {
        return edge3;
    }

    @Override
    public String toString(){
        return String.format("Parallelepiped: %d;  %d;  %d", edge1, edge2, edge3);
    }

}
