package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.repository.CuentaRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CreateAccountViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateAccountViewModel.class)) {
            return (T) new CreateAccountViewModel(RepositoryFactory.getInstance().getCuentaRepository());
        } else {
        throw new IllegalArgumentException("Unkwown ViewModel class");
        }
    }
}
