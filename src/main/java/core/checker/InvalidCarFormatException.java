package core.checker;

/**
 * Created by shybovycha on 30/05/16.
 */
class InvalidCarFormatException extends Exception {
    public InvalidCarFormatException(String carStr, String message) {
        super(String.format("Invalid car format for string '%s'. %s", carStr, message));
    }
}