package es.ucm.fdi.keeperly.ui.categorias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Date;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.ui.presupuestos.PresupuestosViewModel;

public class CrearCategoriaFragment extends Fragment {

    private CategoriasViewModel categoriasViewModel;
    private EditText etNombre;
    private Button btnCrear, btnCancelar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crear_categoria, container, false);

        // Inicializamos las vistas
        etNombre = rootView.findViewById(R.id.etNombre);

        // Inicializamos el ViewModel
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);

        // Configurar el botón de crear presupuesto
        btnCrear = rootView.findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            if (!nombre.isEmpty()) {
                categoriasViewModel.crearCategoria(nombre);
            }
        });

        btnCancelar = rootView.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> {
            // Aquí podrías limpiar los campos o cerrar el fragment
            Toast.makeText(requireContext(), "Operación cancelada", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
        });

        // Observa el estado del update
        categoriasViewModel.getOperationStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Categoria creada con éxito", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "Error al insertar la categoria", Toast.LENGTH_SHORT).show();
                        break;
                    case -2: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: el nombre es vacio", Toast.LENGTH_SHORT).show();
                        break;
                    case -3: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: la Categoria ya existe", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return rootView;
    }

}
