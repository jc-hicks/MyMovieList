package controller;

import java.io.OutputStream;

public class IArgsController {

    private static OutputStream out = System.out;

    /**
     * Args input from the user.
     */
    private static String[] args;

    private IArgsController() {
        // private constructor to prevent instantiation
    }

    private enum ConsoleText {
        /** Help short option. */
        H("-h"), 
        /** Help long option. */
        HELP("--help"), 
        /**Input watch list. */
        WATCHLIST("-w"),
        /** Data file option. */
        DATA("--data"), 
        /** All movies option. */
        ALL("all"),
        /** Filter data. */
        FILTER("--filter"),
        /** Sort data. */
        SORT("--sort");

        /** The text representation of the console command. */
        private final String text;
        
            ConsoleText(String text) {
                this.text = text;
            }
            
            /**
             * Get the text representation.
             * @return the text
             */
            public String getText() {
                return this.text;
            }
        }

        public String getHelp() {
            return "Usage: java -jar MovieApp.jar [options]\n" +
                    "Options:\n" +
                    "  -h, --help       Show this help message\n" +
                    "  --data           Specify the movie input file\n" +
                    "  -w               Specify the watchlist file\n" +
                    "  all              Show all movies\n";
        }
        
        public static boolean parseArg(String[] args) {
            String arg = args[0];
            if (arg.equals(ConsoleText.H.getText()) || arg.equals(ConsoleText.HELP.getText())) {
                // Show help message
                return true;
            } else if (arg.equals(ConsoleText.WATCHLIST.getText())) {
                // Handle watchlist option
                return true;
            } else if (arg.equals(ConsoleText.DATA.getText())) {
                // Handle data file option
                return true;
            } else if (arg.equals(ConsoleText.ALL.getText())) {
                // Handle all movies option
                return true;
            } else if (arg.equals(ConsoleText.FILTER.getText())) {
                // Handle filtering, could be by genre, year, etc. or name
                return true;
            } else if (arg.equals(ConsoleText.SORT.getText())) {
                // Handle sorting
                return true;
            } else {
                // Invalid argument
                return false;
            }
        }
    }
