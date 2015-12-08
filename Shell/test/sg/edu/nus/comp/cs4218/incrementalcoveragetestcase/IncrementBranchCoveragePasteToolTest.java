package sg.edu.nus.comp.cs4218.incrementalcoveragetestcase;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.impl.extended2.PasteTool;

public class IncrementBranchCoveragePasteToolTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Ensure all branches in getContentFromSameLineNumber is traversed.
	 * @throws IOException
	 */
	@Test
	public void getContentFromSameLineNumberNegativeLineIndexTest() throws IOException {
		PasteTool pasteTool = new PasteTool(null);
		String[] actual = pasteTool.getContentFromSameLineNumber(
				new ArrayList<String[]>(), -1);
		assertTrue(actual == null);
	}

}
