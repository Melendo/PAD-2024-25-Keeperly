package es.ucm.fdi.keeperly.ui.cuentas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CuentasViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CuentasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is accounts fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}