package es.ucm.fdi.keeperly.ui.register;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;
import es.ucm.fdi.keeperly.repository.UsuarioRepository;

public class RegisterViewModel extends ViewModel {

    //Se definen variables mutables del registerFormState y registerResult para actualizar valores en el viewModel
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private UsuarioRepository usuarioRepository;

    RegisterViewModel() {
        this.usuarioRepository = RepositoryFactory.getInstance().getUsuarioRepository();
    }

    public MutableLiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public MutableLiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String email, String name, String password) {
        if (isEmailValid(email) && isNameValid(name) && isPasswordValid(password)) {

            Result<Usuario> result = usuarioRepository.register(email, name, password);

            if (result instanceof Result.Success) {
                Usuario data = ((Result.Success<Usuario>) result).getData();
                registerResult.setValue(new RegisterResult(new RegisteredUserView(data.getId(), data.getNombre())));
            } else {
                registerResult.setValue(new RegisterResult(R.string.register_failed));
            }
        }else {
            registerResult.setValue(new RegisterResult(R.string.register_failed_invalid_data));
        }
        registerFormState.setValue(new RegisterFormState(false));
    }

    public void registerDataChanged(String email, String name, String password, String repeatpassword) {
        if (!isNameValid(email)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_name, null, null));
        } else if (!isEmailValid(name)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_email, null, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_password, null));
        } else if (!isRepeatPasswordValid(password, repeatpassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, R.string.invalid_repeat_pw));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNameValid(String name) {
        if (name == null)
            return false;
        return !name.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isRepeatPasswordValid(String password, String repeatpw) {
        return Objects.equals(password, repeatpw);
    }
}
