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

        cuentasAdapter.setOnCuentaClickListener(new CuentasAdapter.OnCuentaClickListener() {
            @Override
            public void onEditClick(Cuenta cuenta) {
                // Lógica para editar una cuenta
                mostrarDialogoEditarCuenta(cuenta);
            }

            @Override
            public void onDeleteClick(Cuenta cuenta) {
                // Lógica para eliminar una cuenta
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar Cuenta")
                        .setMessage("¿Seguro que deseas eliminar la cuenta " + cuenta.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> cuentasViewModel.delete(cuenta)) // Solo invoca delete
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
        cuentasViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1:
                        Toast.makeText(getContext(), "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show();
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "No se puede eliminar, es la única cuenta", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
                cuentasViewModel.resetDeleteStatus();
            }
        });
        cuentasViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1:
                        Toast.makeText(getContext(), "Cuenta actualizada con éxito", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(getContext(), "Error: el nombre debe tener entre 1 y 30 caracteres", Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(getContext(), "Error: el balance debe ser positivo", Toast.LENGTH_SHORT).show();
                        break;
                    case -4:
                        Toast.makeText(getContext(), "Error: la cuenta no existe", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido al actualizar la cuenta", Toast.LENGTH_SHORT).show();
                        break;
                }
                cuentasViewModel.resetUpdateStatus();
            }
        });
        return root;
    }

    private void mostrarDialogoEditarCuenta(Cuenta cuenta) {
        //Crea el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_cuenta, null);
        builder.setView(dialogView);
        //Elementos
        EditText editTextNombre = dialogView.findViewById(R.id.editTextNombreCuenta);
        EditText editTextBalance = dialogView.findViewById(R.id.editTextBalanceCuenta);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);
        //Rellena los campos con los datos actuales
        editTextNombre.setText(cuenta.getNombre());
        editTextBalance.setText(String.valueOf(cuenta.getBalance()));
        //Crea el objeto del dialogo
        AlertDialog dialog = builder.create();
        //Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        //Guardar
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString().trim();
            String nuevoBalanceT = editTextBalance.getText().toString().trim();
            //Comprueba los datos
            if (nuevoNombre.trim().isEmpty() || nuevoNombre.trim().length() > 30) {
                Toast.makeText(getContext(), "Error: el nombre debe tener entre 1 y 30 caracteres", Toast.LENGTH_SHORT).show();
            }
            else if (nuevoBalanceT.isEmpty()) {
                Toast.makeText(getContext(), "Error: el balance no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    //Convertir el balance ingresado a double
                    double nuevoBalance = Double.parseDouble(nuevoBalanceT);
                    if (nuevoBalance <= 0.0) {
                        Toast.makeText(getContext(), "Error: el balance debe ser positivo", Toast.LENGTH_SHORT).show();
                    }
                    else if (!nuevoNombre.equals(cuenta.getNombre()) || nuevoBalance != cuenta.getBalance()) {
                        // Actualizar los datos de la cuenta si hay cambios
                        cuenta.setNombre(nuevoNombre);
                        cuenta.setBalance(nuevoBalance);
                        cuentasViewModel.update(cuenta);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "No se realizaron cambios", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "El balance debe ser un número válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Muestra el dialogo
        dialog.show();
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