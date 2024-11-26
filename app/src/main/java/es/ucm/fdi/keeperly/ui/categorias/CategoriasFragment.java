package es.ucm.fdi.keeperly.ui.categorias;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.databinding.FragmentCategoriasBinding;


public class CategoriasFragment extends Fragment {

    private CategoriasViewModel mViewModel;
    private FragmentCategoriasBinding binding;


    public static CategoriasFragment newInstance() {
        return new CategoriasFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CategoriasViewModel categoriasViewModel =
                new ViewModelProvider(this).get(CategoriasViewModel.class);

        binding = FragmentCategoriasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fabCrearCategoria = root.findViewById(R.id.fab_crear_categoria);

        fabCrearCategoria.setOnClickListener(v -> {
            // Navegación hacia el formulario de creación de presupuesto
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_categoriasFragment_to_crearCategoriaFragment);
        });

        final TextView textView = binding.textCategorias;
        categoriasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

}