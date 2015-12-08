package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.IPasteTool;
import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;

public class HackathonPasteToolTest {
	private IPasteTool pastetool; 
	private File testDir;
	
	@Before
	public void setUp() throws Exception {
		testDir = new File("testPasteFolder");
		testDir.mkdir();
	}

	@After
	public void tearDown() throws Exception {
		// delete all files in directory
		File[] fileList = testDir.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			file.delete();
		}
		// delete directory
		testDir.delete();
		pastetool = null;
	}

	/**
	 * Bug 10.1: Paste does not handle path relative to workingDir arg in execute().
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended2.PasteTool.java:152-154
	 * @throws IOException
	 */
	@Test
	public void pasteRelativeFilePathTest() throws IOException {
		File file = new File(testDir, "test.txt");
		file.createNewFile();
		
		pastetool = new PasteTool(new String[]{"-s", "test.txt", "test.txt"});
		String expectedOutput = "";
		assertEquals(expectedOutput, pastetool.execute(testDir, null));
		
		file.delete();
		pastetool = null;
	}
	
	/**
	 * Bug 10.2: No parameter and no stdin should return error message.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended2.PasteTool.java:167-170
	 */
	@Test
	public void executeNoParamTest() {
		pastetool = new PasteTool(new String[] {});
		assertEquals("paste: Missing parameter.", pastetool.execute(testDir, null));
		assertEquals(5, pastetool.getStatusCode());
		pastetool = null;
	}
}
