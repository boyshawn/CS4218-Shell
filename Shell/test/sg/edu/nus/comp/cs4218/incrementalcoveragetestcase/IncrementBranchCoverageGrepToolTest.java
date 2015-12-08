package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;

public class IncrementBranchCoverageGrepToolTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void executeOptionANegativeLineCountTest() {
		String[] args = {"-A", "-3", "pattern", "filePath"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_CONTEXT_ARG, -3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionANoFilesArgTest() {
		String[] args = {"-A", "3", "pattern"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionBNegativeLineCountTest() {
		String[] args = {"-B", "-3", "pattern", "filePath"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_CONTEXT_ARG, -3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionBNoFilesArgTest() {
		String[] args = {"-B", "3", "pattern"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionBMultipleFilesArgTest() 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer1 = new PrintWriter("filePath1", "UTF-8");
		PrintWriter writer2 = new PrintWriter("filePath2", "UTF-8");
		
		String[] args = {"-B", "3", "pattern", "filePath1", "filePath2"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = "";
		
		assertEquals(expected, actual);
		
		// Clean up
		writer1.close();
		writer2.close();
		
		File file1 = new File("filePath1");
		file1.delete();
		File file2 = new File("filePath2");
		file2.delete();
	}
	
	@Test
	public void executeOptionCNegativeLineCountTest() {
		String[] args = {"-C", "-3", "pattern", "filePath"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_CONTEXT_ARG, -3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionCNoFilesArgTest() {
		String[] args = {"-C", "3", "pattern"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionCMultipleFilesArgTest() 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer1 = new PrintWriter("filePath1", "UTF-8");
		PrintWriter writer2 = new PrintWriter("filePath2", "UTF-8");
		
		String[] args = {"-C", "3", "pattern", "filePath1", "filePath2"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = "";
		
		assertEquals(expected, actual);
		
		// Clean up
		writer1.close();
		writer2.close();
		
		File file1 = new File("filePath1");
		file1.delete();
		File file2 = new File("filePath2");
		file2.delete();
	}
	
	@Test
	public void executeOptionCInvalidNumberTest() {
		String[] args = {"-C", "number", "pattern"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_CONTEXT_ARG, "number");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptioncNoFilesArgTest() {
		String[] args = {"-c", "pattern"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptioncMultipleFilesArgTest() 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer1 = new PrintWriter("filePath1", "UTF-8");
		PrintWriter writer2 = new PrintWriter("filePath2", "UTF-8");
		
		String[] args = {"-c", "pattern", "filePath1", "filePath2"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = "0" + System.lineSeparator() + "0" + System.lineSeparator();
		
		assertEquals(expected, actual);
		
		// Clean up
		writer1.close();
		writer2.close();
		
		File file1 = new File("filePath1");
		file1.delete();
		File file2 = new File("filePath2");
		file2.delete();
	}
	
	@Test
	public void executeOptionoNoFilesArgTest() {
		String[] args = {"-o", "3"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionoMultipleFilesArgTest() 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer1 = new PrintWriter("filePath1", "UTF-8");
		PrintWriter writer2 = new PrintWriter("filePath2", "UTF-8");
		
		String[] args = {"-o", "pattern", "filePath1", "filePath2"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = "";
		
		assertEquals(expected, actual);
		
		// Clean up
		writer1.close();
		writer2.close();
		
		File file1 = new File("filePath1");
		file1.delete();
		File file2 = new File("filePath2");
		file2.delete();
	}
	
	@Test
	public void executeOptionvNoFilesArgTest() {
		String[] args = {"-v", "3"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = String.format(GrepTool.ERR_MSG_INVALID_ARG);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void executeOptionvMultipleFilesArgTest() 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer1 = new PrintWriter("filePath1", "UTF-8");
		PrintWriter writer2 = new PrintWriter("filePath2", "UTF-8");
		
		String[] args = {"-v", "pattern", "filePath1", "filePath2"};
		GrepTool grepTool = new GrepTool(args);
		
		String actual = grepTool.execute(new File(""), "");
		String expected = "";
		
		assertEquals(expected, actual);
		
		// Clean up
		writer1.close();
		writer2.close();
		
		File file1 = new File("filePath1");
		file1.delete();
		File file2 = new File("filePath2");
		file2.delete();
	}
	
	@Test
	public void getLinesNullInputTest() {
		String[] actual = GrepTool.getLines(null);
		assertEquals(actual.length, 0);
	}
}
