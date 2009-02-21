package sandbox;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


class Demo {
	public static void main(String[] args) {
		BeanFactory beanFactory = new ClassPathXmlApplicationContext(
				"classpath*:/META-INF/beans.xml");

		// Gets a bean from the Spring bean context
		UserSystem s = (UserSystem) beanFactory.getBean("usersystem");
		s.addUser("Spiderman");
		boolean b = s.hasUser("Spiderman");
		System.out.printf("Has user Spiderman? %s%n", b);
	}
}
