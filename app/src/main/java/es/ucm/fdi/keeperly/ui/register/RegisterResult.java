package es.ucm.fdi.keeperly.ui.register;


import androidx.annotation.Nullable;

/*
* Clase para informar si se ha tenido exito o no en el registro
 */
public class RegisterResult {
    @Nullable
    private RegisteredUserView success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) { this.error = error; }

    RegisterResult(@Nullable RegisteredUserView success) {this.success = success; }

    @Nullable
    RegisteredUserView getSuccess() { return success; }

    @Nullable
    Integer getError() { return error; }
}
