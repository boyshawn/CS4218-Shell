package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.DeleteTool;

public class IncrementBranchCoverageDeleteToolTest {

IDeleteTool tool;
	
	@Before
	public void setUp() throws Exception {
		String[] args = new String[] {"testdir"};
		tool = new DeleteTool(args);
	}

	@After
	public void tearDown() throws Exception {
		tool = null;
	}
	
	@Test
	public void execute_NullArgs_GetStatusCode1() {
		tool = new DeleteTool(null);
		
		File workingDir = new File(System.getProperty("user.dir"));
		tool.execute(workingDir, null);
		assertEquals(DeleteTool.STATUS_CODE_INVALID_ARGUMENTS, tool.getStatusCode());
	}
	
	@Test
	public void execute_EmptyArg_GetStatusCode1() {
		String[] args = new String[] {"testdir1", "testdir2", " "};
		tool = new DeleteTool(args);
		
		File testDir1 = new File("testdir1");
		testDir1.mkdir();
		File testDir2 = new File("testdir2");
		testDir2.mkdir();
		
		File workingDir = new File(System.getProperty("user.dir"));
		tool.execute(workingDir, null);
		assertEquals(DeleteTool.STATUS_CODE_INVALID_ARGUMENTS, tool.getStatusCode());
		assertFalse(testDir1.exists());
		assertFalse(testDir2.exists());
	}

}
