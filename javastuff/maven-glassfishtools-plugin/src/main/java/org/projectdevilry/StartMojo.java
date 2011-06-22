package org.projectdevilry;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.AbstractMojo;

import java.io.IOException;

/**
 * Start the glassfish server.
 * @goal start
 */
public class StartMojo extends Asadmin {
	

	public void execute() throws MojoExecutionException {
		getLog().info("Starting glassfish server." + glassfishHome);
		getLog().debug("glassfishHome: " + getGlassfishHome());
		getLog().debug("asadminPath: " + getAsadminPath());
		try {
			asadmin("--help");
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}