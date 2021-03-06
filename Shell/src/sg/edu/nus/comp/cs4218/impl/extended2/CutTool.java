package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class CutTool extends ATool implements ICutTool {
	// Variables
	private List<Integer> accessedIndices;
	private int startIdx;
	private int endIdx;

	// Constants
	private static final int CUT_SUCCESSFUL = 0;
	private static final int INVALID_POSITION = -1;
	private static final int FILE_NOT_EXISTS = -2;
	private static final int INVALID_OPTION = -3;
	private static final int NO_ARGUMENTS = -4;
	private static final int INVALID_DELIMITER = -5;
	private final int INITIAL_STATUS = 111;
	private static final String OPTION_C = "-c";
	private static final String OPTION_D = "-d";
	private static final String OPTION_F = "-f";
	private static final String OPTION_HELP = "-help";
	private static final String DASH = "-";
	private static final String COMMA = ",";

	// Constructor
	public CutTool(String[] arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
		accessedIndices = new ArrayList<Integer>();
		setStatusCode(INITIAL_STATUS);
		startIdx = 0;
		endIdx = 0;
	}

	/**
	 * 
	 * <p>
	 * Cuts out specific characters defined by indices and displays them as a
	 * <b>String</b> value.
	 * </p>
	 * 
	 * @param list
	 *            List of character indices represented as a <b>String</b>
	 *            value.
	 * 
	 * @param input
	 *            Input text to extract characters from.
	 * 
	 * @return The characters specified by the indices returned as a
	 *         <b>String</b> value.
	 * 
	 */
	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		String[] listArr = list.split(COMMA);
		String[] inputs = input.split(System.lineSeparator());

		for (String inputStr : inputs) {
			for (String listItem : listArr) {
				int dashIdx = listItem.lastIndexOf(DASH);

				List<Integer> indices = getStartEndIdx(listItem, dashIdx,
						inputStr.length());

				if (indices.get(0) == 0 || indices.get(1) == 0) {
					setStatusCode(INVALID_POSITION);
					return "cut: fields and positions are numbered from 1"
							+ System.lineSeparator()
							+ "Try 'cut -help' for more information.";
				}

				addIndicesToList(indices.get(0), indices.get(1));
			}

			Collections.sort(accessedIndices);
			appendChars(inputStr, sb);
			sb.append(System.lineSeparator());
		}

		accessedIndices.clear();

		setStatusCode(CUT_SUCCESSFUL);
		return sb.toString().trim();
	}

	/**
	 * 
	 * <p>
	 * Cuts out specific characters from an delimited input defined by indices
	 * and displays them as a <b>String</b> value.
	 * </p>
	 * 
	 * @param list
	 *            List of character indices represented as a <b>String</b>
	 *            value.
	 * 
	 * @param delim
	 *            Type of delimiter to look out for.
	 * 
	 * @param input
	 *            Input text to extract characters from.
	 * 
	 * @return The characters specified by the indices returned as a
	 *         <b>String</b> value.
	 * 
	 */
	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		try {
			if (!delim.equals("") && input.contains(delim)) {
				String[] listArr = list.split(COMMA);
				String[] inputs = input.split(System.lineSeparator());

				for (String inputStr : inputs) {
					String[] delimitedArr = inputStr.split(delim);

					for (String listItem : listArr) {
						int dashIdx = listItem.lastIndexOf(DASH);

						List<Integer> indices = getStartEndIdxWithDelim(
								listItem, dashIdx, delimitedArr.length);

						if (indices != null) {
							if (indices.get(0) == 0 || indices.get(1) == 0) {
								setStatusCode(INVALID_POSITION);
								return "cut: fields and positions are numbered from 1"
										+ System.lineSeparator()
										+ "Try 'cut -help' for more information.";
							} else {
								addIndicesToList(indices.get(0), indices.get(1));
							}
						} else {
							return "Error: invalid decreasing range";
						}

					}

					Collections.sort(accessedIndices);
					appendChars(delimitedArr, sb, delim);
					sb.append(System.lineSeparator());
				}

				accessedIndices.clear();
			} else {
				sb.append(input);
			}
		} catch (NumberFormatException nFE) {
			return "Error: invalid list argument for -c";
		}

		setStatusCode(CUT_SUCCESSFUL);
		return sb.toString().trim();

	}

	/**
	 * <p>
	 * Displays the Help Information of the <i>cut</i> command.
	 * </p>
	 * 
	 * @return Displays the help information of the <i>cut</i> command
	 * 
	 */
	@Override
	public String getHelp() {
		// TODO Auto-generated method stub

		String helpStr = "";

		try {
			helpStr = new String(Files.readAllBytes(new File("help_files"
					+ File.separator + "cut_help").toPath()));
		} catch (IOException iOE) {

		}

		return helpStr;
	}

	/**
	 * <p>
	 * Executes the <i>cut</i> command with options specific to the type of
	 * arguments.
	 * </p>
	 * 
	 * @param workingDir
	 *            Current working directory Path
	 * 
	 * @param stdin
	 *            The input taken from the command line's input stream when "-"
	 *            is specified for the source (input) file.
	 * 
	 * @return Displays the result of the command and its corresponding option.
	 * 
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		// TODO Auto-generated method stub
		String returnMessage = "";

		try {
			List<String> arguments = Arrays.asList(args);
			if (arguments.size() == 0 || arguments.get(0).equals(DASH)) {
				setStatusCode(NO_ARGUMENTS);
				returnMessage = "cut: you must specify a list of bytes, characters, or fields"
						+ System.lineSeparator()
						+ "Try 'cut -help' for more information.";

			} else if (arguments.contains(OPTION_HELP)) {
				returnMessage = getHelp();
			} else if (arguments.contains(OPTION_C)) {
				returnMessage = handleOptionC(arguments, workingDir, stdin);
			} else if (arguments.contains(OPTION_F)) {
				if (arguments.contains(OPTION_D)) {
					returnMessage = handleOptionD(arguments, workingDir, stdin);
				} else {
					returnMessage = cutFromFilesWithDelimiter("", "",
							arguments.subList(2, arguments.size()), workingDir,
							stdin);
				}

			} else {
				setStatusCode(INVALID_OPTION);
				returnMessage = "cut: invalid option -- '"
						+ arguments.get(0).substring(1, 2) + "'"
						+ System.lineSeparator()
						+ "Try 'cut -help' for more information.";
			}

		} catch (IOException iOE) {

		}

		return returnMessage.trim();
	}

	// Helper Methods

	/**
	 * 
	 * <p>
	 * Handles the scenario where option -c is specified together with the cut
	 * command.
	 * </p>
	 * 
	 * @param arguments
	 *            List of arguments
	 * 
	 * @param workingDir
	 *            Current working directory.
	 * 
	 * @param stdin
	 *            Standard Input from user.
	 * 
	 * @return A corresponding return message is returned.
	 * 
	 * @throws IOException
	 * 
	 */
	public String handleOptionC(List<String> arguments, File workingDir,
			String stdin) throws IOException {

		if (arguments.size() == 1) {
			setStatusCode(NO_ARGUMENTS);
			return "cut: option requires an argument -- 'c'"
					+ System.lineSeparator()
					+ "Try 'cut -help' for more information.";
		} else {
			if (arguments.get(1).endsWith(COMMA)
					|| arguments.get(1).startsWith(COMMA)) {
				setStatusCode(INVALID_POSITION);
				return "cut: fields and positions are numbered from 1"
						+ System.lineSeparator()
						+ "Try 'cut -help' for more information.";

			} else {
				return cutFromFiles(arguments.get(1),
						arguments.subList(2, arguments.size()), workingDir,
						stdin);
			}
		}

	}

	/**
	 * 
	 * <p>
	 * Handles the scenario where option -d is specified together with the cut
	 * command.
	 * </p>
	 * 
	 * @param arguments
	 *            List of arguments
	 * 
	 * @param workingDir
	 *            Current working directory.
	 * 
	 * @param stdin
	 *            Standard Input from user.
	 * 
	 * @return A corresponding return message is returned.
	 * 
	 * @throws IOException
	 * 
	 */
	public String handleOptionD(List<String> arguments, File workingDir,
			String stdin) throws IOException {

		int fieldsIdx = (arguments.get(2).equals(OPTION_F)) ? 3 : 1;
		int delimIdx = (arguments.get(2).equals(OPTION_F)) ? 1 : 3;

		String delim = arguments.get(delimIdx);

		if (delim.contains("'") || delim.contains("\"")) {
			if (delim.length() == 3) {
				if (arguments.get(fieldsIdx).endsWith(COMMA)
						|| arguments.get(fieldsIdx).startsWith(COMMA)) {
					setStatusCode(INVALID_POSITION);
					return "cut: fields and positions are numbered from 1"
							+ System.lineSeparator()
							+ "Try 'cut -help' for more information.";

				} else {
					return cutFromFilesWithDelimiter(arguments.get(fieldsIdx),
							Character.toString(delim.charAt(1)),
							arguments.subList(4, arguments.size()), workingDir,
							stdin);
				}

			} else {
				setStatusCode(INVALID_DELIMITER);
				return "cut: the delimiter must be a single character"
						+ System.lineSeparator()
						+ "Try 'cut -help' for more information.";
			}
		} else {
			setStatusCode(NO_ARGUMENTS);
			return "cut: option requires an argument -- 'd'"
					+ System.lineSeparator()
					+ "Try 'cut -help' for more information.";
		}

	}

	/**
	 * 
	 * <p>
	 * Adds a range of indices to the list, <i>accessedIndices</i>.
	 * </p>
	 * 
	 * @param ints
	 *            Range of indices.
	 */
	private void addIndicesToList(int... ints) {

		int start = ints[0] - 1;
		int end = ints[1];

		do {
			if (!accessedIndices.contains(start)) {
				accessedIndices.add(start);
			}
			start++;
		} while (start < end);

	}

	/**
	 * 
	 * <p>
	 * Appends the characters of an input using a <b>StringBuilder</b> based on
	 * the indices found within the list, <i>accessedIndices</i>.
	 * </p>
	 * 
	 * @param inputStr
	 *            Input to append characters from.
	 * 
	 * @param sb
	 *            <b>StringBuilder</b> Object.
	 */
	private void appendChars(String inputStr, StringBuilder sb) {
		for (int i : accessedIndices) {
			if (i < inputStr.length()) {
				sb.append(inputStr.charAt(i));
			}

		}
	}

	/**
	 * 
	 * <p>
	 * Overloaded method that appends the entries found within an array using a
	 * <b>StringBuilder</b> based on the indices found within the list,
	 * <i>accessedIndices</i>.
	 * </p>
	 * 
	 * @param delimitedArr
	 *            Input array that contains the entries to be appended.
	 * 
	 * @param sb
	 *            <b>StringBuilder</b> Object.
	 * 
	 * @param delim
	 *            Delimiter that is used concatenated with each array entry.
	 * 
	 */
	private void appendChars(String[] delimitedArr, StringBuilder sb,
			String delim) {
		for (int i : accessedIndices) {
			if (i < delimitedArr.length) {
				sb.append(delimitedArr[i] + delim);
			}
		}

		if (sb.lastIndexOf(delim) != -1) {
			sb.deleteCharAt(sb.lastIndexOf(delim));
		}
	}

	/**
	 * 
	 * <p>
	 * Cuts out the same range of characters from the specified files.
	 * </p>
	 * 
	 * @param list
	 *            Range of indices to cut out the characters from the contents
	 *            of each file.
	 * 
	 * @param files
	 *            List of files to cut out characters from.
	 * 
	 * @param workingDir
	 *            Current working directory.
	 * 
	 * @param stdin
	 *            Standard Input from user.
	 * 
	 * @return A string of characters successfully cut out from the specified
	 *         files.
	 * 
	 * @throws IOException
	 * 
	 */
	public String cutFromFiles(String list, List<String> filesList,
			File workingDir, String stdin) throws IOException {

		StringBuilder sb = new StringBuilder();
		String returnMessage = "";
		File fileToRead = null;
		String fileContent = "";
		int fileCount = 0;
		boolean isValid = true;

		ArrayList<String> files = new ArrayList<>(filesList);
		if (files.isEmpty()) {
			files.add("-");
		}

		while (isValid) {
			if (files.get(0).equals("-")) {
				fileContent = stdin;
				isValid = false;
				returnMessage = cutSpecfiedCharacters(list, fileContent);
			} else {
				fileToRead = new File(files.get(fileCount));

				if (fileToRead.exists()) {
					fileContent = new String(Files.readAllBytes(fileToRead
							.toPath()));
					returnMessage = cutSpecfiedCharacters(list, fileContent);
				} else {
					setStatusCode(FILE_NOT_EXISTS);
					returnMessage = "cut: " + fileToRead.getName()
							+ ": No such file or directory";
				}
			}

			sb.append(returnMessage + System.lineSeparator());

			if (getStatusCode() == INVALID_POSITION) {
				break;
			} else {
				fileCount++;
				if (fileCount == files.size()) {
					isValid = false;
				}
			}

		}

		return sb.toString().trim();
	}

	/**
	 * 
	 * <p>
	 * Cuts out the same range of characters (using a delimiter) from the
	 * specified files.
	 * </p>
	 * 
	 * @param list
	 *            Range of indices to cut out the characters from the contents
	 *            of each file.
	 * 
	 * @param delim
	 *            Delimiter that separates each field of the input.
	 * 
	 * @param fields
	 *            List of fields to cut out characters from.
	 * 
	 * @param workingDir
	 *            Current working directory.
	 * 
	 * @param stdin
	 *            Standard Input from user.
	 * 
	 * @return A string of characters successfully cut out from the specified
	 *         files.
	 * @throws IOException
	 * 
	 */
	public String cutFromFilesWithDelimiter(String fields, String delim,
			List<String> filesList, File workingDir, String stdin)
			throws IOException {

		StringBuilder sb = new StringBuilder();
		String returnMessage = "";
		File fileToRead = null;
		String fileContent = "";
		int fileCount = 0;
		boolean isValid = true;

		// If no file specified, read from stdin
		ArrayList<String> files = new ArrayList<>(filesList);
		if (files.isEmpty()) {
			files.add("-");
		}

		while (isValid) {
			if (files.get(0).equals("-")) {
				fileContent = stdin;
				isValid = false;
				returnMessage = cutSpecifiedCharactersUseDelimiter(fields,
						delim, fileContent);
			} else {
				fileToRead = new File(files.get(fileCount));

				if (fileToRead.exists()) {
					fileContent = new String(Files.readAllBytes(fileToRead
							.toPath()));
					returnMessage = cutSpecifiedCharactersUseDelimiter(fields,
							delim, fileContent);
				} else {
					setStatusCode(FILE_NOT_EXISTS);
					returnMessage = "cut: " + fileToRead.getName()
							+ ": No such file or directory";
				}
			}

			sb.append(returnMessage + System.lineSeparator());

			if (getStatusCode() == INVALID_POSITION) {
				break;
			} else {
				fileCount++;
				if (fileCount == files.size()) {
					isValid = false;
				}
			}

		}

		return sb.toString().trim();
	}

	/**
	 * 
	 * <p>
	 * Determines the start and end indices to cut from the inputString when
	 * there is a dash in the list argument.
	 * </p>
	 * 
	 * @param listItem
	 *            Index specified in the list of of indices.
	 * 
	 * @param dashIdx
	 *            Index of "-" character in the list argument.
	 * 
	 * @param inputStrLen
	 *            Length of the Input String
	 * 
	 * @return A list containing the start and end index to cut from.
	 * 
	 */
	public List<Integer> getStartEndIdx(String listItem, int dashIdx,
			int inputStrLen) {

		List<Integer> indices = new ArrayList<Integer>();

		if (dashIdx != -1) {
			if (listItem.startsWith(DASH)) {
				startIdx = 1;
				endIdx = Integer.parseInt(listItem.substring(dashIdx + 1));
			} else if (listItem.endsWith(DASH)) {
				startIdx = Integer.parseInt(listItem.substring(0, dashIdx));
				endIdx = inputStrLen;
			} else {
				startIdx = Integer.parseInt(listItem.substring(0, dashIdx));
				endIdx = Integer.parseInt(listItem.substring(dashIdx + 1));
			}
		} else {
			startIdx = Integer.parseInt(listItem);
			endIdx = startIdx;
		}

		indices.add(startIdx);
		indices.add(endIdx);

		return indices;
	}

	/**
	 * 
	 * <p>
	 * Determines the start and end indices to cut from the inputString with
	 * delimiter when there is a dash in the list argument.
	 * </p>
	 * 
	 * @param listItem
	 *            Index specified in the list of of indices.
	 * 
	 * @param dashIdx
	 *            Index of "-" character in the list argument.
	 * 
	 * @param delimitedArrLen
	 *            Length of the array that contains the delimited inputs.
	 * 
	 * @return A list containing the start and end index to cut from.
	 * 
	 */
	public List<Integer> getStartEndIdxWithDelim(String listItem, int dashIdx,
			int delimitedArrLen) {

		List<Integer> indices = new ArrayList<Integer>();

		if (dashIdx != -1) {
			if (listItem.startsWith(DASH)) {
				startIdx = 1;
				endIdx = Integer.parseInt(listItem.substring(dashIdx + 1));
			} else if (listItem.endsWith(DASH)) {
				startIdx = Integer.parseInt(listItem.substring(0, dashIdx));
				endIdx = delimitedArrLen;

			} else {
				startIdx = Integer.parseInt(listItem.substring(0, dashIdx));
				endIdx = Integer.parseInt(listItem.substring(dashIdx + 1));
				if (startIdx > endIdx && startIdx > 0 && endIdx > 0) {
					return null;
				}
			}
		} else {
			startIdx = Integer.parseInt(listItem);
			endIdx = startIdx;
		}

		indices.add(startIdx);
		indices.add(endIdx);

		return indices;
	}

}
