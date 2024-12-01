package es.ucm.fdi.keeperly.ui.transaccion;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.TransaccionRepository;

public class TransaccionViewModel extends ViewModel {
    private MutableLiveData<TransaccionFormState>  createTransaccionFormState = new MutableLiveData<>();
    private MutableLiveData<TransaccionResult> createTransaccionResult = new MutableLiveData<>();

    private MutableLiveData<TransaccionFormState> editTransaccionFormState = new MutableLiveData<>();
    private MutableLiveData<TransaccionResult> editTransaccionResult = new MutableLiveData<>();

    private TransaccionRepository transaccionRepository;

    private LiveData<List<TransaccionAdapter.TransaccionconCategoria>> transacciones;

    TransaccionViewModel() {
        this.transaccionRepository = RepositoryFactory.getInstance().getTransactionRepository();
        transacciones = transaccionRepository.getAllTransaccionesLoggedIn();
    }

    public MutableLiveData<TransaccionFormState> getCreateTransaccionFormState() {
        return createTransaccionFormState;
    }

    public MutableLiveData<TransaccionResult> getCreateTransaccionResult() {
        return createTransaccionResult;
    }

    public MutableLiveData<TransaccionFormState> getEditTransaccionFormState() {
        return editTransaccionFormState;
    }

    public MutableLiveData<TransaccionResult> getEditTransaccionResult() {
        return editTransaccionResult;
    }

    public void createTransaccion(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        if (isConceptoValid(concepto) && isCantidadValid(cantidad) && isDateValid(fecha)) {
            Result<Boolean> result = transaccionRepository.createTransaccion(concepto, cantidad, cuenta, categoria, fecha);

            if(result instanceof Result.Success) {
                Boolean data = ((Result.Success<Boolean>) result).getData();
                createTransaccionResult.setValue(new TransaccionResult(true));
            } else {
                createTransaccionResult.setValue(new TransaccionResult(R.string.crear_transaccion_fallado));
            }
        } else {
            createTransaccionResult.setValue(new TransaccionResult(R.string.crear_transaccion_datos_invalidos));
        }
        createTransaccionFormState.setValue(new TransaccionFormState(false));
    }

    public void createTransaccionDataChanged(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        if(!isConceptoValid(concepto)) {
            createTransaccionFormState.setValue(new TransaccionFormState(R.string.concepto_invalido, null, null, null, null));
        } else if (!isCantidadValid(cantidad)) {
            createTransaccionFormState.setValue(new TransaccionFormState(null, R.string.cantidad_invalida, null, null, null));
        } else if (!isDateValid(fecha)) {
            createTransaccionFormState.setValue(new TransaccionFormState(null, null, null, null, R.string.fecha_invalida));
        } else {
            createTransaccionFormState.setValue(new TransaccionFormState(true));
        }
    }

    public void editTransaccionDataChanged(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        if(!isConceptoValid(concepto)) {
            editTransaccionFormState.setValue(new TransaccionFormState(R.string.concepto_invalido, null, null, null, null));
        } else if (!isCantidadValid(cantidad)) {
            editTransaccionFormState.setValue(new TransaccionFormState(null, R.string.cantidad_invalida, null, null, null));
        } else if (!isDateValid(fecha)) {
            editTransaccionFormState.setValue(new TransaccionFormState(null, null, null, null, R.string.fecha_invalida));
        } else {
            editTransaccionFormState.setValue(new TransaccionFormState(true));
        }
    }

    public LiveData<List<TransaccionAdapter.TransaccionconCategoria>> getTransacciones() {
        return transacciones;
    }

    private boolean isConceptoValid (String concepto) {
        return !TextUtils.isEmpty(concepto);
    }

    private boolean isCantidadValid (double cantidad) {
        return cantidad > 0 || cantidad < 0;
    }

    private boolean isDateValid (Date fecha) {
        return fecha != null && !fecha.after(new Date());
    }

    public void editTransaccion(int id, String concepto, double cantidad, int idcuenta, int idcategoria, Date fecha) {
        if (isConceptoValid(concepto) && isCantidadValid(cantidad) && isDateValid(fecha)) {
            Transaccion transaccion = new Transaccion();
            transaccion.setId(id);
            transaccion.setConcepto(concepto);
            transaccion.setCantidad(cantidad);
            transaccion.setFecha(fecha);
            transaccion.setIdCuenta(idcuenta);
            transaccion.setIdCategoria(idcategoria);
            Result<Boolean> result = transaccionRepository.updateTransaccion(transaccion);
            if (result instanceof Result.Success) {
                Boolean data = ((Result.Success<Boolean>) result).getData();
                editTransaccionResult.setValue(new TransaccionResult(true));
            } else {
                editTransaccionResult.setValue(new TransaccionResult(R.string.error_editar_transaccion));
            }
        }
    }
}