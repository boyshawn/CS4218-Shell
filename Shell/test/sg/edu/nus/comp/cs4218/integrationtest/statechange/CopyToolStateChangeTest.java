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

public class CopyToolStateChangeTest {

	// Static Variables
	private static File workingDir;
	private static File initialWorkingDir;
	private static File tempDir;
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
		
	}

	@After
	public void tearDown() throws Exception {
		// remove inputNew.txt
		File inputNew = new File(tempDir.toString() + File.separator
				+ "inputNew.txt");
		
		System.setProperty("user.dir", initialWorkingDir.toString());
		
		if (inputNew.exists()) {
			inputNew.delete();
		}
	}

	// Positive Testing

	@Test
	public void cdCopyLsWithPiping_FilesListed() {

		final String complexCmdStr = "cd \"" + tempDir.toString() + "\""
				+ "|copy \"" + input.toString() + "\" \"" + tempDir.toString()
				+ File.separator + "inputNew.txt\"" + "|ls";

		final Vector<String> finalResult = Shell
				.shellTestExecution(complexCmdStr);

		assertTrue(finalResult.get(0).contains("inputNew.txt"));

	}

	@Test
	public void cdCopyLsWithSequentialExecution_FilesListed() {

		final String cdCmdStr = "cd \"" + tempDir.toString() + "\"";
		final String copyCmdStr = "copy \"" + input.toString() + "\" \""
				+ tempDir.toString() + File.separator + "inputNew.txt\"";
		final String lsCmdStr = "ls";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr,
				copyCmdStr, lsCmdStr);

		assertTrue(finalResult.get(0).contains("inputNew.txt"));

	}

	// Negative Testing

	@Test
	public void cdCopyCdMoveWithPiping_FileNotFound() {

		final String complexCmdStr = "cd " + tempDir.getName() + "|copy \""
				+ input.toString() + "\" \"" + tempDir.toString()
				+ File.separator + "inputNew.txt\""
				+ "|cd ..|move inputNew.txt inputNew2.txt";

		final Vector<String> finalResult = Shell
				.shellTestExecution(complexCmdStr);

		assertTrue(finalResult.get(0).contains(
				"move: 'inputNew.txt': No such file or directory"));

	}

	@Test
	public void cdCopyCdMoveWithSequentialExecution_FileNotFound() {

		final String cdCmdStr = "cd " + tempDir.getName();
		final String copyCmdStr = "copy \"" + input.toString() + "\" \""
				+ tempDir.toString() + File.separator + "inputNew.txt\"";
		final String cdCmdRootStr = "cd ..";
		final String moveCmdStr = "move inputNew.txt inputNew.txt2";

		final Vector<String> finalResult = Shell.shellTestExecution(cdCmdStr,
				copyCmdStr, cdCmdRootStr, moveCmdStr);

		assertTrue(finalResult.get(0).contains(
				"move: 'inputNew.txt': No such file or directory"));

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
