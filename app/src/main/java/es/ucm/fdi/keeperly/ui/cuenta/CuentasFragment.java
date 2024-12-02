package es.ucm.fdi.keeperly.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.databinding.FragmentCuentasBinding;
import es.ucm.fdi.keeperly.ui.cuenta.CuentasViewModel;


public class CuentasFragment extends Fragment {
    private FragmentCuentasBinding binding;
    private CuentasViewModel cuentasViewModel;
    private CuentasAdapter cuentasAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cuentasViewModel = new ViewModelProvider(this).get(CuentasViewModel.class);

        binding = FragmentCuentasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCuentas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        cuentasAdapter = new CuentasAdapter();
        recyclerView.setAdapter(cuentasAdapter);

        FloatingActionButton fabCrearCuenta = root.findViewById(R.id.fab_crear_cuenta);
        fabCrearCuenta.setOnClickListener(v -> {
            // Navegación hacia el formulario de creación de presupuesto
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_cuentasFragment_to_crearCuentaFragment);
        });

        cuentasViewModel.getCuentas().observe(getViewLifecycleOwner(), cuentasAdapter::setCuentas);

        cuentasAdapter.setOnCuentaClickListener(cuenta -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", cuenta.getId());
            bundle.putString("nombre", cuenta.getNombre());
            bundle.putString("balance", String.valueOf(cuenta.getBalance()));

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_cuentasFragment_to_cuentaDetalladaFragment, bundle);
        });


        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}