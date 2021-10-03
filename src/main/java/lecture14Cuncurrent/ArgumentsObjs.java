package lecture14Cuncurrent;

import java.io.Serializable;

public class ArgumentsObjs implements Serializable {
   private final Object[] args;
   public ArgumentsObjs(Object[] args){
       this.args = args;
   }

    public ArgumentsObjs(Object[] args, int[] indexes){
        this.args = new Object[indexes.length];
        for (int i = 0; i < indexes.length; ++i) {
            this.args[i] = args[indexes[i]];
        }
    }

   @Override
    public boolean equals(Object other) {
       if (other == null) {
           return false;
       }
       if (other == this)
           return true;
       if (other.getClass() != this.getClass())
           return false;
       ArgumentsObjs otherArgs = (ArgumentsObjs) other;
       if (otherArgs.args.length != this.args.length)
           return false;
       for (int i = 0; i < args.length; ++i){
           if (!this.args[i].equals(otherArgs.args[i]))
               return false;
       }
       return true;
    }
    @Override
    public int hashCode() {
       return java.util.Arrays.hashCode(args);
    }
}
