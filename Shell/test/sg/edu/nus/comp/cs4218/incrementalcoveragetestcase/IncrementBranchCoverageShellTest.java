package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;

public class IncrementBranchCoverageShellTest {

	Shell shell;
	String sep = File.separator; // to make testing OS independent
	ITool tool;
	String[] results;
	
	@Before
	public void setUp() throws Exception {
		shell = new Shell();
	}

	@After
	public void tearDown() throws Exception {
		shell = null;
		tool = null;
		results = null;
	}
	
	@Test
	public void getBasicCommandArgs_mismatchCommandType_ReturnNull() {
		results = shell.getBasicCommandArgs("ls", " cd ");
		assertNull(results);
	}
	
	@Test
	public void getGrepArgs_NotAGrepCommand_ReturnNull() {
		// grep pattern file option (assumptions made)
		results = shell.getGrepArgs("cd ..");
		assertNull(results);
	}
	
	@Test
	public void parse_CommExceedMaxOptionsAllowed_ReturnCommTool() {
		tool = shell.parse("comm -c -d -c file.txt file2.txt");
		assertNull(tool);
	}
	
	@Test
	public void parse_CatInvalidSymbols_ReturnNull() {
		tool = shell.parse("cat  - $%^");
		assertNull(tool);
	}
	
	@Test
	public void parse_CatQuote_ReturnNull() {
		tool = shell.parse("cat \"");
		assertNull(tool);
	}
	
	@Test
	public void parse_GrepMultipleStdin_ReturnNull() {
		tool = shell.parse("grep - o - o");
		assertNull(tool);
	}
	
	@Test
	public void parse_RandomInput_ReturnNull() {
		tool = shell.parse("random input");
		assertNull(tool);
	}
	
	@Test
	public void parse_null_ReturnNull() {
		tool = shell.parse(null);
		assertNull(tool);
	}
	
}
