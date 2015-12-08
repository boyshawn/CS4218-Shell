/**
 * Assumption(s) Made: 
 * The Copy command only supports the following functions:
 * 
 * 1. Copy file1 destination file2
 * 2. Copy file1 into directory
 * 3. Copy multiple files into directory
 *
 * These functions are tested in the test cases below.
 * 
 */
package sg.edu.nus.comp.cs4218.impl.fileutils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;

public class CopyToolTest {
	// Variables
	private ICopyTool copyTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;
	private File fileToCopy;
	private String initialWorkingDirStr;
	private String fileContent;

	@Before
	public void setUp() throws Exception {
		copyTool = new CopyTool(null);

		initialWorkingDirStr = System.getProperty("user.dir");
		workingDir = new File(initialWorkingDirStr);

		sourceDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "sourceDir")
						.toPath()).toString());
		destDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "destDir")
						.toPath()).toString());

		fileToCopy = new File(sourceDir.toString() + File.separator
				+ "fileToCopy.txt");
		fileToCopy.createNewFile();

		fileContent = "1111111";
		Files.write(fileToCopy.toPath(), fileContent.getBytes(),
				StandardOpenOption.APPEND);

	}

	@After
	public void tearDown() throws Exception {
		copyTool = null;
		deleteFolder(sourceDir);
		deleteFolder(destDir);

		workingDir = null;
		sourceDir = null;
		destDir = null;

		System.setProperty("user.dir", initialWorkingDirStr);
	}

	// Black Box Positive Testing
	@Test
	public void copy_FileToFileTest() throws IOException {

		boolean fileCopied = copyTool
				.copy(fileToCopy, new File(destDir.toString() + File.separator
						+ "fileCopied.txt"));

		assertTrue(fileCopied == true);
		assertEquals(0, copyTool.getStatusCode());
	}

	@Test
	public void copy_FileToDirectoryTest() throws IOException {

		boolean fileCopied = copyTool.copy(fileToCopy, destDir);

		assertTrue(fileCopied == true);
		assertEquals(0, copyTool.getStatusCode());

	}

	@Test
	public void execute_FileToFileTest() throws IOException {

		String[] args = { fileToCopy.toString(),
				destDir.toString() + File.separator + "filedCopied.txt" };

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);
		assertEquals(0, copyTool.getStatusCode());
		assertEquals("", returnMessage);

	}

	@Test
	public void execute_SingleFileToDirectoryTest() throws IOException {

		String[] args = { fileToCopy.toString(), destDir.toString() };

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(0, copyTool.getStatusCode());
		assertEquals("", returnMessage);

	}

	@Test
	public void execute_MultipleFilesToDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = new File(sourceDir.toString() + File.separator
					+ "fileToCopy_" + i + ".txt");
			aFile.createNewFile();
			Files.write(aFile.toPath(), fileContent.getBytes(),
					StandardOpenOption.APPEND);

			args[i] = aFile.toString();
		}

		args[args.length - 1] = destDir.toString();

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(0, copyTool.getStatusCode());
		assertEquals("", returnMessage);

	}

	// Black Box Negative Testing

	@Test
	public void copy_FileNotFoundTest() throws IOException {

		File from = new File(sourceDir.toString() + "/fileNotFound.txt");
		File to = new File(destDir.toString());

		boolean fileCopied = copyTool.copy(from, to);

		assertFalse(fileCopied == true);
		assertEquals(1, copyTool.getStatusCode());
	}

	@Test
	public void copy_FileAlreadyExistsTest() throws IOException {

		File from = new File(sourceDir.toString() + File.separator
				+ "fileAlreadyExistsSource.txt");

		from.createNewFile();

		boolean fileCopied = copyTool.copy(from, from);

		assertFalse(fileCopied == true);
		assertEquals(2, copyTool.getStatusCode());

	}

	@Test
	public void execute_NoArgumentsTest() throws IOException {

		String[] args = {};

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(-1, copyTool.getStatusCode());
		assertEquals("copy: missing file operand", returnMessage);

	}

	@Test
	public void execute_SingleArgumentTest() throws IOException {

		String[] args = { fileToCopy.toString() };

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(-2, copyTool.getStatusCode());
		assertEquals(returnMessage,
				"copy: missing destination file operand after '" + args[0]
						+ "'");

	}

	@Test
	public void execute_SameArgumentsTest() throws IOException {

		String[] args = { fileToCopy.toString(), fileToCopy.toString() };

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(-3, copyTool.getStatusCode());
		assertEquals(returnMessage, "copy: '" + args[0] + "' and '" + args[1]
				+ "' are the same file");

	}

	// Black Box Negative Testing

	@Test
	public void execute_FileNotFoundTest() throws IOException {

		String[] args = {
				sourceDir.toString() + File.separator + "fileNotFound.txt",
				destDir.toString() };

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(1, copyTool.getStatusCode());
		assertEquals(returnMessage,
				"copy: 'fileNotFound.txt': No such file or directory");

	}

	@Test
	public void execute_FileAlreadyExistsTest() throws IOException {

		String[] tempArgs = { fileToCopy.toString(), destDir.toString() };

		copyTool = new CopyTool(tempArgs);
		copyTool.execute(workingDir, null);

		String[] args = { fileToCopy.toString(),
				destDir.toString() + File.separator + fileToCopy.getName() };
		
		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(2, copyTool.getStatusCode());
		assertEquals("copy: '" + fileToCopy.getName() + "': File already exists",
				returnMessage);

	}

	@Test
	public void execute_MultipleFilesToDirectory_FileNotFoundTest()
			throws IOException {

		String[] args = new String[5];
		File aFile = null;

		args[0] = new File(sourceDir.toString() + File.separator
				+ "newFileToCopy_" + 0 + ".txt").toString();
		aFile = new File(args[0]);
		aFile.createNewFile();
		Files.write(aFile.toPath(), fileContent.getBytes(),
				StandardOpenOption.APPEND);

		args[1] = sourceDir.toString() + File.separator + "fileNotFound1.txt";

		args[2] = new File(sourceDir.toString() + File.separator
				+ "newFileToCopy_" + 2 + ".txt").toString();
		aFile = new File(args[2]);
		aFile.createNewFile();
		Files.write(aFile.toPath(), fileContent.getBytes(),
				StandardOpenOption.APPEND);

		args[3] = sourceDir.toString() + File.separator + "fileNotFound2.txt";
		args[4] = destDir.toString();

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals("copy: 'fileNotFound1.txt': No such file or directory"
				+ System.lineSeparator() + System.lineSeparator()
				+ "copy: 'fileNotFound2.txt': No such file or directory",
				returnMessage);

	}

	@Test
	public void execute_MultipleFilesToNonDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = new File(sourceDir.toString() + File.separator
					+ "newFileToCopy_" + i + ".txt");
			aFile.createNewFile();
			Files.write(aFile.toPath(), fileContent.getBytes(),
					StandardOpenOption.APPEND);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = destDir.toString() + File.separator + "NotADir";

		copyTool = new CopyTool(args);
		String returnMessage = copyTool.execute(workingDir, null);

		assertEquals(-4, copyTool.getStatusCode());
		assertEquals("copy: target '" + args[args.length - 1]
				+ "' is not a directory", returnMessage);

	}

	// Helper Functions
	public static void deleteFolder(File folder) throws IOException {
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

		Files.delete(folder.toPath());
	}
}
