package es.ucm.fdi.keeperly.ui.inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends ViewModel {

    private final MutableLiveData<String> welcomeText;
    private final MutableLiveData<String> priceThisMonth;
    private final MutableLiveData<String> priceLastMonth;
    private final MutableLiveData<Integer> progressBarThisMonth;
    private final MutableLiveData<Integer> progressBarLastMonth;

    public InicioViewModel() {
        welcomeText = new MutableLiveData<>();
        priceThisMonth = new MutableLiveData<>();
        priceLastMonth = new MutableLiveData<>();
        progressBarThisMonth = new MutableLiveData<>();
        progressBarLastMonth = new MutableLiveData<>();


        welcomeText.setValue("Bienvenido, Iván");
        priceThisMonth.setValue("69.42€");
        priceLastMonth.setValue("123.45€");
        progressBarThisMonth.setValue(45);
        progressBarLastMonth.setValue(80);
    }

    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<String> getPriceThisMonth() {
        return priceThisMonth;
    }

    public LiveData<String> getPriceLastMonth() {
        return priceLastMonth;
    }

    public LiveData<Integer> getProgressBarThisMonth() {
        return progressBarThisMonth;
    }

    public LiveData<Integer> getProgressBarLastMonth() {
        return progressBarLastMonth;
    }

}