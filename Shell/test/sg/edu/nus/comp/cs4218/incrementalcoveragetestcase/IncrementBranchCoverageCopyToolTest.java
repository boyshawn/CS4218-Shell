package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.ICopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;

public class IncrementBranchCoverageCopyToolTest {
	// Variables
	private ICopyTool copyTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;

	@Before
	public void setUp() throws Exception {
		this.copyTool = new CopyTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.sourceDir = createDir("sourceDir");
		this.destDir = createDir("testDir");
	}

	@After
	public void tearDown() throws Exception {
		this.copyTool = null;

		this.removeFiles(this.sourceDir);
		this.removeFiles(this.destDir);
		Files.delete(this.sourceDir.toPath());
		Files.delete((this.destDir.toPath()));

		this.workingDir = null;
		this.sourceDir = null;
		this.destDir = null;

		File file = new File("copiedFolder");
		if (file.exists()) {
			deleteFolder(file);
		}
	}

	@Test
	public void execute_NullArgumentsTest() throws IOException {

		this.copyTool = new CopyTool(null);
		String returnMessage = this.copyTool.execute(this.workingDir, null);

		assertEquals(this.copyTool.getStatusCode(), -1);
		assertEquals("copy: missing file operand", returnMessage);

	}

	// Helper Functions
	private void removeFiles(File directory) throws IOException {

		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				removeFiles(f);
			}

			Files.delete(f.toPath());
		}
	}

	private File createDir(String path) {
		File dir = new File(path);
		try {
			Files.createDirectories(dir.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dir;
	}

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
