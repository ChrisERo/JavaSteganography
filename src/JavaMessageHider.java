import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Contains Main functionality for JavaMessageHider program
 */
public class JavaMessageHider {
    private static final int ERROR_STATUS = 1;

    // Indecies of key parts of argument
    private static final int COMMAND_INDEX = 0;  // Whether to read or write
    private static final int IMG_FILE_INDEX = 1;  // Write or read message in this file
    private static final int MIDDLE_INDEX = 2; // [opt.] write new image to here, or
                                                 // get message for this file
    private static final int MSSG_FILE_INDEX = 3; // [opt. write only]
                                                  // get message of image here

    // Min and max number of arguments passed to main call
    private static final int MIN_ARGS_LENGTH = 2;
    private static final int MAX_ARGS_LENGTH = 4;

    // Commands used to read or write messages to an image
    private static final String [] READ_COMMANDS = new String[] {"-r", "-read"};
    private static final String[] WRITE_COMMANDS = new String[] {"-w", "-write"};

    /**
     * Determines whether args forms a valid set of arguments for JavaMessageHider,
     * regardless of the existence of specific files. If either the command, png, or txt
     * arguments re invalid, return the corresponding error message
     *
     * WARNING: function is public only for testing purposes
     *
     * @param args arguments given by user when executing JavaMessageHider
     * @return null if there are no syntactic errors in args, a String with the error
     *         message otherwise
     */
    public static String checkInputHelper(String [] args) {
        if (args == null ||
                args.length < MIN_ARGS_LENGTH ||
                args.length > MAX_ARGS_LENGTH) {
            return String.format("INVALID NUMBER OF ARGUMENTS");
        } else if (!Util.containsString(args[COMMAND_INDEX], READ_COMMANDS) &&
                !Util.containsString(args[COMMAND_INDEX], WRITE_COMMANDS)) {
            return String.format("INVALID COMMAND %s", args[COMMAND_INDEX]);
        }

        boolean isReadCommand = Util.containsString(args[COMMAND_INDEX], READ_COMMANDS);
        if (!Util.hasRightFileFormat(args[IMG_FILE_INDEX], "png")) {
            return String.format("INVALID IMAGE TYPE [%s], MUST BE .png",
                    args[IMG_FILE_INDEX]);
        } else if ((args.length == MAX_ARGS_LENGTH - 1 &&
                !Util.hasRightFileFormat(args[MIDDLE_INDEX], "txt") &&
                (isReadCommand ||
                        !Util.hasRightFileFormat(args[MIDDLE_INDEX], "png"))) ||
                (args.length == MAX_ARGS_LENGTH && (isReadCommand ||
                        !Util.hasRightFileFormat(args[MIDDLE_INDEX], "png")))
        ) {
            return "INVALID PARAMETERS CONCERNING THIRD PARAMETER!!!";
        } else if (args.length == MAX_ARGS_LENGTH &&
                !Util.hasRightFileFormat(args[MSSG_FILE_INDEX], "txt")) {
            return String.format("INVALID MESSAGE SOURCE/DESTINATION TYPE [%s], " +
                            "MUST BE .txt", args[MSSG_FILE_INDEX]);
        }

        return null;
    }


    /**
     * Determines whether args forms a valid set of arguments for JavaMessageHider,
     * regardless of the existence of specific files.
     *
     * @param args arguments given by user when executing JavaMessageHider
     * @return true if there are syntactic errors in args, false otherwise
     */
    private static boolean inputIsInvalid(String [] args) {
        String commandErrorMessage = checkInputHelper(args);
        if (commandErrorMessage != null) {
            System.out.println(commandErrorMessage);
            return true;
        }
        return false;
    }

    /**
     * Determines whether text file present in args and, if so, returns the contents of
     * that file, else asks the user to type in a message and returns that content.
     *
     * @param args valid list of arguments for JavaMessageHider program
     * @return some String
     * @throws IOException
     */
    private static String getMessage(String[] args) throws IOException {
        Scanner in;
        if (args.length == MIN_ARGS_LENGTH ||
                (args.length < MAX_ARGS_LENGTH &&
                Util.hasRightFileFormat(args[MIDDLE_INDEX], "png"))) {
           in = new Scanner(System.in);
           System.out.println("Write you message below: ");
           return in.nextLine();
        } else {
            int mssgIndex = args.length == MAX_ARGS_LENGTH ?
                    MSSG_FILE_INDEX : MIDDLE_INDEX;
            in = new Scanner(new File(args[mssgIndex]));
            String messageContent = "";
            while (in.hasNextLine()) {
                messageContent += in.nextLine() + '\n';
            }
            return messageContent.trim();
        }
    }


    /**
     * Writes a user-specified message into an image file
     *
     * @param args valid list of arguments for JavaMessageHider program
     * @return true if write executed successfully and false otherwise
     */
    private static boolean write(String [] args) {
        try {
            String imgFilePath = args[IMG_FILE_INDEX];
            // Set destinationPath to destinationPath specified by user or to imgFilePath
            // if no image is given
            String destinationPath;
            if (args.length > MIN_ARGS_LENGTH &&
                    Util.hasRightFileFormat(args[MIDDLE_INDEX], "png") ) {
                destinationPath = args[MIDDLE_INDEX];
            } else {
                destinationPath = imgFilePath; // write to self
            }
            String message = getMessage(args);

            MessageWriter writer = new MessageWriter(
                    message, imgFilePath, destinationPath);
            writer.writeMessage();

            return true;
        } catch (IOException e) {
            System.out.println("ONE OF THE FILES SELECTED DOES NOT EXIST " +
                    "OR IS POORLY FORMATTED");
            return false;
        } catch (Exception e) { // catch all
            System.out.println("UNANTICIPATED ERROR OCCURRED");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes message to screen, and maybe to a text file if one is specified in args
     * Currently used as helper function for read()
     *
     * @param mssg message to write on the screen, and maybe a file
     * @param args valid list of arguments for JavaMessageHider program
     * @throws IOException
     */
    private static void writeMessage(String mssg, String[] args) throws IOException {
        if (args.length == MAX_ARGS_LENGTH) { // write to specified file
            PrintWriter out = new PrintWriter(args[MSSG_FILE_INDEX]);
            out.print(mssg);
            out.close();
        }

        System.out.println("*******\nMESSAGE\n*******");
        System.out.println(mssg);
    }

    /**
     * Reads message from user-specified image. Assumes image had been written to before
     *
     * @param args valid list of arguments for JavaMessageHider program
     * @return true if write executed successfully and false otherwise
     */
    private static boolean read(String [] args) {
        try {
            String imgFilePath = args[IMG_FILE_INDEX];
            MessageReader reader = new MessageReader(imgFilePath);
            String message = reader.readMessage();
            writeMessage(message, args);
            return true;
        }

        catch (IOException e) {
            System.out.println("ONE OF THE FILES SELECTED DOES NOT EXIST " +
                    "OR IS POORLY FORMATTED");
            return false;
        } catch (Exception e) { // catch all
            System.out.println("UNANTICIPATED ERROR OCCURRED");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Main execution of JavaMessageHider
     *
     * @param args user-provided arguments
     */
    public static void main(String [] args) {
        if (inputIsInvalid(args)) {
            System.exit(1);
        }

        boolean success;
        String action;
        if (Util.containsString(args[COMMAND_INDEX], WRITE_COMMANDS)) {
            success = write(args);
            action = "MESSAGE WRITTEN SUCCESSFULLY";
        } else {
            success = read(args);
            action = "MESSAGE EXTRACTED SUCCESSFULLY";
        }

        if (!success) {
            System.exit(1);
        } else {
            System.out.println(action);
        }
    }
}
