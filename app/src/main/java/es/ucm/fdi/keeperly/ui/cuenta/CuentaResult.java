package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.annotation.Nullable;


public class CuentaResult {
    @Nullable
    private CreatedAccountView success;
    @Nullable
    private Integer error;

    CuentaResult(@Nullable Integer error) {
        this.error = error;
    }

    CuentaResult(@Nullable CreatedAccountView success) {
        this.success = success;
    }

    @Nullable
    CreatedAccountView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
