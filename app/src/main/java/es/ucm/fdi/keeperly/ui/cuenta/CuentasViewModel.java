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

    public CuentasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is accounts fragment");
        this.cuentaRepository = RepositoryFactory.getInstance().getCuentaRepository();
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
}