package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.repository.AnadirCuentaRepository;
import es.ucm.fdi.keeperly.repository.CategoriaRepository;
import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CuentasViewModel extends ViewModel {
    private final MutableLiveData<String> mutableLiveData;
    private final CuentaRepository cuentaRepository;
    private final List<Cuenta> cuentas;


    public CuentasViewModel() {
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue("This is categories fragment");
        this.cuentaRepository = RepositoryFactory.getInstance().getCuentaRepository();
        int idUsuario = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository()).getLoggedUser().getId();
        this.cuentas = cuentaRepository.getAllCuentas(idUsuario);
    }

    public LiveData<String> getText() {
        return mutableLiveData;
    }

    public void crearCuenta(String nombre, double balance, int idUsuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setBalance(balance);
        cuenta.setIdUsuario(idUsuario);
        cuentaRepository.insert(cuenta); // Insertamos la cuenta en la base de datos
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public LiveData<Integer> getOperationStatus() {
        return cuentaRepository.getOperationStatus();
    }

    public void delete(Cuenta cuenta) {
        cuentaRepository.delete(cuenta);
    }

    public void update(Cuenta cuenta) {
        cuentaRepository.update(cuenta);
    }
}
