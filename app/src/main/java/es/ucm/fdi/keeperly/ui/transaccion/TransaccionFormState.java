package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.annotation.Nullable;

public class TransaccionFormState {
    @Nullable
    private Integer conceptoError;
    @Nullable
    private Integer cantidadError;
    @Nullable
    private Integer cuentaError;
    @Nullable
    private Integer categoriaError;
    @Nullable
    private Integer fechaError;
    private boolean isDataValid;

    TransaccionFormState(@Nullable Integer conceptoError, @Nullable Integer cantidadError, @Nullable Integer cuentaError, @Nullable Integer categoriaError, @Nullable Integer fechaError) {
        this.conceptoError = conceptoError;
        this.cantidadError = cantidadError;
        this.cuentaError = cuentaError;
        this.categoriaError = categoriaError;
        this.fechaError = fechaError;
        this.isDataValid = false;
    }

    TransaccionFormState(boolean isDataValid) {
        this.conceptoError = null;
        this.cantidadError = null;
        this.cuentaError = null;
        this.categoriaError = null;
        this.fechaError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getConceptoError() {
        return conceptoError;
    }

    @Nullable
    public Integer getCantidadError() {
        return cantidadError;
    }

    @Nullable
    public Integer getCuentaError() {
        return cuentaError;
    }

    @Nullable
    public Integer getCategoriaError() {
        return categoriaError;
    }

    @Nullable
    public Integer getFechaError() {
        return fechaError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
