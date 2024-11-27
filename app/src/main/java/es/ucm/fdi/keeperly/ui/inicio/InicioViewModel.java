package es.ucm.fdi.keeperly.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> numDineroTotal;
    private final MutableLiveData<String> numTotalGastado;;

    private final LoginRepository loginRepository;

    public InicioViewModel() {
        welcomeText = new MutableLiveData<>();
        numDineroTotal = new MutableLiveData<>();
        numTotalGastado = new MutableLiveData<>();

        loginRepository = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository());


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