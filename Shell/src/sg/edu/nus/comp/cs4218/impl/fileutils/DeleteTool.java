package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;

import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {
	
	public static final int STATUS_CODE_NOT_EXECUTED      = -1;
	public static final int STATUS_CODE_SUCCESSFUL		  = 0;
	public static final int STATUS_CODE_INVALID_ARGUMENTS = 1;
	public static final int STATUS_CODE_INVALID_FILE_OR_DIRECTORY = 2;
	public static final int STATUS_CODE_FAILED_TO_DELETE  = 3;
	
	public DeleteTool(String[] arguments) {
		super(arguments);
		setStatusCode(STATUS_CODE_NOT_EXECUTED);
	}

	
	/**
	 * Deletes a file or directory
	 * @param toDelete	file or directory to be deleted
	 * @return 			true if file or directory specified has been deleted successfully, 
	 * 					else returns false
	 */
	@Override
	public boolean delete(File toDelete) {
		
		if (!toDelete.exists()) { 
			setStatusCode(STATUS_CODE_INVALID_FILE_OR_DIRECTORY);
			return false;
		}
		
		else {
			boolean isDeleted = toDelete.delete();
			if (isDeleted) {
				setStatusCode(STATUS_CODE_SUCCESSFUL);
				return true;
			}
			else {
				setStatusCode(STATUS_CODE_FAILED_TO_DELETE);
				return false;
			}
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		
		// set status code as successful first
		// if an error is encountered, it will be changed
		setStatusCode(STATUS_CODE_SUCCESSFUL);
		
		if (args == null || args.length < 1) {
			setStatusCode(STATUS_CODE_INVALID_ARGUMENTS);
			return "delete: Invalid arguments given";
		}
		
		for (int i=0; i<args.length; ++i) {
			final String arg = args[i];
			if (arg.trim().isEmpty()) {
				setStatusCode(STATUS_CODE_INVALID_ARGUMENTS);
				return "delete: Invalid arguments given";
			}
			final File file = new File(arg);
			final boolean isDeleted = delete(file);
			if (!isDeleted) {
				switch (getStatusCode()) {
					case 2:
						return "delete: " + arg + ": No such file or directory";
					case 3:
						return "delete: " + arg + ": Failed to delete - ensure the directory is empty";
					default:
						return "delete: Unknown error";
					
				}
			}
		}
		return "";
	}
	
}
