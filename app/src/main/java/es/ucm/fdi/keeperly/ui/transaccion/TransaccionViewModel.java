package es.ucm.fdi.keeperly.ui.transaccion;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.Date;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.TransaccionRepository;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.ui.register.RegisterFormState;

public class TransaccionViewModel extends ViewModel {
    private MutableLiveData<CreateTransaccionFormState>  createTransaccionFormState = new MutableLiveData<>();
    private MutableLiveData<CreateTransaccionResult> createTransaccionResult = new MutableLiveData<>();
    private TransaccionRepository transaccionRepository;

    TransaccionViewModel() {
        this.transaccionRepository = RepositoryFactory.getInstance().getTransactionRepository();
    }

    public MutableLiveData<CreateTransaccionFormState> getCreateTransaccionFormState() {
        return createTransaccionFormState;
    }

    public MutableLiveData<CreateTransaccionResult> getCreateTransaccionResult() {
        return createTransaccionResult;
    }

    public void createTransaccion(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        if (isConceptoValid(concepto) && isCantidadValid(cantidad) && isDateValid(fecha)) {
            Result<Transaccion> result = transaccionRepository.createTransaccion(concepto, cantidad, cuenta, categoria, fecha);

            if(result instanceof Result.Success) {
                Transaccion data = ((Result.Success<Transaccion>) result).getData();
                createTransaccionResult.setValue(new CreateTransaccionResult(true));
            } else {
                createTransaccionResult.setValue(new CreateTransaccionResult(R.string.crear_transaccion_fallado));
            }
        } else {
            createTransaccionResult.setValue(new CreateTransaccionResult(R.string.crear_transaccion_datos_invalidos));
        }
        createTransaccionFormState.setValue(new CreateTransaccionFormState(false));
    }

    public void createTransaccionDataChanged(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        if(!isConceptoValid(concepto)) {
            createTransaccionFormState.setValue(new CreateTransaccionFormState(R.string.concepto_invalido, null, null, null, null));
        } else if (!isCantidadValid(cantidad)) {
            createTransaccionFormState.setValue(new CreateTransaccionFormState(null, R.string.cantidad_invalida, null, null, null));
        } else if (!isDateValid(fecha)) {
            createTransaccionFormState.setValue(new CreateTransaccionFormState(null, null, null, null, R.string.fecha_invalida));
        } else {
            createTransaccionFormState.setValue(new CreateTransaccionFormState(true));
        }
    }

    private boolean isConceptoValid (String concepto) {
        return !TextUtils.isEmpty(concepto);
    }

    private boolean isCantidadValid (double cantidad) {
        return cantidad > 0;
    }

    private boolean isDateValid (Date fecha) {
        return !fecha.after(new Date());
    }
}