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

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentaDetalladaFragment extends Fragment {
    private CuentasViewModel cuentasViewModel;
    private TextView nombreT, balanceT, gastadoT;
    private Button cancelarB, editarB;

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
        //Vincula las vistas
        nombreT = view.findViewById(R.id.textViewNombreValor);
        balanceT = view.findViewById(R.id.textViewBalanceValor);
        gastadoT = view.findViewById(R.id.textViewGastadoCValor);
        cancelarB = view.findViewById(R.id.buttonEliminarC);
        editarB = view.findViewById(R.id.buttonEditarC);
        //Faltaria la lista de transacciones

        //Obtiene los datos
        Cuenta cuenta = new Cuenta();
        Bundle args = getArguments();
        if (args != null) {
            //Carga los datos
            cuenta.setId(args.getInt("id", 0));
            cuenta.setNombre(args.getString("nombre", "N/A"));
            cuenta.setBalance(Double.parseDouble(args.getString("balance", "0.0")));
            double gastado = cuentasViewModel.getGastoTotal(cuenta);
            cuenta.setGastado(gastado * (-1));
            //Setters datos
            nombreT.setText(cuenta.getNombre());
            balanceT.setText(String.format("%.2f€", cuenta.getBalance()));
            gastadoT.setText(String.format("%.2f€", gastado));
        }
        //Funcionalidad botones
        cancelarB.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Eliminar Cuenta")
                    .setMessage("¿Seguro que deseas eliminar la cuenta " + cuenta.getNombre() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> cuentasViewModel.delete(cuenta)) // Solo invoca delete
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
        editarB.setOnClickListener(v -> {
            mostrarDialogoEditarCuenta(cuenta);
        });
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
}
