package org.projectdevilry;

import org.apache.maven.plugin.AbstractMojo;

import java.io.File;
import java.io.IOException;


abstract class Asadmin extends AbstractMojo {


	/**
	 * Path to glassfish.
	 * @parameter
	 */
	protected String glassfishHome = null;

	/**
	 * Path to asadmin executable.
	 * @parameter
	 */
	protected String asadminPath = null;

	/**
	 * Should we send asadmin output to INFO? If not, it is sent to DEBUG.
	 * @parameter
	 */
	protected boolean outputToDebug = false;


	public void exec(String cmd) throws IOException {
		Process p = Runtime.getRuntime().exec(cmd);
		java.util.Scanner s = new java.util.Scanner(p.getInputStream());
		while (s.hasNextLine()) {
			if(outputToDebug)
				getLog().debug(s.nextLine());
			else
				getLog().info(s.nextLine());
		}
	}

	public void asadmin(String format, String... args) throws IOException {
		exec(getAsadminPath() + " " + String.format(format, args));
	}

	public File getGlassfishHome() {
		return new File(glassfishHome);
	}

	public String getAsadminPath() {
		if(asadminPath == null) {
			return getGlassfishHome().toString() + File.separator + "bin" +
					File.separator + "asadmin";
		} else {
			return asadminPath;
		}
	}

}

