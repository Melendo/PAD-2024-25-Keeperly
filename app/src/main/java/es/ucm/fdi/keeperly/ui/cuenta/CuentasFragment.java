package es.ucm.fdi.keeperly.ui.cuenta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
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

    private ConnectivityManager connectivityManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cuentasViewModel = new ViewModelProvider(this).get(CuentasViewModel.class);

        binding = FragmentCuentasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Obtenemos una instancia del ConnectivityManager
        connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        Network network = connectivityManager.getActiveNetwork();
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);

        if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            // Hay conexión a internet

        } else {
            // No hay conexión a internet

        }

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


    private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            // La red está disponible, puedes reintentar la llamada a la API de PayPal si es necesario
        }

        @Override
        public void onLost(@NonNull Network network) {super.onLost(network);
            // La red se ha perdido, muestra un mensaje de error al usuario
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network,
                                          @NonNull NetworkCapabilities networkCapabilities)
        {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability
                    (NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}