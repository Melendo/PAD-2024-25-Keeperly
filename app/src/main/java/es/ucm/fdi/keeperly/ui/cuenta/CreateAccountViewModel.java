package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.repository.AnadirCuentaRepository;

public class CreateAccountViewModel extends ViewModel {
    private AnadirCuentaRepository anadirCuentaRepository;
    private MutableLiveData<CuentaResult> cuentaResult = new MutableLiveData<>();
    private MutableLiveData<CuentaFormState> cuentaFormState = new MutableLiveData<>();

    public CreateAccountViewModel(AnadirCuentaRepository anadirCuentaRepository) {
        this.anadirCuentaRepository = anadirCuentaRepository;
    }

    LiveData<CuentaResult> getCuentaResult() {
        return cuentaResult;
    }

    LiveData<CuentaFormState> getCuentaFormState() {
        return cuentaFormState;
    }

    public void createAccount(String nombre, double balance, int idUsuario) {
        if (!validName(nombre)) {
            cuentaResult.setValue(new CuentaResult(R.string.invalid_accountname));
            return;
        }
        if (!validBalance(balance)) {
            cuentaResult.setValue(new CuentaResult(R.string.invalid_balance));
            return;
        }
        Result<Cuenta> result = anadirCuentaRepository.anadirCuenta(nombre, balance, idUsuario);
        if (result instanceof Result.Success) {
            Cuenta data = ((Result.Success<Cuenta>) result).getData();
            cuentaResult.setValue(new CuentaResult(new CreatedAccountView(data.getNombre())));
        } else {
            cuentaResult.setValue(new CuentaResult(R.string.create_account_failed));
        }
    }

    public void createAccountDataChanged(String nombre, double balance) {
        if (!validName(nombre)) {
            cuentaFormState.setValue(new CuentaFormState(R.string.invalid_accountname, null));
        } else if (!validBalance(balance)) {
            cuentaFormState.setValue(new CuentaFormState(null, R.string.invalid_balance));
        } else {
            cuentaFormState.setValue(new CuentaFormState(true));
        }
    }

    private boolean validBalance(double balance) {
        return balance > 0;
    }

    private boolean validName(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.trim().length() <= 30; //.trim() elimina los espacios en blanco del inicio y final
    }
}
