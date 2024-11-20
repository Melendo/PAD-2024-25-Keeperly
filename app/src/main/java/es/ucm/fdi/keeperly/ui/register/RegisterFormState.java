package es.ucm.fdi.keeperly.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer nombreError;
    @Nullable
    private Integer pwError;
    @Nullable
    private Integer repeatpwError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer emailError, @Nullable Integer nombreError, @Nullable Integer pwError, @Nullable Integer repeatpwError) {
        this.emailError = emailError;
        this.nombreError = nombreError;
        this.pwError = pwError;
        this.repeatpwError = repeatpwError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.emailError = null;
        this.nombreError = null;
        this.pwError = null;
        this.repeatpwError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getNombreError() {
        return nombreError;
    }

    @Nullable
    public Integer getPwError() {
        return pwError;
    }

    @Nullable
    public Integer getRepeatpwError() {
        return repeatpwError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
