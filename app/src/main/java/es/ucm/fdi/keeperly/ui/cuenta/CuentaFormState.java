package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.annotation.Nullable;

public class CuentaFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer balanceError;
    private boolean isDataValid;

    CuentaFormState(@Nullable Integer nameError, @Nullable Integer balanceError) {
        this.nameError = nameError;
        this.balanceError = balanceError;
        this.isDataValid = false;
    }

    CuentaFormState(boolean isDataValid) {
        this.nameError = null;
        this.balanceError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    Integer getBalanceError() {
        return balanceError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
