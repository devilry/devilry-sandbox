/** Tests the MyMath class using Junit4.
 * 
 * To keep the tests very simple, we only use one type of
 * assertion: assertEquals(). And we only put tests in the
 * class even though we could have used lots more advanced
 * Junit4 features.
 * */
package org.devilry;
import org.testng.annotations.*;
import static org.testng.Assert.*;


/**
 * @author Espen Angell Kristiansen
 */
public class MyMathTest {

	@Test public void sum(){   // the @Test decorator marks this as a test 
		assertEquals(MyMath.sum(2, 2), 4);
		assertEquals(MyMath.sum(2.4, 2.3), 4.7, 0.1); // the third argument is the allowed error margin.
	}

	@Test public void div(){
		assertEquals(MyMath.div(4, 2), 2);
		assertEquals(MyMath.div(3, 2), 1);

		assertEquals(MyMath.div(4, 2), 2);
		assertEquals(MyMath.div(3.0, 2.0), 1.5, 0);
	}
}
