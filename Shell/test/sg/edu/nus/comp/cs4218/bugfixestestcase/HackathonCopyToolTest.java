package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;

public class HackathonCopyToolTest {

	private ICopyTool copytool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		copytool = null;
	}

	/**
	 * Test error handling Destination folder contains file with the same name
	 * 
	 * 
	 * <p>
	 * Bug Fix Comment
	 * </p>
	 * <p>
	 * <ol>
	 * <li><b>BUG_ID : </b>#2.2</li>
	 * <li><b>Class : </b>CopyTool.java</li>
	 * <li><b>Method : </b>copy(File from, File to)</li>
	 * <li><b>Line Numbers : </b>71 - 74</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@Test
	public void executeFileExistedBefCopyTest() throws IOException {
		// Test error-handling 8
		File tempFolder = Files.createTempDirectory("tempFolder").toFile();
		File tempFile = Files.createTempFile("tempFile", ".tmp").toFile();
		copytool = new CopyTool(new String[] { tempFile.getName(),
				tempFolder.getName() });
		copytool.execute(tempFile.getParentFile(), null);
		File copiedFile = new File(tempFolder, tempFile.getName());
		assertTrue(copiedFile.exists());
		copytool.execute(tempFile.getParentFile(), null);
		assertEquals(2, copytool.getStatusCode());
		Files.delete(copiedFile.toPath());
		Files.delete(tempFolder.toPath());
		Files.delete(tempFile.toPath());
	}

}
