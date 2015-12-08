/**
 * 
 */
package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CdTool;

/**
 * @author Francis Pang
 *
 */
public class IncrementBranchCoverageCdToolTest {
	private final String HOME_DIRECTORY = "user.home";
	private final String USER_DIRECTORY = "user.dir";
	private ICdTool cdTool;
	private String defaultWorkingDirectory;
	private File workingDirectory;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		defaultWorkingDirectory = System.getProperty(USER_DIRECTORY);
		
		cdTool= new CdTool(null);
		workingDirectory = new File(defaultWorkingDirectory);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		cdTool = null;
		workingDirectory = null;
		System.setProperty(USER_DIRECTORY, defaultWorkingDirectory);
	}
	
	@Test
	public void execute_NavigateToFile_DirectoryError() {
		File directory = new File(defaultWorkingDirectory);
		final String[] argument = { directory.getAbsolutePath()
				+ File.separator + ".project" };

		cdTool = new CdTool(argument);
		cdTool.execute(workingDirectory, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}
	
	@Test 
	public void execute_NullWorkingDirectory_DirectoryError() {
		cdTool.execute(null, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}
	
	@Test
	public void execute_InvalidWorkingDirectory_DirectoryError(){
		File directory = new File(HOME_DIRECTORY + File.separator + "cadcadc");
		cdTool.execute(directory, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}
	
	@Test
	public void execut_WorkingDirectoryAsFile_DirectoryError(){
		File directory = new File(workingDirectory + File.separator
				+ ".gitignore");
	
		cdTool.execute(directory, null);

		assertNotEquals(0, cdTool.getStatusCode());
	}

}
