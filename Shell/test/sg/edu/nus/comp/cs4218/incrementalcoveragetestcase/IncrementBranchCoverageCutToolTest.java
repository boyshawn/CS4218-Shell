package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

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

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;

public class IncrementBranchCoverageCutToolTest {

	private ICutTool cutTool;
	private static File workingDir;
	private static File tempDir;
	private static File inputNoDelimiter;
	private static File inputWithDelimiter;
	private static String inputNoDelimiterStr;
	private static String inputWithDelimiterStr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));

		/* Creation of directories and files for testing purposes */
		tempDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "tempDir")
						.toPath()).toString());
		inputNoDelimiter = new File(tempDir.toString() + File.separator
				+ "inputNoDelimiter");
		inputNoDelimiter.createNewFile();

		inputWithDelimiter = new File(tempDir.toString() + File.separator
				+ "inputWithDelimiter");
		inputWithDelimiter.createNewFile();

		/* Writing of content into files */
		inputNoDelimiterStr = "012345678901234567890" + System.lineSeparator()
				+ "123456789 0123456789";

		inputWithDelimiterStr = "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789;0123456789;0123456789"
				+ System.lineSeparator() + "0123456789 0123456789;0123456789";

		Files.write(inputNoDelimiter.toPath(), inputNoDelimiterStr.getBytes(),
				StandardOpenOption.APPEND);
		Files.write(inputWithDelimiter.toPath(),
				inputWithDelimiterStr.getBytes(), StandardOpenOption.APPEND);

	}

	@Before
	public void setUp() {
		cutTool = new CutTool(null);
	}

	@After
	public void tearDown() {
		cutTool = null;
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		/* Delete all temporary testing files */
		Files.delete(inputNoDelimiter.toPath());
		Files.delete(inputWithDelimiter.toPath());
		Files.delete(tempDir.toPath());

		/* Setting all file objects to null */
		workingDir = null;
		tempDir = null;
		inputNoDelimiter = null;
		inputWithDelimiter = null;

		/* Setting all file object Strings to null */
		inputNoDelimiterStr = null;
		inputWithDelimiterStr = null;
	}

	@Test
	public void cutSpecifiedCharacters_Stdin() {

		final String[] arguments = { "-c", "1-3,7-20,3-5,6,21,19" };
		cutTool = new CutTool(arguments);

		final String returnMessage = cutTool.execute(workingDir,
				inputNoDelimiterStr);

		final String expectedMessage = "012345678901234567890"
				+ System.lineSeparator() + "123456789 0123456789";

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}

	@Test
	public void cutSpecifiedCharactersWithDelimiter_Stdin() {

		final String[] arguments = { "-d", "\";\"", "-f", "1" };
		cutTool = new CutTool(arguments);

		final String returnMessage = cutTool.execute(workingDir,
				inputWithDelimiterStr);

		final String expectedMessage = "0123456789" + System.lineSeparator()
				+ "0123456789" + System.lineSeparator()
				+ "0123456789 0123456789";

		assertEquals(expectedMessage, returnMessage);
		assertEquals(0, cutTool.getStatusCode());

	}
}
