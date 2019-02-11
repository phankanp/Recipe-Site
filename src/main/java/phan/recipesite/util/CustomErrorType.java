package phan.recipesite.util;

// Used to create a custom error message
public class CustomErrorType {

    private String errorMessage;

    public CustomErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}