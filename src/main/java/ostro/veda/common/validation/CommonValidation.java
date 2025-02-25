package ostro.veda.common.validation;

import ostro.veda.common.error.ErrorHandling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidation {

    public static String hasValidName(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[\\sA-Za-z\\p{Punct}]{1,255}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return input;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_NAME,
                "name:" + input
        );
    }

    public static String hasValidDescription(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^[a-zA-Z\\s\\p{Punct}\n]{0,510}$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return input;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_DESCRIPTION,
                "description:" + input
        );
    }

    public static boolean hasValidId(int id) throws ErrorHandling.InvalidInputException {
        if (id > 0 || id == -1) return true;

        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_ID,
                "id:" + id
        );
    }
}
