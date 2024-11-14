package es.ucm.fdi.keeperly.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.R;

public class LoginViewModel extends ViewModel {

    //Se definen variables mutables del loginFormState y loginResult para actualizar valores en el viewModel
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    //Se definen la version de solo lectura de las variables para respetar encapsulamiento
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    //se llama loginRepository.login con usuario y contrasenia y crea un loginResult para el resultado (exito o fallo)
    public void login(String username, String password) {
        Result<Usuario> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            Usuario data = ((Result.Success<Usuario>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getNombre())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }
    //Validamos a tiempo de introduccion si los datos son correctos actualizando loginFormState
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // Valida el nombre de usuario (correo con @ o nombre de usuario)
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // Valida la contraseña con una longitud de min 5 caracteres
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}