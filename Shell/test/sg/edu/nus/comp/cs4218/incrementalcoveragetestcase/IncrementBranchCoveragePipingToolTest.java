package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CopyTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;

public class IncrementBranchCoveragePipingToolTest {

PipingTool pipingTool;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		pipingTool = null;
	}
	
	@Test
	public void execute_NullTools_GetStatusCode0() {
		ITool to = new CatTool(new String[]{"-"});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(to);
		tools.add(null);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), null);
		assertEquals(PipingTool.STATUS_CODE_NULL_TOOL, pipingTool.getStatusCode());
	}
	
	@Test
	public void execute_2ndCommandFail_GetStatusCode1() {
		// cd | pwd
		ITool tool1 = new CopyTool(new String[]{"fileThatDoesNotExist"});
		ITool tool2 = new EchoTool(new String[]{System.getProperty("user.dir")});
		Vector<ITool> tools = new Vector<ITool>();
		tools.add(tool1);
		tools.add(tool2);
		
		pipingTool = new PipingTool(tools);
		pipingTool.execute(new File(System.getProperty("user.dir")), "hello");
		assertEquals(1, pipingTool.getStatusCode());
	}

}
