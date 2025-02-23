package ostro.veda.common.validation;

import ostro.veda.common.error.ErrorHandling;

public class CategoryValidation {

    public static boolean hasValidInput(String name, String description)
            throws ErrorHandling.InvalidInputException {
        return CommonValidation.hasValidName(name) &&
                CommonValidation.hasValidDescription(description);
    }
}
