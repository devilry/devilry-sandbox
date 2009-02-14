package sandbox;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


public class DemoTest {
	Demo d;
	User bender;
	User fry;
	
	@BeforeMethod
	public void setUp() {
		d = new Demo("test");
		bender = d.add("Bender");
		fry = d.add("Fry");
	}
	
	@AfterMethod
	public void tearDown() {
		d.close();
	}

	@Test
	public void getTest() {
		User u = d.get(bender.getId());
		assertEquals(u.getName(), bender.getName());

		u = d.get(fry.getId());
		assertEquals(u.getName(), fry.getName());
	}

	@Test
	public void queryUserTest() {
		User u = d.queryUser("Bender");
		assertEquals(u.getName(), "Bender");
	}
}