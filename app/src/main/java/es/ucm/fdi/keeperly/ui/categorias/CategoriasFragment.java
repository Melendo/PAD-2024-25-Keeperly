package es.ucm.fdi.keeperly.ui.categorias;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ucm.fdi.keeperly.R;

public class CategoriasFragment extends Fragment {

    private CategoriasViewModel mViewModel;

    public static CategoriasFragment newInstance() {
        return new CategoriasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        // TODO: Use the ViewModel
    }

}