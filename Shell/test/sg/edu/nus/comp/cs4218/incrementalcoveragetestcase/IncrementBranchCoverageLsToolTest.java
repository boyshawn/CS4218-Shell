package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import sg.edu.nus.comp.cs4218.fileutils.ILsTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.LsTool;

public class IncrementBranchCoverageLsToolTest {
	private static ILsTool lsTool;
	private static String strDefaultDirectory;
	private static File currentWorkingDirectory;
//	private static final String DIRECTORY_ERROR_MSG = "No such file or directory";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		strDefaultDirectory = System.getProperty("user.dir");
		currentWorkingDirectory = new File(strDefaultDirectory);
		lsTool = new LsTool(null);
	}

	@After
	public void tearDown() throws Exception {
		lsTool = null;
		System.setProperty("user.dir", strDefaultDirectory);
		strDefaultDirectory = null;
	}

	@Test
	public void execute_InvalidWorkingDirectory_NonZeroStatusCode(){
		final String[] argument = {""};
		lsTool = new LsTool(argument);
		
		lsTool.execute(new File("CcwECrgRG"), null);
		assertNotEquals(0, lsTool.getStatusCode());
	}
	
	@Test
	public void sadvasd(){
		File[] fileList = currentWorkingDirectory.listFiles();
		
		//Get all filesname
		Vector<String> fileNames = new Vector<>();
		
		for(int i = 0; i < fileList.length; i++){
			fileNames.add(fileList[i].getName());
		}
		
		String[] expectedFileList = fileNames.toArray(new String[fileNames.size()]);
		
		//Add invalid file
		List<File> dirtyFileList = new ArrayList<>(Arrays.asList(fileList));
		File invalidFile = new File("acadcadcawcaf");
		dirtyFileList.add(invalidFile);
		
		final String returnStatement = lsTool.getStringForFiles(dirtyFileList);
		final String[] testStrFileList = returnStatement.split(System.lineSeparator());

		assertArrayEquals(expectedFileList, testStrFileList);

		assertEquals(0, lsTool.getStatusCode());
	}
}
