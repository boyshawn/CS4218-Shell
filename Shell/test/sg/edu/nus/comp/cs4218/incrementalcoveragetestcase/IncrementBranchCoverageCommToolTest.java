package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CommToolExtraTest;

public class IncrementBranchCoverageCommToolTest {
	private ICommTool commTool;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// creating testFile 1 & 2 in sorted order
		File myFile1 = new File("testFile1.txt");
		myFile1.createNewFile();
		CommToolExtraTest.writeFile("testFile1.txt", 
				"aaa" + System.lineSeparator() + 
				"bbb" + System.lineSeparator() + 
				"ccc" + System.lineSeparator() + 
				"ddd");
		
		File myFile2 = new File("testFile2.txt");
		myFile2.createNewFile();
		CommToolExtraTest.writeFile("testFile2.txt", 
				"aaf" + System.lineSeparator() + 
				"abb" + System.lineSeparator() + 
				"ccc" + System.lineSeparator() + 
				"fff");
		
		//Empty file
		File myFile3 = new File("testFile3.txt");
		myFile3.createNewFile();
		
		File shorterFile = new File("shorterFile.txt");
		shorterFile.createNewFile();
		CommToolExtraTest.writeFile("shorterFile.txt", 
				"aaf" + System.lineSeparator() + 
				"abb" + System.lineSeparator() + 
				"ccc" + System.lineSeparator()); 
		
		//Empty folder
		File emptyFolder1 = new File ("EmptyFolder");
		emptyFolder1.mkdir();
		
		//File with space
		File fileWithSpace = new File ("A file with space.txt");
		fileWithSpace.createNewFile();
		CommToolExtraTest.writeFile("A file with space.txt",
				"aaa" + System.lineSeparator() + 
				"abc" + System.lineSeparator() + 
				"acc" + System.lineSeparator() + 
				"abc");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File myFile1 = new File("testFile1.txt");
		Files.delete(myFile1.toPath());
		
		File myFile2 = new File("testFile2.txt");
		Files.delete(myFile2.toPath());
		
		File myFile3 = new File("testFile3.txt");
		Files.delete(myFile3.toPath());
		
		File emptyFolder1 = new File ("EmptyFolder");
		emptyFolder1.delete();
		
		File fileWithSpace = new File ("A file with space.txt");
		fileWithSpace.delete();
		
		File shorterFile = new File ("shorterFile.txt");
		shorterFile.delete();
	}

	@Before
	public void setUp() throws Exception {
		commTool = new CommTool(null);
	}

	@After
	public void tearDown() throws Exception {
		commTool = null;
	}
	
	@Test
	public void compareFilesCheckSortStatus_2ndFileisDirectory_DirectoryError() {
		assertEquals("comm: EmptyFolder: Is a directory",
				commTool.compareFilesCheckSortStatus("testFile1.txt",
						"EmptyFolder"));
		
		assertNotEquals(0, commTool.getStatusCode());
	}

	@Test
	public void compareFilesDoNotCheckSortStatus_2ndFileisDirectory_DirectoryError() {
		assertEquals("comm: EmptyFolder: Is a directory",
				commTool.compareFilesDoNotCheckSortStatus("testFile1.txt",
						"EmptyFolder"));
		
		assertNotEquals(0, commTool.getStatusCode());
	}
	
//	//TODO: Check this answer with ubuntu answer
	@Test
	public void compareFilesCheckSortStatus_2ndFileisShorter_Out(){
		assertEquals(
				"aaa" + System.lineSeparator() +
				"\t" + "aaf" + System.lineSeparator() +
				"\t" + "abb" + System.lineSeparator() +
				"bbb" + System.lineSeparator() +
				"\t" + "\t" + "ccc" + System.lineSeparator() +				
				"ddd" + System.lineSeparator() + "",
				commTool.compareFilesCheckSortStatus("testFile1.txt",
						"shorterFile.txt"));
		
		assertEquals(0, commTool.getStatusCode());
	}
	
	@Test
	public void compareFilesDoNotCheckSortStatus_2ndEmptyFile_Output(){
		commTool.compareFilesDoNotCheckSortStatus(
				"testFile1.txt", "testFile3.txt");
		
		assertEquals(0, commTool.getStatusCode());
	}
	
	

}
