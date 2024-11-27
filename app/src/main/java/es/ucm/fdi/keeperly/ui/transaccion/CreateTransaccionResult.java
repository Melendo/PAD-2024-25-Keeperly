package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.annotation.Nullable;

public class CreateTransaccionResult {
    @Nullable
    private Boolean success;
    @Nullable
    private Integer error;

    CreateTransaccionResult(@Nullable Integer error) { this.error = error; }

    CreateTransaccionResult(@Nullable Boolean success) { this.success = success; }

    @Nullable
    Boolean getSuccess() { return success; }

    @Nullable
    Integer getError() { return error; }
}
