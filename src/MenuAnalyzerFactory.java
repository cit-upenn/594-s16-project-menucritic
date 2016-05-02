import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Class that will provide MenuAnalyzer objects to an Object Pool for use in multithreading.
 * MenuAnalyzer object initialization is time intensive. By having a pool of objects, we can 
 * control how many are generated and recycle ones that are already created
 * @author gabecolton
 *
 */
public class MenuAnalyzerFactory extends BasePooledObjectFactory<MenuAnalyzer>{

	@Override
	public MenuAnalyzer create() throws Exception {
		return new MenuAnalyzer();
	}

	@Override
	public PooledObject<MenuAnalyzer> wrap(MenuAnalyzer ma) {
		return new DefaultPooledObject<MenuAnalyzer>(ma);
	}

}
