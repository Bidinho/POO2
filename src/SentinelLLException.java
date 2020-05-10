/**
 * Exception for the SentinelLL class in case violates the contract made by SentinelLL.
 */

public class SentinelLLException extends Exception {

    private final String message;

    /**
     * Inherit the methods of the super class Exception and set the message field.
     *
     * @param message message to be printed along with toString method, for readability's sake.
     */
    public SentinelLLException(String message) {
        super();
        this.message = message;
    }

    /**
     * Converts the SentinelLLException to a String representation.
     * @return String
     */
    @Override
    public String toString() {
        return "SentinelLLException: " + message;
    }
}
