package sg.edu.nus.comp.cs4218.impl.extended2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sg.edu.nus.comp.cs4218.extended2.ISortTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class SortTool extends ATool implements ISortTool {
	private static final String TOOL_NAME = "sort: ";

	private static final String MISSING_OPERAND_ERROR = TOOL_NAME
			+ "missing operand ";
	private static final int MISSING_OPERAND_ERROR_CODE = 2;

	private static final String OPTION_ERROR = TOOL_NAME + "unknown option -- ";
	private static final int OPTION_ERROR_CODE = 3;
	
	private static final String EXTRA_OPERAND_ERROR = TOOL_NAME + "extra operand ";
	private static final int EXTRA_OPERAND_ERROR_CODE = 3;

	private static final String FILE_NOT_FOUND = "No such file or directory";
	private static final int FILE_NOT_FOUND_ERROR = 4;
	
	private static final String DIRECTORY_ERROR = ": Is a directory"; 
	private static final int DIRECTORY_ERROR_CODE = 5;

	private static final String SUGGEST_SEE_HELP = "Try 'sort -help' for more information";
	
	private static final int NOT_EXECUTED_FINISH_CODE = -1;
	private static final int EXECUTED_FINISHED_CODE = 0;
	
	public SortTool(String[] arguments) {
		super(arguments);
	}

	@Override
	public String sortFile(String input) {
		setStatusCode(NOT_EXECUTED_FINISH_CODE);
		
		if (input == null) {
			setStatusCode(MISSING_OPERAND_ERROR_CODE);
			return "sort: missing operand" + System.lineSeparator()
					+ SUGGEST_SEE_HELP;
		}

		List<String> fileInput = new ArrayList<String>();
		fileInput.add(input);
		
		String result = sortMultipleFiles(fileInput);
		
		if(getStatusCode() == NOT_EXECUTED_FINISH_CODE){
			setStatusCode(EXECUTED_FINISHED_CODE);
		}
		
		return result;
	}

	@Override
	public String checkIfSorted(String input) {
		setStatusCode(NOT_EXECUTED_FINISH_CODE);
		
		if (input == null) {
			setStatusCode(MISSING_OPERAND_ERROR_CODE);
			return "sort: missing operand" + System.lineSeparator()
					+ SUGGEST_SEE_HELP;
		}
		
		File file = new File(input);
		if(file.isDirectory()){
			setStatusCode(DIRECTORY_ERROR_CODE);
			return TOOL_NAME + ": " + input + DIRECTORY_ERROR;
		}

		StringBuilder sortedOutput = new StringBuilder();

		List<String> linesInFile = null;
		Path inputPath = FileSystems.getDefault().getPath(input);

		try {
			linesInFile = Files.readAllLines(inputPath,
					Charset.defaultCharset());
		} catch (IOException e1) {
			setStatusCode(FILE_NOT_FOUND_ERROR);
			// Must use early termination here, or else there will be error at
			// later part of the program
			return TOOL_NAME + "open failed: " + input + ": " + FILE_NOT_FOUND;
		}

		// Compare result
		boolean isSorted = true;
		if (linesInFile.size() > 1) {
			int counter = 1;
			while (isSorted && counter < linesInFile.size()) {
				if (linesInFile.get(counter - 1).compareTo(
						linesInFile.get(counter)) > 0) {
					sortedOutput.append(TOOL_NAME + input + ":" + (counter + 1)
							+ ": disorder: " + linesInFile.get(counter));
					isSorted = false;
				}
				counter++;
			}
		}
		
		setStatusCode(EXECUTED_FINISHED_CODE);
		return sortedOutput.toString();
	}

	/**
	 * Returns the help message for the tool
	 * @return help message for Sort Tool
	 */
	@Override
	public String getHelp() {
		StringBuilder output = new StringBuilder();
		File file = new File("help_files" + File.separator + "sort_help");

		BufferedReader br = null;

		String line;
		try {
			br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				output.append(line + System.lineSeparator());
			}

			br.close();
		} catch (IOException e) {
			return TOOL_NAME + "Help file cannot be found";
		}
		
		if(getStatusCode() == NOT_EXECUTED_FINISH_CODE){
			setStatusCode(EXECUTED_FINISHED_CODE);
		}

		return output.toString().trim();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		setStatusCode(NOT_EXECUTED_FINISH_CODE);
		String sortedResult = null;
		
		//No arguments
		if(args == null || args.length == 0){
			setStatusCode(MISSING_OPERAND_ERROR_CODE);
			return MISSING_OPERAND_ERROR + System.lineSeparator()
					+ SUGGEST_SEE_HELP;
		}
		
		// Get help
		for (int i = 0; i < args.length; i++) {
			if("-help".equals(args[i])){
				return getHelp();
			}
		}
		
		sortedResult = checkForWrongOption();
		 
		if("".equals(sortedResult)){

			//From this point onwards, no more invalid option
			List<String> fileList = new ArrayList<>();
			boolean checkIfSort = false;
			
			//Collect all the non-option file
			for(int i = 0; i < args.length; i++){
				if(!args[i].startsWith("-")){
					fileList.add(args[i]);
				}
				else{
					checkIfSort = true;
				}
			}
			
			if(!checkIfSort){
				sortedResult = sortMultipleFiles(fileList);
			}
			else{
				switch(fileList.size()){
				case 0:
					setStatusCode(MISSING_OPERAND_ERROR_CODE);
					sortedResult = MISSING_OPERAND_ERROR + System.lineSeparator() + SUGGEST_SEE_HELP;
					break;
				case 1:
					sortedResult = checkIfSorted(fileList.get(0));
					break;
				default:
					setStatusCode(EXTRA_OPERAND_ERROR_CODE);
					sortedResult = EXTRA_OPERAND_ERROR + "'" + fileList.get(1)	+ "' not allowed with -c";
					break;
				}
			}
		}
		
		if(getStatusCode() == NOT_EXECUTED_FINISH_CODE){
			setStatusCode(EXECUTED_FINISHED_CODE);
		}

		return sortedResult;
	}

	private String sortMultipleFiles(List<String> inputFiles){
		StringBuilder sortedOutput = new StringBuilder();
		List<String> allFilesLines = new ArrayList<>();
		
		for (int i = 0; i < inputFiles.size(); i++) {
			List<String> linesInFile = null;
			Path inputPath = FileSystems.getDefault()
					.getPath(inputFiles.get(i));

			try {
				linesInFile = Files.readAllLines(inputPath,
						Charset.defaultCharset());
			}  catch (IOException e1){
				setStatusCode(FILE_NOT_FOUND_ERROR);
				if (i == 0) {
					return TOOL_NAME + "open failed: " + inputFiles.get(i)
							+ ": " + FILE_NOT_FOUND;
				} else {
					return TOOL_NAME + "stat failed: " + inputFiles.get(i)
							+ ": " + FILE_NOT_FOUND;
				}
			}
			allFilesLines.addAll(linesInFile);
		}

		Collections.sort(allFilesLines);

		// Building result
		for (int i = 0; i < allFilesLines.size(); i++) {
			sortedOutput.append(allFilesLines.get(i));
			sortedOutput.append(System.lineSeparator());
		}

		// Trim the last unnecessary new line
		if(sortedOutput.length() > 0){
			sortedOutput.setLength(sortedOutput.lastIndexOf(System.lineSeparator()));
		}
		
		return sortedOutput.toString();
	}
	
	private String checkForWrongOption() {
		String errorMessage = ""; 
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				//FIXME: Need to handle corner case where it is only "-"
				boolean foundWrongOption = false;
				int counter = 1;
				while (!foundWrongOption && counter < args[i].length()) {
					char substring = args[i].charAt(counter);
					if (substring != 'c') {
						setStatusCode(OPTION_ERROR_CODE);
						errorMessage = OPTION_ERROR + substring
								+ System.lineSeparator() + SUGGEST_SEE_HELP;
						foundWrongOption = true;
					}
					counter++;
				}
			}
		}
		
		return errorMessage;
	}

}
