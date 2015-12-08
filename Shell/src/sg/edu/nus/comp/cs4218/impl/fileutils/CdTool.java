package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.ICdTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

/**
 * @author Francis Pang
 *
 */
public class CdTool extends ATool implements ICdTool {
	
	private final static String TOOL_NAME = "cd: ";
	
	public final static int DIRECTORY_ERROR_CODE = 1;
	private final static String DIRECTORY_ERROR = "The system cannot find the path specified.";
	
	private final static int FILE_ERROR_CODE = 2;
	private final static String FILE_NOT_FOUND_ERROR = ": No such file or directory";
	
	private static final int NOT_EXECUTED_FINISH_CODE = -1;
	private static final int EXECUTED_FINISHED_CODE = 0;
	
	private final static String USER_DIRECTORY = "user.dir";
	private final static String HOME_DIRECTORY = "user.home";
	protected static String previousDirectory;

	//Constructor
	/**
	 * Create a new CdTool instance so that it represents an unexecuted cd command. 
	 * 
	 * @param arguments
	 * 	the argument that is to be passed in to execute the command
	 */
	public CdTool(final String[] arguments) {
		super(arguments);
	}

	public CdTool(final String[] arguments, boolean resetPreviewDirectory){
		super(arguments);
		if(resetPreviewDirectory){
			previousDirectory = null;
		}
	}

	/**
	 * Executes the tool with arguments provided in the constructor
	 * 
	 * @param workingDir
	 *            the current working directory path
	 * 
	 * @param stdin
	 *            the additional input from the stdin
	 * 
	 * @return the message to be shown on the shell, null if there is no error
	 *         from the command
	 */
	@Override
	public String execute(final File workingDir, final String stdin) {
		setStatusCode(NOT_EXECUTED_FINISH_CODE);
		String output = "";

		/*
		 * Error case:
		 * 1. Current working directory is null object
		 * 2. Current working directory is not a valid directory/ file
		 * 3. Current working directory is not a directory
		 * 4. The argument is null object
		 */
		if (workingDir == null || !workingDir.exists()
				|| !workingDir.isDirectory() || this.args == null) {
			setStatusCode(DIRECTORY_ERROR_CODE);
			output = TOOL_NAME + DIRECTORY_ERROR;			
		}
		else{
			final String newDirectory;

			if("-".equals(this.args[0]) && previousDirectory != null){
				newDirectory = previousDirectory;
			}
			else{
				newDirectory = this.args[0];
			}

			if(	newDirectory.equals(workingDir.getAbsolutePath())){
				output = workingDir.getAbsolutePath();
			}
			else{
				File result = changeDirectory(newDirectory);
				if(result == null){
					setStatusCode(FILE_ERROR_CODE);
					output = TOOL_NAME + args[0] + FILE_NOT_FOUND_ERROR;
				}
				else{
					previousDirectory = workingDir.getAbsolutePath();
					output = result.getAbsolutePath();
				}
			}
			
		}
		if(getStatusCode() == NOT_EXECUTED_FINISH_CODE){
			setStatusCode(EXECUTED_FINISHED_CODE);
		}
		return output;
	}

	/**
	 * Change the current working directory to the specified string.
	 * <p>
	 * If the new file directory is the same as the current working directory,
	 * there will be no change of working directory
	 * 
	 * @param newDirectory
	 *            the new file directory to be changed
	 * 
	 * @return the working directory with the specified file directory, null if
	 *         <i>newDirectory</i> is invalid
	 */
	@Override
	public File changeDirectory(final String newDirectory) {
		setStatusCode(NOT_EXECUTED_FINISH_CODE);
		File directory = null;

		if (newDirectory == null) {
			directory = null;
		}
		else if("".equals(newDirectory)){	//Home directory
			directory = new File(System.getProperty(HOME_DIRECTORY));
			System.setProperty(USER_DIRECTORY, directory.getAbsolutePath());

		}
		else if("..".equals(newDirectory)){	//Parent directory
			directory = new File(System.getProperty(USER_DIRECTORY));
			directory = directory.getParentFile();
			System.setProperty(USER_DIRECTORY, directory.getAbsolutePath());
		}
		//Change to a relative directory navigating from home
		else if(newDirectory.startsWith("~" + File.separator)){
			final int NUM_PREFIX = newDirectory.indexOf(File.separator);
			final String HOME_ABSOLUTE_PATH = System.getProperty(HOME_DIRECTORY);
			String relativeDir = newDirectory.substring(NUM_PREFIX);
			relativeDir = HOME_ABSOLUTE_PATH.concat(relativeDir);
			directory = new File (relativeDir);

			if(!directory.exists()){
				setStatusCode(FILE_ERROR_CODE);
				directory = null;
			}
			else{
				System.setProperty(USER_DIRECTORY, relativeDir);
			}
		}
		else{
			directory = new File(newDirectory);

			if (!directory.isDirectory()) {
				setStatusCode(DIRECTORY_ERROR_CODE);
				directory = null;
			} 
			else if (directory.isAbsolute()) { // Absolute directory
				System.setProperty(USER_DIRECTORY, newDirectory);
			} 
			else { // Relative directory
				System.setProperty(USER_DIRECTORY, directory.getAbsolutePath());
			}
		}
		
		if(getStatusCode() == NOT_EXECUTED_FINISH_CODE){
			setStatusCode(EXECUTED_FINISHED_CODE);
		}
		return directory;
	}
}