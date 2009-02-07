package $org.devilry;

import org.testng.annotations.*;
import static org.testng.Assert.*;


public class AppTest {
	String s;

	@BeforeMethod
	public void setUp() {
		s = "Hello World";
	}

	@Test
	public void substring() {
		s = s.substring(6);
		assertEquals(s, "World");
	}

	@Test
	public void toLowerCase() {
		s = s.toLowerCase();
		assertEquals(s, "hello world");
	}
}
