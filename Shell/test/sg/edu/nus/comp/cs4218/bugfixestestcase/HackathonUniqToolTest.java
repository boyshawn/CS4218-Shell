package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.UniqTool;

public class HackathonUniqToolTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File file = new File("workingDir");
		if (file.exists()) {
			deleteFolder(file);
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Bug 8.2: Incorrect help file. 
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended2.UniqTool.java:24 (24 refers to line num)
	 */
	@Test
	public void getHelpTest() {
		UniqTool uniqTool = new UniqTool();
		String helpMsg = uniqTool.getHelp();
		assertTrue(helpMsg.contains("Usage: uniq"));
	}

	/**
	 * Bug 8.3: Uniq no options return wrong result.
	 * 
	 * Actually it returns the correct result. I reverted back to previous commit
	 * before receiving the Hackathon report and it works fine.
	 * 
	 * The reason why the other team finds out it's wrong is because they reverse
	 * the expected and actual argument in assertEquals() and they don't use system's
	 * new line character.
	 * 
	 * @throws IOException
	 */
	@Test
	public void executeGetUniqueTest() throws IOException {
		// Setup.
		File file = Files.createTempFile("tempFile", ".tmp").toFile();
		String input = " \nHello World\nhello World\nTest\ntest";
		Files.write(file.toPath(), input.getBytes(), StandardOpenOption.CREATE);
		UniqTool uniqtool = new UniqTool(
				new String[] { file.getAbsolutePath() });
		
		String result = uniqtool.execute(file.getParentFile(), null);
		assertEquals("Hello World"+System.lineSeparator()+"hello World"+System.lineSeparator()+"Test"+System.lineSeparator()+"test", result);
		
		Files.delete(file.toPath());
	}
	
	
	/**
	 * Bug 8.6: Uniq does not support path relative to working dir specified
	 * execute arg.
	 * Fixed in sg.edu.nus.comp.cs4218.impl.extended2.UniqTool.java:179-181
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionIgnoreCaseTest()
			throws IOException {
		// Create a temp working dir.
		File workingDir = new File("workingDir");  
		if (workingDir.exists()) {
			deleteFolder(workingDir);
		}
		workingDir.mkdir();
		
		// Create a test file in working dir.
		File testFile = new File(workingDir, "temp");
		String testInput = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
		Files.write(testFile.toPath(), testInput.getBytes(), StandardOpenOption.CREATE);
		
		String[] args = { "-f", "2", "-f", "3", "-f", "1", "temp" };
		UniqTool uniqTool = new UniqTool(args);
		
		String actual = uniqTool.execute(workingDir, null);
		assertEquals("Hello World"+System.lineSeparator()+"TEST", actual);
	}

	/**
	 * Bug 8.6: Uniq does not file name with space
	 * Actually it does.
	 */
	@Test
	public void executeGetUniqueSkipNumMultipleSameOptionTest() 
			throws IOException {
		// Create a temp working dir.
		File workingDir = new File("workingDir");  
		if (workingDir.exists()) {
			deleteFolder(workingDir);
		}
		workingDir.mkdir();
		
		// Create a test file in working dir.
		File testFile = new File(workingDir, "temp name");
		String testInput = " \nHello World\nhello World\nTEST\ntest\njest\nBEST";
		Files.write(testFile.toPath(), testInput.getBytes(), StandardOpenOption.CREATE);
		
		String[] args = { "-f", "2", "-f", "3", "-f", "1", "temp name" };
		UniqTool uniqTool = new UniqTool(args);
		
		String actual = uniqTool.execute(workingDir, null);
		assertEquals("Hello World"+System.lineSeparator()+"TEST", actual);
	}
	
	// Helper functions
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
