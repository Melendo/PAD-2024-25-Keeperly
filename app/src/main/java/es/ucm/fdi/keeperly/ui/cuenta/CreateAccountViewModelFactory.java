package es.ucm.fdi.keeperly.ui.cuenta;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.repository.AnadirCuentaRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CreateAccountViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CreateAccountViewModel.class)) {
            return (T) new CreateAccountViewModel(AnadirCuentaRepository.getInstance(RepositoryFactory.getInstance().getCuentaRepository()));
        } else {
        throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
