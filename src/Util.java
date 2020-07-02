/**
 * Contains utility functions that may be useful in multiple classes
 */
public class Util {
    /**
     * Checks to see there's a string element whose value equals value
     *
     * Assumes values does not contain a null element
     *
     * @param value string to look for in values
     * @param values array in which to search for value. Cannot contain null elements
     * @return true if there exists element e in values s.t. e.equals(value), else false
     */
    public static boolean containsString(String value, String[] values) {
        for(String element: values) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether file referenced by filePath (regardless of its actual existence)
     * is of type fileType
     *
     * @param filePath path of some file (could theoretically be any string or null)
     * @param fileType a file format suffix: e.g. png, jpg, jpeg, etc.
     * @return true if filepath is of type fileType and false otherwise
     */
    public static boolean hasRightFileFormat(String filePath, String fileType) {
        if (filePath == null) { // null check
            return false;
        }

        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex == -1) {
            return false;
        } else {
            return filePath.substring(dotIndex + 1).equals(fileType);
        }
    }


}
