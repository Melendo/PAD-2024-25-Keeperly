package es.ucm.fdi.keeperly.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.service.UsuarioSA;

/**
 * Factoria para el LoginViewModel
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance(new UsuarioSA()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}