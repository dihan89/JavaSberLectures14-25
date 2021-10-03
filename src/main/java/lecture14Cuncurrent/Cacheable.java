package lecture14Cuncurrent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  saveToFile is false if result saved to RAM and true if result saved to file
 *  indexes is array of field numbers,  that are important for result.
 *  Value {-1} is default value, that mean, that all fields are important.
 *  maxLengthList is max Length for cache from result (the first maxLengthList elements from results,
 *  if returnResult implements interfaceList;
 *  zip is true, if necessary to save into zip-archive and is false in otherwise.
 *  folderName is name for the folder of cache for the method
 *  (default it is the name of the method, and also it's  types of parameters).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    boolean saveTofile() default false;
    int[] indexes() default {-1};
    int maxLengthList() default Integer.MAX_VALUE;
    String folderNameCache() default "";
    boolean zip() default false;
}
