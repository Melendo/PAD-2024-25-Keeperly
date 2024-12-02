package es.ucm.fdi.keeperly.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;
import es.ucm.fdi.keeperly.repository.LoginRepository;
import es.ucm.fdi.keeperly.repository.RepositoryFactory;

public class CrearCuentaFragment extends Fragment {
    private CuentasViewModel cuentasViewModel;
    private EditText nombreT, balanceT;
    private Button cancelarB, crearB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_cuenta, container, false);
        //Vincula las vistas
        nombreT = view.findViewById(R.id.accountNameEditText);
        balanceT = view.findViewById(R.id.balanceEditText);
        //Inicializacion
        cuentasViewModel = new ViewModelProvider(this).get(CuentasViewModel.class);
        //Configurar botones
        crearB = view.findViewById(R.id.createAccountButton);
        crearB.setOnClickListener(v -> {
            String nombre = nombreT.getText().toString();
            String input = balanceT.getText().toString();
            double balance = Double.parseDouble(input);
            if (!nombre.isEmpty() && nombre.length() <= 30 && balance > 0.0) {
                LoginRepository loginRepository = LoginRepository.getInstance(
                        RepositoryFactory.getInstance().getUsuarioRepository()
                );
                int idUsuario = loginRepository.getLoggedUser().getId();
                cuentasViewModel.crearCuenta(nombre, balance, idUsuario);
            }
        });
        cancelarB = view.findViewById(R.id.cancelButton);
        cancelarB.setOnClickListener(v -> {
            // Aquí podrías limpiar los campos o cerrar el fragment
            Toast.makeText(requireContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
        });
        //Estado del insert
        cuentasViewModel.getOperationStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "Error al insertar la cuentaa", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(getContext(), "Error: el nombre debe tener entre 1 y 30 caracteres", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return view;
    }
}
