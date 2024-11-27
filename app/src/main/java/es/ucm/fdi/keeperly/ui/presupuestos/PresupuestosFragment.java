package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.databinding.FragmentPresupuestosBinding;


public class PresupuestosFragment extends Fragment {

    private FragmentPresupuestosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PresupuestosViewModel presupuestosViewModel =
                new ViewModelProvider(this).get(PresupuestosViewModel.class);

        binding = FragmentPresupuestosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fabCrearPresupuesto = root.findViewById(R.id.fab_crear_presupuesto);

        fabCrearPresupuesto.setOnClickListener(v -> {
            // Navegación hacia el formulario de creación de presupuesto
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_budgetsFragment_to_crearPresupuestoFragment);
        });

        final TextView textView = binding.textBudgets;
        presupuestosViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}