package sg.edu.nus.comp.cs4218.bugfixestestcase;

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

public class HackathonCommToolTest {
	private ICommTool commTool;

	private static final String USER_DIRECTORY = "user.dir";
	private static File defaultWorkingDirectory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		defaultWorkingDirectory = new File(System.getProperty(USER_DIRECTORY));

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
		
		File emptyFolder1 = new File ("EmptyFolder");
		emptyFolder1.delete();
		
		File fileWithSpace = new File ("A file with space.txt");
		fileWithSpace.delete();
	}

	@Before
	public void setUp() throws Exception {
		commTool = new CommTool(null);
	}

	@After
	public void tearDown() throws Exception {
		commTool = null;
	}

	//Mean for Bug #6.5
	@Test
	public void compareFiles_1stFileIsDirectory_DirectoryError(){
		String result = commTool.compareFiles("EmptyFolder", "testFile1.txt");
		assertNotEquals(0, commTool.getStatusCode());
		assertEquals("comm: EmptyFolder: Is a directory", result);
	}
	
	@Test
	public void compareFiles_2ndFileIsDirectory_DirectoryError(){
		String result = commTool.compareFiles("testFile1.txt", "EmptyFolder");
		assertNotEquals(0, commTool.getStatusCode());
		assertEquals("comm: EmptyFolder: Is a directory", result);
	}
	
	@Test
	public void compareFilesCheckSortStatus_1stFileIsDirectory_DirectoryError(){
		String result = commTool.compareFilesCheckSortStatus("EmptyFolder", "testFile1.txt");
		assertNotEquals(0, commTool.getStatusCode());
		assertEquals("comm: EmptyFolder: Is a directory", result);
	}
	
	@Test
	public void compareFilesDoNotCheckSortStatus_1stFileIsDirectory_DirectoryError(){
		String result = commTool.compareFilesDoNotCheckSortStatus("EmptyFolder", "testFile1.txt");
		assertNotEquals(0, commTool.getStatusCode());
		assertEquals("comm: EmptyFolder: Is a directory", result);
	}
	
	@Test
	public void execute_1stFileIsDirectory_DirectoryError(){
		String[] arguments = {"EmptyFolder", "testFile1.txt"};
		commTool = new CommTool(arguments);
		
		String result = commTool.execute(defaultWorkingDirectory, null);
		
		assertNotEquals(0, commTool.getStatusCode());
		assertEquals("comm: EmptyFolder: Is a directory", result);
	}
	
	//Meant for bug 6.2
	//Valid Absolute File Path
	@Test
	public void compareFiles_ValidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("testFile2.txt");
		
		assertEquals(
				"aaa" 				+ System.lineSeparator() + 
				"\t" + 	"aaf" 		+ System.lineSeparator() + 
				"\t" + 	"abb" 		+ System.lineSeparator() + 
				"bbb" 				+ System.lineSeparator() + 
				"\t" +	"\t" + "ccc"+ System.lineSeparator() + 
				"ddd" 				+ System.lineSeparator() + 
				"\t" +	"fff" 		+ System.lineSeparator() + "", 
				commTool.compareFiles(testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));
		
		assertEquals(0, commTool.getStatusCode());
	}
	
	public void compareFilesCheckSortStatus_ValidAbsoluteFile_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("testFile2.txt");
		
		assertEquals(
				"aaa" 				+ System.lineSeparator() + 
				"\t" + 	"aaf" 		+ System.lineSeparator() + 
				"\t" + 	"abb" 		+ System.lineSeparator() + 
				"bbb" 				+ System.lineSeparator() + 
				"\t" +	"\t" + "ccc"+ System.lineSeparator() + 
				"ddd" 				+ System.lineSeparator() + 
				"\t" +	"fff" 		+ System.lineSeparator() + "", 
				commTool.compareFilesCheckSortStatus(
						testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));

		assertEquals(0, commTool.getStatusCode());		
	}
	
	@Test
	public void compareFilesDoNotCheckSortStatus_ValidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("testFile2.txt");
		
		assertEquals(
				"aaa" 				+ System.lineSeparator() + 
				"\t" + 	"aaf" 		+ System.lineSeparator() + 
				"\t" + 	"abb" 		+ System.lineSeparator() + 
				"bbb" 				+ System.lineSeparator() + 
				"\t" +	"\t" + "ccc"+ System.lineSeparator() + 
				"ddd" 				+ System.lineSeparator() + 
				"\t" +	"fff" 		+ System.lineSeparator() + "", 
				commTool.compareFilesDoNotCheckSortStatus(
						testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));

		assertEquals(0, commTool.getStatusCode());			
	}
	
	@Test
	public void execute_ValidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("testFile2.txt");
		
		String[] arguments = { testFile1.getAbsolutePath(),
				testFile2.getAbsolutePath() };
		commTool = new CommTool(arguments);
		
		assertEquals(
				"aaa" 				+ System.lineSeparator() + 
				"\t" + 	"aaf" 		+ System.lineSeparator() + 
				"\t" + 	"abb" 		+ System.lineSeparator() + 
				"bbb" 				+ System.lineSeparator() + 
				"\t" +	"\t" + "ccc"+ System.lineSeparator() + 
				"ddd" 				+ System.lineSeparator() + 
				"\t" +	"fff" 		+ System.lineSeparator() + "", 
				commTool.execute(defaultWorkingDirectory, null));
				
		assertEquals(0, commTool.getStatusCode());
	}
	
	//Invalid Absolute File path
	@Test
	public void compareFiles_InvalidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("NotExist.txt");
		
		assertEquals(
				"comm: " + defaultWorkingDirectory + File.separator
						+ "NotExist.txt: No such file or directory",
				commTool.compareFiles(testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));
		
		assertNotEquals(0, commTool.getStatusCode());
	}
	
	@Test
	public void compareFilesCheckSortStatus_InvalidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("NotExist.txt");
		
		assertEquals(
				"comm: " + defaultWorkingDirectory + File.separator
						+ "NotExist.txt: No such file or directory",
				commTool.compareFilesCheckSortStatus(
						testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));
		
		assertNotEquals(0, commTool.getStatusCode());
	}
	
	@Test
	public void compareFilesDonotCheckSortStatus_InvalidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		final File testFile2 = new File("NotExist.txt");
		
		assertEquals(
				"comm: " + defaultWorkingDirectory + File.separator
						+ "NotExist.txt: No such file or directory",
				commTool.compareFilesDoNotCheckSortStatus(
						testFile1.getAbsolutePath(),
						testFile2.getAbsolutePath()));
		
		assertNotEquals(0, commTool.getStatusCode());
	}
	
	@Test
	public void execute_InvalidAbsoluteFilePath_Output(){
		File testFile1 = new File("testFile1.txt");
		File testFile2 = new File("NotExist.txt");
		
		String[] arguments = { testFile1.getAbsolutePath(),
				testFile2.getAbsolutePath() };
		commTool = new CommTool(arguments);
		
		assertEquals(
				"comm: " + defaultWorkingDirectory + File.separator
						+ "NotExist.txt: No such file or directory",
				commTool.execute(defaultWorkingDirectory, null));
				
		assertNotEquals(0, commTool.getStatusCode());
	}
	
	//Meant for bug #6.3
	@Test
	public void execute_NoOption2ndFileWithSpaces_Output(){
		String[] arguments = {"testFile1.txt", "A file with space.txt"};
		commTool = new CommTool(arguments);
		
		assertEquals(
				"\t" + "\t" + "aaa" + System.lineSeparator() +
				"\t" + "abc" 		+ System.lineSeparator() + 
				"\t" + "acc" 		+ System.lineSeparator() +
				"comm: file 2 is not in sorted order" + System.lineSeparator() +
				"\t" + "abc" 		+ System.lineSeparator() +
				"bbb" 				+ System.lineSeparator() +
				"ccc" 				+ System.lineSeparator() +
				"ddd" 				+ System.lineSeparator(), 
				commTool.compareFiles("testFile1.txt", "A file with space.txt"));//(defaultWorkingDirectory, null));
		assertEquals(0, commTool.getStatusCode());
	}
}
