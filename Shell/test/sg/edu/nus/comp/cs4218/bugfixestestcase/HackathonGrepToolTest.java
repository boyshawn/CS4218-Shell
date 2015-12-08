package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;

public class HackathonGrepToolTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	private File testFile;
	private File testDir;
	private GrepTool grepTool;

	@Before
	public void before() throws IOException {
		testDir = new File("testCutFolder");
        testDir.mkdir();
        
        // create new file with text
        testFile = new File(testDir, "test.txt");
        testFile.createNewFile();
        FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuilder sb = new StringBuilder();
        sb.append("beforeBeforeBeforeApp" + System.lineSeparator());
		sb.append("beforeBeforeApp" + System.lineSeparator());
		sb.append("beforeApp" + System.lineSeparator());
		sb.append("apple" + System.lineSeparator());
		sb.append("mid" + System.lineSeparator());
		sb.append("apples" + System.lineSeparator());
		sb.append("afterApp" + System.lineSeparator());
		sb.append("afterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterApp" + System.lineSeparator());
		sb.append("afterAfterAfterAfterApp" + System.lineSeparator());
		bw.write(sb.toString());
        bw.close();
	}

	@After
	public void after() {
		grepTool = null;
		
		// delete all files in directory
        File[] fileList = testDir.listFiles();
        for (int i=0; i<fileList.length; i++){
            File file = fileList[i];
            file.delete();
        }
        // delete directory
        testDir.delete();
        // garbage collector swoop in right about here
        grepTool = null;
	}
	
	/**
	 * Bug 11.1: Grep should handle exception thrown when no fileName.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended1.GrepTool.java:286-289
	 */
	@Test
	public void executeFileNameMissingTest(){
		grepTool = new GrepTool(new String[]{"-A", "2", "apple"});
		assertEquals(GrepTool.ERR_MSG_INVALID_ARG, grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Bug 11.2: Grep count line should handle null pattern value.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended1.GrepTool.java:443-445
	 */
	@Test
	public void getCountOfMatchingLinesWithNullPatternTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(0, grepTool.getCountOfMatchingLines(null, "abc\ndef"));
		assertEquals(0, grepTool.getStatusCode());
	}
	
	/**
	 * Bug 11.3: Grep -A, -B and -C should handle -ve number.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended1.GrepTool.java:277, 311, 342
	 * @throws IOException
	 */
	@Test
	public void executeNegativeNumberATest() throws IOException{
        grepTool = new GrepTool(new String[]{"-A", "-2", "a.", testFile.getAbsolutePath()});
		assertEquals("grep: -2: invalid context length argument", grepTool.execute(testDir, null));
		assertEquals(1, grepTool.getStatusCode());
	}
	
	/**
	 * Bug 11.4: Grep count line should handle null input.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended1.GrepTool.java:443
	 */
	@Test
	public void getCountOfMatchingLinesWithNullInputTest() {
		grepTool = new GrepTool(new String[0]);
		assertEquals(0, grepTool.getCountOfMatchingLines("", null));
		assertEquals(0, grepTool.getStatusCode());
	}
}
