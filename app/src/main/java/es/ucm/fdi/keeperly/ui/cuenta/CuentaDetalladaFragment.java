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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CuentaDetalladaFragment extends Fragment {
    private CuentasViewModel cuentasViewModel;
    private TextView nombreT, balanceT, gastadoT;
    private Button eliminarB, editarB, sincPayPalB;

    private EditText etNombre, etBalance, etClientID, etSecret;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta_detallada, container, false);
        cuentasViewModel = new ViewModelProvider(this).get(CuentasViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vincular las vistas
        nombreT = view.findViewById(R.id.textViewNombreValor);
        balanceT = view.findViewById(R.id.textViewBalanceValor);
        eliminarB = view.findViewById(R.id.buttonEliminarC);
        editarB = view.findViewById(R.id.buttonEditarC);
        sincPayPalB = view.findViewById(R.id.sincPayPal);

        Cuenta cuenta = new Cuenta();
        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre", "N/A");
            double balance = Double.parseDouble(args.getString("balance", "0.0"));

            nombreT.setText(nombre);
            balanceT.setText(String.format("%.2f€", balance));

            // Cálculo del gasto

            cuenta.setId(args.getInt("id", 0));
            LoginRepository loginRepository = LoginRepository.getInstance(
                    RepositoryFactory.getInstance().getUsuarioRepository()
            );
            int idUsuario = loginRepository.getLoggedUser().getId();
            cuenta.setIdUsuario(idUsuario);
            cuenta.setNombre(nombre);
            cuenta.setBalance(balance);
        }

        // Funcionalidad de botones
        eliminarB.setOnClickListener(v -> eliminarCuenta(cuenta));

        editarB.setOnClickListener(v -> mostrarDialogoEditarCuenta(cuenta));

        RecyclerView recyclerViewTransacciones = view.findViewById(R.id.recyclerViewTransacciones);

        // Configurar el RecyclerView
        recyclerViewTransacciones.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Transaccion> transacciones = new ArrayList<>(); // Aquí cargamos la lista con datos
        CuentaDetalladaAdapter cuentaDetalladaAdapter = new CuentaDetalladaAdapter(transacciones);
        recyclerViewTransacciones.setAdapter(cuentaDetalladaAdapter);

        cuentasViewModel.getTransaccionesDeCuenta(cuenta).observe(getViewLifecycleOwner(), cuentaDetalladaAdapter::setTransacciones);

        sincPayPalB.setOnClickListener(v -> mostrarDialogoSyncPayPal());
    }

    private void mostrarDialogoSyncPayPal() {
        //Crea el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sincronizar_paypal, null);
        builder.setView(dialogView);

        //Elementos
        etClientID = dialogView.findViewById(R.id.editTextClientID);
        etSecret = dialogView.findViewById(R.id.editTextSecret);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnSincronizar);

        //Crea el objeto del dialogo
        AlertDialog dialog = builder.create();
        //Muestra el dialogo
        dialog.show();
        //Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        //Guardar
        btnGuardar.setOnClickListener(v -> {
            //Logica Sincronizacion
        });
    }

    private void eliminarCuenta(Cuenta cuenta) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Cuenta")
                .setMessage("¿Seguro que deseas eliminar la cuenta?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    cuentasViewModel.delete(cuenta);
                    cuentasViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), status -> {

                        if (status != null) {
                            switch (status) {
                                case 1:
                                    Toast.makeText(getContext(), "Cuenta eliminada con éxito", Toast.LENGTH_SHORT).show();
                                    getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
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
                })
                .setNegativeButton("Cancelar", null)
                .show();
        ;
    }

    private void mostrarDialogoEditarCuenta(Cuenta cuenta) {
        //Crea el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_cuenta, null);
        builder.setView(dialogView);

        //Elementos
        etNombre = dialogView.findViewById(R.id.editTextNombreCuenta);
        etBalance = dialogView.findViewById(R.id.editTextBalanceCuenta);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnSincronizar);

        //Rellena los campos con los datos actuales
        etNombre.setText(cuenta.getNombre());
        etBalance.setText(String.valueOf(cuenta.getBalance()));

        //Crea el objeto del dialogo
        AlertDialog dialog = builder.create();
        //Muestra el dialogo
        dialog.show();
        //Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        //Guardar
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString().trim();
            String nuevoBalanceT = etBalance.getText().toString().trim();

            //Comprueba los datos
            if (validarCampos()) {
                try {
                    //Convertir el balance ingresado a double
                    double nuevoBalance = Double.parseDouble(nuevoBalanceT);
                    // Actualizar los datos de la cuenta
                    cuenta.setNombre(nuevoNombre);
                    cuenta.setBalance(nuevoBalance);
                    cuentasViewModel.update(cuenta);

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "El balance debe ser un número válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Observa el estado del Update
        cuentasViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1://Exito
                        Toast.makeText(getContext(), "Cuenta modificado con éxito", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getParentFragmentManager().popBackStack();
                        break;
                    case -1://Errores
                        Toast.makeText(getContext(), "Error al modificar la cuenta", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(getContext(), "Error: Los campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
                        break;
                    case -4:
                        Toast.makeText(getContext(), "Error: la cuenta non existe", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    private boolean validarCampos() {
        if (etNombre.getText().toString().isEmpty() || etNombre.getText().toString().length() > 30) {
            etNombre.setError("El nombre es obligatorio");
            return false;
        }

        if (etBalance.getText().toString().isEmpty()) {
            etBalance.setError("La cantidad es obligatoria");
            return false;
        }


        return true;
    }
}
