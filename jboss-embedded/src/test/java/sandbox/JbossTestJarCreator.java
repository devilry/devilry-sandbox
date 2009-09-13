package sandbox;

import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;

import java.io.File;

class JbossTestJarCreator extends JbossJarCreator {

	public JbossTestJarCreator(AssembledDirectory jar, File rootDir)
			throws ClassNotFoundException {
		super(jar, rootDir);
		ignorePaths("conf/", "deploy/", "deployers/", "data/",
				"jndi.properties", "sandbox/", "stylesheets/",
				"tmp/", "META-INF/persistence.properties");
	}

	protected void addClass(File file, String path) {
	}
}
