package org.devilry;

import com.bm.testsuite.BaseSessionBeanFixture;


public class HelloBeanTest extends BaseSessionBeanFixture<HelloBean2> {
	private static final Class<?>[] USED_ENTITIES = { HelloBean2.class, HelloTranslations.class };

	public HelloBeanTest() {
		super(HelloBean2.class, USED_ENTITIES);
	}


	/**
	 * Testmethod.
	 */
	public void testBaunWithPreloadedData() {
		HelloBean2 toTest = this.getBeanToTest();
		toTest.addHelloworld("nb", "Hei verden");
		System.out.println(toTest.getHelloworld("nb"));
	}
}
