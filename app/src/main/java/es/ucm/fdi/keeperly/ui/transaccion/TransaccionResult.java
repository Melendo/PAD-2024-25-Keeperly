package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.annotation.Nullable;

public class TransaccionResult {
    @Nullable
    private Boolean success;
    @Nullable
    private Integer error;

    TransaccionResult(@Nullable Integer error) { this.error = error; }

    TransaccionResult(@Nullable Boolean success) { this.success = success; }

    @Nullable
    Boolean getSuccess() { return success; }

    @Nullable
    Integer getError() { return error; }
}
