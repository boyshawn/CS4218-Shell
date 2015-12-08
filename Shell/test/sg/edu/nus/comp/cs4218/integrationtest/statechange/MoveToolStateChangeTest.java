package sg.edu.nus.comp.cs4218.integrationtest.statechange;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.Shell;

public class MoveToolStateChangeTest {

	// Static Variables
	private static File workingDir;
	private static File tempDir;
	private static File initialWorkingDir;
	private static File input;
	private static String inputStr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		workingDir = new File(System.getProperty("user.dir"));
		initialWorkingDir = new File(System.getProperty("user.dir"));
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "tempDir")
						.toPath()).toString());

		input = new File(tempDir.toString() + File.separator + "input.txt");
		input.createNewFile();

		inputStr = "0123456789012345678901234567890123456789"
				+ System.lineSeparator()
				+ "0123456789012345678901234567890123456789"
				+ System.lineSeparator()
				+ "0123456789 0123456789 0123456789 0123456789";

		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		deleteFolder(tempDir);
		workingDir = null;

	}

	@Before
	public void setUp() throws Exception {
		input.createNewFile();
		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);		
	}

	@After
	public void tearDown() throws Exception {
		// remove inputNew.txt
		File inputNew = new File(workingDir.toString() + File.separator
				+ "inputNew.txt");

		if (inputNew.exists()) {
			inputNew.delete();
		}
		
		System.setProperty("user.dir", initialWorkingDir.toString());
	}

	// Positive Testing
	@Test
	public void cdMoveLsWithPiping_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";

		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew.txt\"";

		final String lsCmdStr = "ls";

		final String complexCmdStr = cdCmdStr + "|" + moveCmdStr + "|"
				+ lsCmdStr;

		final Vector<String> finalResult = Shell
				.shellTestExecution(complexCmdStr);
		assertTrue(finalResult.get(0).contains(""));

	}

	@Test
	public void cdMoveLsWithSequentialExecution_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";

		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew.txt\"";

		final String lsCmdStr = "ls";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr,
				moveCmdStr, lsCmdStr);
		assertTrue(finalResult.get(0).contains(""));
	}

	// Negative Testing

	@Test
	public void lsMoveDeleteWithPiping_FileNotFound() {

		final String lsCmdStr = "ls";
		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew.txt\"";
		final String deleteCmdStr = "delete " + tempDir.getName()
				+ File.separator + "inputNew.txt";

		final String complexCmdStr = lsCmdStr + "|" + moveCmdStr + "|"
				+ deleteCmdStr;

		final Vector<String> finalResult = Shell
				.shellTestExecution(complexCmdStr);

		final String expectedMessage = "delete: tempDir" + File.separator
				+ "inputNew.txt: No such file or directory";

		assertTrue(finalResult.get(0).contains(expectedMessage));

	}

	@Test
	public void lsMoveDeleteWithSequentialExecution_FileNotFound() {

		final String lsCmdStr = "ls";
		final String moveCmdStr = "move \"" + input.toString() + "\" " + "\""
				+ workingDir.toString() + File.separator + "inputNew.txt\"";

		final String deleteCmdStr = "delete " + tempDir.getName()
				+ File.separator + "inputNew.txt";

		final Vector<String> finalResult = Shell.shellTestExecution(lsCmdStr,
				moveCmdStr, deleteCmdStr);

		final String expectedMessage = "delete: tempDir" + File.separator
				+ "inputNew.txt: No such file or directory";
		assertTrue(finalResult.get(0).contains(expectedMessage));

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
