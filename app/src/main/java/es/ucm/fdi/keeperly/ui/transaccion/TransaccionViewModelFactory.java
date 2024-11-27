package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TransaccionViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransaccionViewModel.class)) {
            return (T) new TransaccionViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
        }
    }
