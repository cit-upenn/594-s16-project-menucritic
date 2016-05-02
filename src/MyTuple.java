
/**
 * Class for storing two generic types, used for return multiple values from one method
 * @author gabecolton
 *
 * @param <T1> the type of the first return value
 * @param <T2> the type of the second return value
 */
public class MyTuple<T1, T2> {
	
	/**
	 * The first value
	 */
	public T1 first;
	/**
	 * The second value
	 */
	public T2 second; 
	
	/**
	 * Constructor for a Tuple, associates first and second fields with the first and second arguments
	 * @param first
	 * @param second
	 */
	public MyTuple(T1 first, T2 second){
		this.first = first;
		this.second = second; 
	}

}
