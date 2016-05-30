package core.checker;

/**
 * Created by shybovycha on 30/05/16.
 */
class InvalidMoveFormatException extends Exception {
    public InvalidMoveFormatException(String moveStr, String message) {
        super(String.format("Invalid move format for string '%s'. %s", moveStr, message));
    }
}
