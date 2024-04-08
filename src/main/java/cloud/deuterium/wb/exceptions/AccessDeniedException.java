package cloud.deuterium.wb.exceptions;

/**
 * Created by Milan Stojkovic 26-Mar-2024
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}
