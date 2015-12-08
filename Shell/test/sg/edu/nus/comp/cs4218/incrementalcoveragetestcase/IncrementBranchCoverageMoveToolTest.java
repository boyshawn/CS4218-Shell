package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.fileutils.IMoveTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.MoveTool;

public class IncrementBranchCoverageMoveToolTest {
	// Variables
	private IMoveTool moveTool;
	private File workingDir;
	private File sourceDir;
	private File destDir;

	@Before
	public void setUp() throws Exception {
		this.moveTool = new MoveTool(null);
		this.workingDir = new File(System.getProperty("user.dir"));
		this.sourceDir = createDir("_source_dir");
		this.destDir = createDir("destdir");
	}

	@After
	public void tearDown() throws Exception {
		this.moveTool = null;

		this.removeFiles(this.sourceDir);
		this.removeFiles(this.destDir);
		this.sourceDir.delete();
		this.destDir.delete();

		this.workingDir = null;
		this.sourceDir = null;
		this.destDir = null;
	}

	@Test
	public void execute_NullArgumentsTest() throws IOException {

		this.moveTool = new MoveTool(null);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(this.moveTool.getStatusCode(), -1);
		assertEquals("move: missing file operand", returnMessage);

	}

	@Test
	public void execute_FileAlreadyExists_FileToFileTest() throws IOException {

		File from = File.createTempFile("fileAreadyExists1", ".txt",
				this.sourceDir);
		String[] args = { from.toString(), from.toString() };

		this.moveTool = new MoveTool(args);
		String returnMessage = this.moveTool.execute(workingDir, null);

		assertEquals(2, this.moveTool.getStatusCode());
		assertEquals("move: '" + from.getName() + "': File already exists",
				returnMessage);
	}

	// Helper Functions
	private void removeFiles(File directory) throws IOException {

		if (directory.listFiles() != null) {
			for (File f : directory.listFiles()) {
				if (f.isDirectory()) {
					removeFiles(f);
				}

				f.delete();
			}
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
}
