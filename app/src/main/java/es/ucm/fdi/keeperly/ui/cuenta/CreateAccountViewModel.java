package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.repository.CuentaRepository;

public class CreateAccountViewModel extends ViewModel {
    private CuentaRepository cuentaRepository;
    private MutableLiveData<String> error = new MutableLiveData<>();
    //private MutableLiveData

    public CreateAccountViewModel(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public LiveData<String> getErrorMessage() {
        return error;
    }

    public void createAccount(String nombre, double balance, int id) {
        if (!validName(nombre)) {
            error.setValue("El nombre no es valido, debe tener entre 1 y 30 caracteres");
            return;
        }
        if (!validBalance(balance)) {
            error.setValue("El balance no es valido, debe ser positivo");
            return;
        }
        Cuenta nuevaC = new Cuenta();
        nuevaC.setNombre(nombre);
        nuevaC.setBalance(balance);
        nuevaC.setIdUsuario(id);
        cuentaRepository.insert(nuevaC);
    }

    private boolean validBalance(double balance) {
        return balance > 0;
    }

    private boolean validName(String nombre) {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 30; //.trim() elimina los espacios en blanco del inicio y final
    }
}
