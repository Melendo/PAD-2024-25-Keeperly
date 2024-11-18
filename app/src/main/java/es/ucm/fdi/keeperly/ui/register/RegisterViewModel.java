package es.ucm.fdi.keeperly.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.keeperly.repository.RegisterRepository;

public class RegisterViewModel extends ViewModel {

    //Se definen variables mutables del registerFormState y registerResult para actualizar valores en el viewModel
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;
}
