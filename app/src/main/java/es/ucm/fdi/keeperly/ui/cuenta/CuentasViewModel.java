package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CuentasViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private final CuentaRepository cuentaRepository;
    private final LiveData<List<Cuenta>> cuentas;

    public CuentasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is accounts fragment");
        this.cuentaRepository = RepositoryFactory.getInstance().getCuentaRepository();
        // Obtiene el ID del usuario logueado
        int idUsuario = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository()).getLoggedUser().getId();
        // Recupera las cuentas para ese usuario
        this.cuentas = cuentaRepository.getAllCuentas(idUsuario);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void crearCuenta(String nombre, double balance, int idUsuario) {
        cuentaRepository.insert(cuentaRepository.creaCuenta(nombre, balance, idUsuario));
    }

    public LiveData<Integer> getOperationStatus() {
        return cuentaRepository.getOperationStatus();
    }

    public LiveData<List<Cuenta>> getCuentas() {
        return cuentas;
    }

    public void delete(Cuenta cuenta) {
        cuentaRepository.delete(cuenta);
    }

    public void update(Cuenta cuenta) {
        cuentaRepository.update(cuenta);
    }

    public LiveData<Integer> getDeleteStatus() {
        return cuentaRepository.getDeleteStatus();
    }

    public void resetDeleteStatus() {
        cuentaRepository.resetDeleteStatus();
    }

    public LiveData<Integer> getUpdateStatus() {
        return cuentaRepository.getUpdateStatus();
    }

    public  void resetUpdateStatus() {
        cuentaRepository.resetUpdateStatus();
    }
}