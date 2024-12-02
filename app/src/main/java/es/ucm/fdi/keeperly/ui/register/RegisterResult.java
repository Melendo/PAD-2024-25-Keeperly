package es.ucm.fdi.keeperly.ui.register;


import androidx.annotation.Nullable;


public class RegisterResult {
    @Nullable
    private RegisteredUserView success;
    @Nullable
    private String error;

    RegisterResult(@Nullable String error) { this.error = error; }

    RegisterResult(@Nullable RegisteredUserView success) {this.success = success; }

    @Nullable
    RegisteredUserView getSuccess() { return success; }

    @Nullable
    String getError() { return error; }
}
