package es.ucm.fdi.keeperly.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.TransaccionRepository;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> numDineroTotal;
    private final MutableLiveData<String> numTotalGastado;


    private final LoginRepository loginRepository;
    //private final CuentaRepository cuentaRepository;
    //private final TransaccionRepository transaccionRepository;

    //private final List<Cuenta> cuentas>;
    //private final List<Transaccion> transacciones>;


    public InicioViewModel() {
        welcomeText = new MutableLiveData<>();
        numDineroTotal = new MutableLiveData<>();
        numTotalGastado = new MutableLiveData<>();

        loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());

        //cuentasRepository = CuentaRepository.getInstance(RepositoryFactory.getInstance().getCuentaRepository());
        //transaccionRepository = TransaccionRepository.getInstance(RepositoryFactory.getInstance().getTransaccionRepository());


        welcomeText.setValue("Hola, " + loginRepository.getUser().getNombre());
        numDineroTotal.setValue("69.42€");
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
}