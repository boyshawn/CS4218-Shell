package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;

public class HackathonMoveToolTest {

	private IMoveTool movetool;
	private ArrayList<File> files;

	@Before
	public void setUp() throws Exception {
		files = new ArrayList<File>();
	}

	@After
	public void tearDown() throws Exception {
		movetool = null;
		for (int i = 0; i < files.size(); i++) {
			Files.delete(files.get(i).toPath());
		}
	}

	// Valid Test Cases
	/**
	 * <p>
	 * Test expected behavior Moving of file into folder
	 * </p>
	 * 
	 * <p>
	 * Bug Fix Comment
	 * </p>
	 * <p>
	 * <ol>
	 * <li><b>BUG_ID : </b>#3.2</li>
	 * <li><b>Class : </b>MoveTool.java</li>
	 * <li><b>Method : </b>move(File from, File to)</li>
	 * <li><b>Line Numbers : </b>60 - 95</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@Test
	public void moveTest() throws IOException {
		movetool = new MoveTool(new String[] {});
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		assertTrue(movetool.move(tempFile, tempFolder));
		File movedFile = new File(tempFolder, tempFile.getName());
		assertFalse(tempFile.exists());
		assertTrue(movedFile.exists());
		files.add(movedFile);
		files.add(tempFolder);
	}

}
