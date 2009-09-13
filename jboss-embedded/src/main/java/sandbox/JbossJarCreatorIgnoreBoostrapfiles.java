package sandbox;

import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;

import java.io.File;

class JbossJarCreatorIgnoreBoostrapfiles extends JbossJarCreator {

	public JbossJarCreatorIgnoreBoostrapfiles(AssembledDirectory jar, File rootDir)
			throws ClassNotFoundException {
		super(jar, rootDir);
		addIgnore("conf/", "deploy/", "deployers/", "data/",
				"jndi.properties", "sandbox/", "stylesheets/",
				"tmp/", "META-INF/persistence.properties",
				"META-INF/MANIFEST.MF" // MANIFEST.MF seems to be fatal not to ignore.
		);
	}

	protected void addClass(File file, String path) {
	}
}
