package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.extended1.IGrepTool;

public class GrepToolTest {
	// Constants
	private final String PATTERN_STR = "opening";
	private final int NUM_OF_LINES = 3;

	// Variables
	private IGrepTool grepTool;
	private static File tempDir;
	private static File input;
	private static File outputGetMatchingLinesOnly;
	private static File outputGetMatchingLinesOnlyMatchingPart;
	private static File outputGetMatchingLinesWithLeadingContext;
	private static File outputGetMatchingLinesWithTrailingContext;
	private static File outputGetMatchinLinesWithOutputContext;
	private static File outputGetNonMatchingLines;
	private static File workingDir;
	private static String inputStr;
	private static String outputGetMatchingLinesOnlyStr;
	private static String outputGetMatchingLinesOnlyMatchingPartStr;
	private static String outputGetMatchingLinesWithLeadingContextStr;
	private static String outputGetMatchingLinesWithTrailingContextStr;
	private static String outputGetMatchinLinesWithOutputContextStr;
	private static String outputGetNonMatchingLinesStr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workingDir = new File(System.getProperty("user.dir"));

		tempDir = new File("___tempDir");
		Files.createDirectories(tempDir.toPath());

		input = new File(tempDir.toString() + File.separator + "input");
		input.createNewFile();

		outputGetMatchingLinesOnly = new File(tempDir.toString()
				+ File.separator + "outputGetMatchingLinesOnly");
		outputGetMatchingLinesOnly.createNewFile();

		outputGetMatchingLinesOnlyMatchingPart = new File(tempDir.toString()
				+ File.separator + "outputGetMatchingLinesOnlyMatchingPart");
		outputGetMatchingLinesOnlyMatchingPart.createNewFile();

		outputGetMatchingLinesWithLeadingContext = new File(tempDir.toString()
				+ File.separator + "outputGetMatchingLinesWithLeadingContext");
		outputGetMatchingLinesWithLeadingContext.createNewFile();

		outputGetMatchingLinesWithTrailingContext = new File(tempDir.toString()
				+ File.separator + "outputGetMatchingLinesWithTrailingContext");
		outputGetMatchingLinesWithTrailingContext.createNewFile();

		outputGetMatchinLinesWithOutputContext = new File(tempDir.toString()
				+ File.separator + "outputGetMatchinLinesWithOutputContext");
		outputGetMatchinLinesWithOutputContext.createNewFile();

		outputGetNonMatchingLines = new File(tempDir.toString()
				+ File.separator + "outputGetNonMatchingLines");
		outputGetNonMatchingLines.createNewFile();

		/* Writing of content into files */
		inputStr = "It may also be objected that my opening remark about the appealing character of Pyrrhonism is wrong or surprising, given that it is not possible for anyone to think that the stance I have presented is attractive and worth adopting."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For instance, not only does the Skeptic not promise that the suspensive attitude will certainly make possible the attainment of ataraxia, but he does not even regard this as an aim that is intrinsic to his philosophy."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "To this objection, I would first reply that the appeal of Skepticism seems to lie in the sort of radical changes that this philosophy may entail in a person's life."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For, if adopted, the cautious Pyrrhonean attitude will prevent one from making rash judgments about any topic that one has not examined or found final answers to, which in turn will prevent one from acting hastily."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "Another profound change consists in the fact that, even if at some point the Skeptic broke some of the most important moral rules of the society to which he belongs, he would perhaps experience some kind of discomfort, but he would not believe that he has done something objectively wrong."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "This would free him from the shame and remorse that those who believe that such an action is morally incorrect would experience in the same situation."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "In sum, the Pyrrhonean philosophy would produce, if adopted, profound changes in a person's thoughts, feelings, and actions; changes that at first glance seem to be beneficial."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "But secondly, I think that whether or not Pyrrhonism is an appealing philosophy cannot in the end be determined a priori."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For it depends on whether one values such attitudes as caution, open-mindedness, and intellectual modesty; or, if one does, on whether these attitudes are preferred to, for example, the sense of assurance that one may experience when espousing philosophic systems or religious beliefs."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "This is why my opening comment was just that Pyrrhonism may still be found attractive and worth adopting.";

		outputGetMatchingLinesOnlyStr = "It may also be objected that my opening remark about the appealing character of Pyrrhonism is wrong or surprising, given that it is not possible for anyone to think that the stance I have presented is attractive and worth adopting."
				+ System.lineSeparator()
				+ "This is why my opening comment was just that Pyrrhonism may still be found attractive and worth adopting."
				+ System.lineSeparator();

		outputGetMatchingLinesOnlyMatchingPartStr = "opening"
				+ System.lineSeparator() + "opening" + System.lineSeparator();

		outputGetMatchingLinesWithLeadingContextStr = "It may also be objected that my opening remark about the appealing character of Pyrrhonism is wrong or surprising, given that it is not possible for anyone to think that the stance I have presented is attractive and worth adopting."
				+ System.lineSeparator()
				+ "--"
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For it depends on whether one values such attitudes as caution, open-mindedness, and intellectual modesty; or, if one does, on whether these attitudes are preferred to, for example, the sense of assurance that one may experience when espousing philosophic systems or religious beliefs."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "This is why my opening comment was just that Pyrrhonism may still be found attractive and worth adopting."
				+ System.lineSeparator();

		outputGetMatchingLinesWithTrailingContextStr = "It may also be objected that my opening remark about the appealing character of Pyrrhonism is wrong or surprising, given that it is not possible for anyone to think that the stance I have presented is attractive and worth adopting."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For instance, not only does the Skeptic not promise that the suspensive attitude will certainly make possible the attainment of ataraxia, but he does not even regard this as an aim that is intrinsic to his philosophy."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "--"
				+ System.lineSeparator()
				+ "This is why my opening comment was just that Pyrrhonism may still be found attractive and worth adopting."
				+ System.lineSeparator();

		outputGetMatchinLinesWithOutputContextStr = "It may also be objected that my opening remark about the appealing character of Pyrrhonism is wrong or surprising, given that it is not possible for anyone to think that the stance I have presented is attractive and worth adopting."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For instance, not only does the Skeptic not promise that the suspensive attitude will certainly make possible the attainment of ataraxia, but he does not even regard this as an aim that is intrinsic to his philosophy."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "--"
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For it depends on whether one values such attitudes as caution, open-mindedness, and intellectual modesty; or, if one does, on whether these attitudes are preferred to, for example, the sense of assurance that one may experience when espousing philosophic systems or religious beliefs."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "This is why my opening comment was just that Pyrrhonism may still be found attractive and worth adopting."
				+ System.lineSeparator();

		outputGetNonMatchingLinesStr = System.lineSeparator()
				+ "For instance, not only does the Skeptic not promise that the suspensive attitude will certainly make possible the attainment of ataraxia, but he does not even regard this as an aim that is intrinsic to his philosophy."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "To this objection, I would first reply that the appeal of Skepticism seems to lie in the sort of radical changes that this philosophy may entail in a person's life."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For, if adopted, the cautious Pyrrhonean attitude will prevent one from making rash judgments about any topic that one has not examined or found final answers to, which in turn will prevent one from acting hastily."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "Another profound change consists in the fact that, even if at some point the Skeptic broke some of the most important moral rules of the society to which he belongs, he would perhaps experience some kind of discomfort, but he would not believe that he has done something objectively wrong."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "This would free him from the shame and remorse that those who believe that such an action is morally incorrect would experience in the same situation."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "In sum, the Pyrrhonean philosophy would produce, if adopted, profound changes in a person's thoughts, feelings, and actions; changes that at first glance seem to be beneficial."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "But secondly, I think that whether or not Pyrrhonism is an appealing philosophy cannot in the end be determined a priori."
				+ System.lineSeparator()
				+ System.lineSeparator()
				+ "For it depends on whether one values such attitudes as caution, open-mindedness, and intellectual modesty; or, if one does, on whether these attitudes are preferred to, for example, the sense of assurance that one may experience when espousing philosophic systems or religious beliefs."
				+ System.lineSeparator() + System.lineSeparator();

		Files.write(input.toPath(), inputStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetMatchingLinesOnly.toPath(),
				outputGetMatchingLinesOnlyStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetMatchingLinesOnlyMatchingPart.toPath(),
				outputGetMatchingLinesOnlyMatchingPartStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetMatchingLinesWithLeadingContext.toPath(),
				outputGetMatchingLinesWithLeadingContextStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetMatchingLinesWithTrailingContext.toPath(),
				outputGetMatchingLinesWithTrailingContextStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetMatchinLinesWithOutputContext.toPath(),
				outputGetMatchinLinesWithOutputContextStr.getBytes(),
				StandardOpenOption.APPEND);

		Files.write(outputGetNonMatchingLines.toPath(),
				outputGetNonMatchingLinesStr.getBytes(),
				StandardOpenOption.APPEND);

	}

	@Before
	public void setUp() {
		grepTool = new GrepTool(null);
	}

	@After
	public void tearDown() {
		grepTool = null;

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		/* Delete all temporary testing files */
		Files.delete(input.toPath());
		Files.delete(outputGetMatchingLinesOnly.toPath());
		Files.delete(outputGetMatchingLinesOnlyMatchingPart.toPath());
		Files.delete(outputGetMatchingLinesWithLeadingContext.toPath());
		Files.delete(outputGetMatchingLinesWithTrailingContext.toPath());
		Files.delete(outputGetMatchinLinesWithOutputContext.toPath());
		Files.delete(outputGetNonMatchingLines.toPath());
		Files.delete(tempDir.toPath());

		/* Setting all file objects to null */
		tempDir = null;
		input = null;
		workingDir = null;
		inputStr = null;
	}

	// Black Box Positive Testing

	@Test
	public void defaultConstructor_StatusCodeZero() {
		IGrepTool grepTool = new GrepTool();
		assertEquals(0, grepTool.getStatusCode());
	}

	@Test
	public void getOnlyMatchingLines_StatusCodeZero() throws IOException {

		final String resultStr = grepTool.getOnlyMatchingLines(PATTERN_STR,
				inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyStr, resultStr);

	}

	@Test
	public void getMatchingLinesWithTrailingContext_StatusCodeZero()
			throws IOException {

		final String resultStr = grepTool.getMatchingLinesWithTrailingContext(
				NUM_OF_LINES, PATTERN_STR, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithTrailingContextStr, resultStr);

	}

	@Test
	public void getMatchingLinesWithLeadingContext_StatusCodeZero()
			throws IOException {

		final String resultStr = grepTool.getMatchingLinesWithLeadingContext(
				NUM_OF_LINES, PATTERN_STR, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithLeadingContextStr, resultStr);

	}

	@Test
	public void getMatchingLinesWithOutputContext_StatusCodeZero()
			throws IOException {

		final String resultStr = grepTool.getMatchingLinesWithOutputContext(
				NUM_OF_LINES, PATTERN_STR, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchinLinesWithOutputContextStr, resultStr);

	}

	@Test
	public void getCountOfMatchingLines_StatusCodeZero() {

		final int numOfMatchingLines = grepTool.getCountOfMatchingLines(
				PATTERN_STR, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(2, numOfMatchingLines);

	}

	@Test
	public void getMatchingLinesOnlyMatchingPart_StatusCodeZero()
			throws IOException {

		final String resultStr = grepTool.getMatchingLinesOnlyMatchingPart(
				PATTERN_STR, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyMatchingPartStr, resultStr);

	}

	@Test
	public void getNonMatchingLines_StatusCodeZero() throws IOException {

		final String resultStr = grepTool.getNonMatchingLines(PATTERN_STR,
				inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetNonMatchingLinesStr, resultStr);

	}

	@Test
	public void getHelp_StatusCodeZero() throws IOException {

		final String expectedMessage = new String(Files.readAllBytes(new File(
				"help_files" + File.separator + "grep_help").toPath()));
		final String returnMessage = grepTool.getHelp();

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(expectedMessage, returnMessage);

	}

	@Test
	public void execute_GetMatchingLinesOnly_StatusCodeZero()
			throws IOException {

		final String[] args = { PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyStr, resultStr);
	}

	@Test
	public void execute_GetMatchingLinesWithTrailingContext_StatusCodeZero()
			throws IOException {

		final String[] args = { "-A", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithTrailingContextStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesWithTrailingContextFromStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-A", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithTrailingContextStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesWithLeadingContext_StatusCodeZero()
			throws IOException {

		final String[] args = { "-B", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithLeadingContextStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesWithLeadingContextFromStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-B", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesWithLeadingContextStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesWithOutputContext_StatusCodeZero()
			throws IOException {

		final String[] args = { "-C", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchinLinesWithOutputContextStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesWithOutputContextFromStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-C", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchinLinesWithOutputContextStr, resultStr);

	}

	@Test
	public void execute_GetCountMatchingLines_StatusCodeZero()
			throws IOException {

		final String[] args = { "-c", PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(2, Integer.parseInt(resultStr.trim()));

	}

	@Test
	public void execute_GetCountMatchingLinesFromStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-c", PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(2, Integer.parseInt(resultStr.trim()));

	}

	@Test
	public void execute_GetMatchingLinesOnlyMatchingPart_StatusCodeZero()
			throws IOException {

		final String[] args = { "-o", PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyMatchingPartStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesOnlyMatchingPartFromStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-o", PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyMatchingPartStr.trim(),
				resultStr.trim());

	}

	@Test
	public void execute_GetNonMatchingLines_StatusCodeZero() throws IOException {

		final String[] args = { "-v", PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetNonMatchingLinesStr, resultStr);

	}

	@Test
	public void execute_GetNonMatchingLinesStdin_StatusCodeZero()
			throws IOException {

		final String[] args = { "-v", PATTERN_STR, "-" };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetNonMatchingLinesStr, resultStr);

	}

	@Test
	public void execute_GetHelp_StatusCodeZero() throws IOException {

		final String[] args = { "-help" };

		grepTool = new GrepTool(args);
		final String expectedResultStr = new String(
				Files.readAllBytes(new File("help_files" + File.separator
						+ "grep_help").toPath()));
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(expectedResultStr, resultStr);

	}

	@Test
	public void execute_GetMatchingLinesOnlyWithStandardInput_StatusCodeZero()
			throws IOException {

		final String[] args = { PATTERN_STR };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, inputStr);

		assertEquals(0, grepTool.getStatusCode());
		assertEquals(outputGetMatchingLinesOnlyStr, resultStr);

	}

	// Black Box Negative Testing

	@Test
	public void execute_NoArguments_StatusCodeNonZero() throws IOException {

		final String[] args = {};

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(GrepTool.ERR_CODE_INVALID_ARG, grepTool.getStatusCode());
		assertEquals(GrepTool.ERR_MSG_INVALID_ARG, resultStr);

	}

	@Test
	public void execute_SingleArgument_StatusCodeNonZero() throws IOException {

		final String[] args = { PATTERN_STR };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(GrepTool.ERR_CODE_INVALID_ARG, grepTool.getStatusCode());
		assertEquals(GrepTool.ERR_MSG_INVALID_ARG, resultStr);
	}

	@Test
	public void execute_InputFileNotFound_StatusCodeNonZero()
			throws IOException {

		final String filePath = workingDir.toString() + "/inputFileNotFound";

		final String[] args = { PATTERN_STR, filePath };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(GrepTool.ERR_CODE_IO, grepTool.getStatusCode());
		assertEquals(GrepTool.ERR_MSG_IO, resultStr);

	}

	@Test
	public void execute_MissingNumValue_StatusCodeNonZero() throws IOException {

		final String[] args = { "-A", PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(GrepTool.ERR_CODE_INVALID_ARG, grepTool.getStatusCode());
		assertEquals("grep: " + args[1] + ": invalid context length argument",
				resultStr);

	}

	@Test
	public void execute_InvalidOption_StatusCodeNonZero() throws IOException {

		final String[] args = { "----A", Integer.toString(NUM_OF_LINES),
				PATTERN_STR, input.toString() };

		grepTool = new GrepTool(args);
		final String resultStr = grepTool.execute(workingDir, null);

		assertEquals(GrepTool.ERR_CODE_INVALID_ARG, grepTool.getStatusCode());
		assertEquals(GrepTool.ERR_MSG_INVALID_ARG, resultStr);
	}

}