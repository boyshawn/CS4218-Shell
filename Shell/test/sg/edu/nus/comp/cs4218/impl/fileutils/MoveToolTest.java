/**
 * Assumption(s) Made: 
 * The Move command only supports the following functions:
 * 
 * 1. Move file1 to file2
 * 2. Move directory dir1 to dir2
 * 3. Move multiple files into directory
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

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;

public class MoveToolTest {
	// Variables
	private IMoveTool moveTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;
	private File fileToMove;
	private String initialWorkingDirStr;
	private String fileContent;

	@Before
	public void setUp() throws Exception {
		moveTool = new MoveTool(null);

		initialWorkingDirStr = System.getProperty("user.dir");
		workingDir = new File(initialWorkingDirStr);

		sourceDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "sourceDir")
						.toPath()).toString());
		destDir = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "destDir")
						.toPath()).toString());

		fileToMove = new File(sourceDir.toString() + File.separator
				+ "fileToMove.txt");
		fileToMove.createNewFile();

		fileContent = "1111111";
		Files.write(fileToMove.toPath(), fileContent.getBytes(),
				StandardOpenOption.APPEND);
	}

	@After
	public void tearDown() throws Exception {
		moveTool = null;

		deleteFolder(sourceDir);
		deleteFolder(destDir);

		workingDir = null;
		sourceDir = null;
		destDir = null;

		System.setProperty("user.dir", initialWorkingDirStr);
	}

	// Black Box Positive Testing

	@Test
	public void move_renameFileTest() throws IOException {

		boolean fileMoved = moveTool
				.move(fileToMove, new File(destDir.toString() + File.separator
						+ "fileMoved.txt"));

		assertTrue(fileMoved == true);
		assertEquals(0, moveTool.getStatusCode());

	}

	@Test
	public void move_renameDirectoryTest() throws IOException {

		File from = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "from")
						.toPath()).toString());
		File to = new File("renamedDir");

		boolean fileMoved = moveTool.move(from, to);

		assertTrue(fileMoved == true);
		assertEquals(0, moveTool.getStatusCode());

		to.delete();

	}

	@Test
	public void execute_renameFileTest() throws IOException {

		File to = new File(destDir.toString() + File.separator
				+ "renamedMovedFile.txt");

		String[] args = { fileToMove.toString(), to.toString() };

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(0, moveTool.getStatusCode());
		assertEquals("", returnMessage);

		to.delete();
	}

	@Test
	public void execute_renameDirectoryTest() throws IOException {

		File from = new File(Files.createDirectory(
				new File(workingDir.toString() + File.separator + "from")
						.toPath()).toString());
		File to = new File("renamedDir");

		String[] args = { from.toString(), to.toString() };

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(0, moveTool.getStatusCode());
		assertEquals("", returnMessage);

		to.delete();
	}

	@Test
	public void execute_FilesToDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = new File(sourceDir.toString() + File.separator
					+ "fileToMove_" + i + ".txt");
			aFile.createNewFile();
			Files.write(aFile.toPath(), fileContent.getBytes(),
					StandardOpenOption.APPEND);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = destDir.toString();

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(0, moveTool.getStatusCode());
		assertEquals("", returnMessage);

	}

	// Black Box Negative Testing

	@Test
	public void execute_NoArgumentsTest() throws IOException {

		String[] args = {};

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(-1, moveTool.getStatusCode());
		assertEquals("move: missing file operand", returnMessage);

	}

	@Test
	public void execute_SingleArgumentTest() throws IOException {

		String[] args = { fileToMove.toString() };

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(-2, moveTool.getStatusCode());
		assertEquals("move: missing destination file operand after '" + args[0]
				+ "'", returnMessage);

	}

	@Test
	public void execute_renameFile_FileNotFoundTest() throws IOException {

		File from = new File(sourceDir.toString() + File.separator
				+ "fileNotFound.txt");
		File to = new File(sourceDir.toString() + File.separator
				+ "renamedMovedFile.txt");

		String[] args = { from.toString(), to.toString() };

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(1, moveTool.getStatusCode());
		assertEquals("move: '" + from.getName()
				+ "': No such file or directory", returnMessage);

	}

	@Test
	public void execute_renameDirectory_FileNotFoundTest() throws IOException {

		File from = new File(sourceDir.toString() + File.separator
				+ "dirNotFound");
		File to = new File("renamedDir");

		String[] args = { from.toString(), to.toString() };

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(1, moveTool.getStatusCode());
		assertEquals("move: '" + from.getName()
				+ "': No such file or directory", returnMessage);

		to.delete();
	}

	@Test
	public void execute_FilesToNonDirectoryTest() throws IOException {

		File aFile = null;

		String[] args = new String[5];

		for (int i = 0; i < args.length - 1; i++) {
			aFile = new File(sourceDir.toString() + File.separator
					+ "fileToMove" + i + ".txt");
			aFile.createNewFile();
			Files.write(aFile.toPath(), fileContent.getBytes(),
					StandardOpenOption.APPEND);
			args[i] = aFile.toString();
		}

		args[args.length - 1] = destDir.toString() + File.separator + "notADir";

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals(-3, moveTool.getStatusCode());
		assertEquals("move: target '" + args[args.length - 1]
				+ "' is not a directory", returnMessage);

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

		moveTool = new MoveTool(args);
		String returnMessage = moveTool.execute(workingDir, null);

		assertEquals("move: 'fileNotFound1.txt': No such file or directory"
				+ System.lineSeparator() + System.lineSeparator()
				+ "move: 'fileNotFound2.txt': No such file or directory",
				returnMessage);
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
