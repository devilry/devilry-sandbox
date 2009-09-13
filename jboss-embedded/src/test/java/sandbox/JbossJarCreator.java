package sandbox;

import org.jboss.virtual.plugins.context.vfs.AssembledDirectory;
import org.jboss.virtual.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

class JbossJarCreator {
	private AssembledDirectory jar;
	private File rootDir;
	protected Set<String> ignore;

	public JbossJarCreator(AssembledDirectory jar, File rootDir)
			throws ClassNotFoundException {
		this.jar = jar;
		this.rootDir = rootDir;
		ignore = new HashSet<String>();
	}

	public JbossJarCreator ignorePaths(String... ignorePaths) {
		for (String p : ignorePaths) {
			ignore.add(p);
		}
		return this;
	}

	public void addToJar() throws ClassNotFoundException {
		addToJar(rootDir, jar, "");
	}

	protected void addToJar(File curFsDir,
							AssembledDirectory curAssembledDir, String path)
			throws ClassNotFoundException {

		for (File child : curFsDir.listFiles()) {
			String childPath = path + child.getName();
			if (ignore.contains(childPath)) {
				continue;
			}

			if (child.isDirectory()) {
				childPath += "/";
				if (!ignore.contains(childPath)) {
					addDirectory(curAssembledDir, child, childPath);
				}
			} else if (child.getName().endsWith(".class")) {
				String javapath = path.replace("/", ".") +
						child.getName().replace(".class", "");
				if (!ignore.contains(javapath)) {
					addClass(child, javapath);
				}
			} else {
				addFile(child, childPath, curAssembledDir);
			}
		}
	}

	protected void addDirectory(AssembledDirectory curAssembledDir, File dir,
								String path)
			throws ClassNotFoundException {
		addToJar(dir, curAssembledDir.mkdir(dir.getName()),
				path);
	}

	protected void addClass(File file, String javapath)
			throws ClassNotFoundException {
		ClassLoader l = ClassLoader.getSystemClassLoader();
		Class c = l.loadClass(javapath);
		jar.addClass(c);
	}

	protected void addFile(File file, String path,
						   AssembledDirectory curAssembledDir) {
		curAssembledDir.addResource(path.replace("/", File.separator));
	}


	/**
	 * Print the contents of a VirtualFile. For debugging:).
	 */
	private static void printVirtualFile(VirtualFile d, String prefix) {
		try {
			for (VirtualFile f : d.getChildren()) {
				if (f.isLeaf()) {
					System.out.println(prefix + f.getName());
				} else {
					printVirtualFile(f, prefix + f.getName() + "/");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print the contents of the jar file. For debugging:).
	 */
	public static void printJar(AssembledDirectory j) {
		System.out.printf("Contents of %s:%n", j.getName());
		printVirtualFile(j, "   ");
	}
}
