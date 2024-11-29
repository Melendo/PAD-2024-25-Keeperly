package es.ucm.fdi.keeperly.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.TransaccionRepository;

public class InicioViewModel extends ViewModel {
    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> numDineroTotal;
    private final MutableLiveData<String> numTotalGastado;
    private final LiveData<List<Cuenta>> cuentas;

    private final LoginRepository loginRepository;
    private final CuentaRepository cuentaRepository;

    public InicioViewModel() {

        this.loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());
        this.cuentaRepository = RepositoryFactory.getInstance().getCuentaRepository();

        this.cuentas = cuentaRepository.getAllCuentas(loginRepository.getLoggedUser().getId());


        welcomeText = new MutableLiveData<>();
        welcomeText.setValue("Hola, " + loginRepository.getLoggedUser().getNombre());

        numDineroTotal = new MutableLiveData<>();
        numDineroTotal.setValue("69.42€");

        numTotalGastado = new MutableLiveData<>();
        numTotalGastado.setValue("123.45€");

    }

    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<String> getPriceThisMonth() {
        return numDineroTotal;
    }

    public LiveData<String> getPriceLastMonth() {
        return numTotalGastado;
    }

    public LiveData<List<Cuenta>> getCuentas() {
        return cuentas;
    }
}