package sg.edu.nus.comp.cs4218.bugfixestestcase;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.extended2.CutTool;

public class HackathonCutToolTest {

	private ICutTool cuttool;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		cuttool = null;
	}

	/**
	 * <p>
	 * Bug Fix Comment
	 * </p>
	 * <p>
	 * <ol>
	 * <li><b>BUG_ID : </b>#5.1</li>
	 * <li><b>Class : </b>CutTool.java</li>
	 * <li><b>Method : </b>getStartEndIdxWithDelim(String listItem, int dashIdx,
	 * int delimitedArrLen)</li>
	 * <li><b>Line Numbers : </b>679 - 681</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@Test
	public void cutSpecfiedCharDecreasingRangeTest() throws IOException {
		cuttool = new CutTool(new String[] {});
		String expectedResult = "Error: invalid decreasing range";
		assertEquals(
				expectedResult,
				cuttool.cutSpecifiedCharactersUseDelimiter("8-6", ":",
						"alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
	}

	/**
	 * <p>
	 * Bug Fix Comment
	 * </p>
	 * <p>
	 * <ol>
	 * <li><b>BUG_ID : </b>#5.2</li>
	 * <li><b>Class : </b>CutTool.java</li>
	 * <li><b>Method : </b>cutSpecifiedCharactersUseDelimiter(String list,
	 * String delim, String input)</li>
	 * <li><b>Line Numbers : </b>169 - 171</li>
	 * </ol>
	 * </p>
	 * 
	 */
	@Test
	public void cutSpecfiedCharNotNumberRangeTest() throws IOException {
		cuttool = new CutTool(new String[] {});
		String expectedResult = "Error: invalid list argument for -c";
		assertEquals(
				expectedResult,
				cuttool.cutSpecifiedCharactersUseDelimiter("abc", ":",
						"alpha:beta:gamma:delta:epsilon:zeta:eta:teta:iota:kappa:lambda:mu"));
	}

}
