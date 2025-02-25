package es.ucm.fdi.keeperly.ui.categorias;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.databinding.FragmentCategoriasBinding;


public class CategoriasFragment extends Fragment {

    private CategoriasViewModel categoriaViewModel;
    private FragmentCategoriasBinding binding;
    private CategoriasAdapter categoriaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        categoriaViewModel =
                new ViewModelProvider(this).get(CategoriasViewModel.class);

        binding = FragmentCategoriasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        categoriaAdapter = new CategoriasAdapter();
        recyclerView.setAdapter(categoriaAdapter);


        FloatingActionButton fabCrearCategoria = root.findViewById(R.id.fab_crear_categoria);

        fabCrearCategoria.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_categoriasFragment_to_crearCategoriaFragment);
        });

        categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categoriaAdapter::setCategorias);

        categoriaAdapter.setOnCategoriaClickListener(new CategoriasAdapter.OnCategoriaClickListener() {
            @Override
            public void onEditClick(Categoria categoria) {
                mostrarDialogoEditarCategoria(categoria);
            }

            @Override
            public void onDeleteClick(Categoria categoria) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar Categoría")
                        .setMessage("¿Seguro que deseas eliminar la categoría " + categoria.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            categoriaViewModel.delete(categoria);
                            categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categoriaAdapter::setCategorias);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        categoriaViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Categoria eliminada con éxito", Toast.LENGTH_SHORT).show();
                        break;
                    case -1: // Error de base de datos
                        Toast.makeText(getContext(), "Error al eliminar la categoria", Toast.LENGTH_SHORT).show();
                        break;
                    case -2: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: la Categoria está asignada a Presupuestos", Toast.LENGTH_SHORT).show();
                        break;
                    case -3: // Otro error (opcional)
                        Toast.makeText(getContext(), "Error: la Categoria está asignada a transacciones", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        categoriaViewModel.getUpdateStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case 1: // Éxito
                        Toast.makeText(getContext(), "Categoria Editada con éxito", Toast.LENGTH_SHORT).show();
                        break;
                    case -1: // Errores
                        Toast.makeText(getContext(), "Error al editar la categoria", Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(getContext(), "Error: el nombre no puede ser vacio", Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(getContext(), "Error: Ya existe una Categoria con ese nombre", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return root;
    }

    private void mostrarDialogoEditarCategoria(Categoria categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_categoria, null);
        builder.setView(dialogView);

        EditText editTextNombre = dialogView.findViewById(R.id.editTextNombreCategoria);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnSincronizar);

        // Prellenar el nombre actual de la categoría
        editTextNombre.setText(categoria.getNombre());

        AlertDialog dialog = builder.create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString().trim();
            if (!nuevoNombre.isEmpty() && !nuevoNombre.equals(categoria.getNombre())) {
                // Actualiza el nombre de la categoría
                categoria.setNombre(nuevoNombre);
                categoriaViewModel.update(categoria);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}