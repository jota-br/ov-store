package ostro.veda.common.validation;

import ostro.veda.common.error.ErrorHandling;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductValidation {

    public static boolean hasValidInput(String nameProduct, String descriptionProduct, double priceProduct, int stockProduct)
            throws ErrorHandling.InvalidDescriptionException, ErrorHandling.InvalidInputException {
        return CommonValidation.hasValidName(nameProduct) &&
                CommonValidation.hasValidDescription(descriptionProduct) &&
                priceProduct >= 0.0 &&
                stockProduct >= 0;
    }

    public static boolean hasValidInput(String input) throws ErrorHandling.InvalidInputException {
        return hasValidImageUrl(input);
    }

    public static boolean hasValidImageUrl(String input) throws ErrorHandling.InvalidInputException {
        Pattern validPattern = Pattern.compile("^(https?://)?([\\w\\-]+\\.)+\\w+(/[\\w\\-.,@?^=%&:/~+#]*)?\\.(png)(\\?.*)?$");
        Matcher matcher = validPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        throw new ErrorHandling.InvalidInputException(
                ErrorHandling.InputExceptionMessage.EX_INVALID_IMAGE_URL,
                "url:" + input
        );
    }
}
