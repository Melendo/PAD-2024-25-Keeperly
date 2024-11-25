package es.ucm.fdi.keeperly.ui.presupuestos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PresupuestosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PresupuestosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is budgets fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}