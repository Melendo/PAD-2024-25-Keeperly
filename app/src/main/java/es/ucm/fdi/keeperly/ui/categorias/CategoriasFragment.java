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
import android.widget.TextView;
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
        CategoriasViewModel categoriasViewModel =
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
            // Navegación hacia el formulario de creación de presupuesto
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_menu);
            navController.navigate(R.id.action_categoriasFragment_to_crearCategoriaFragment);
        });
        categoriaViewModel = new CategoriasViewModel();
        categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categoriaAdapter::setCategorias);

        categoriaAdapter.setOnCategoriaClickListener(new CategoriasAdapter.OnCategoriaClickListener() {
            @Override
            public void onEditClick(Categoria categoria) {
                // Lógica para editar la categoría
                mostrarDialogoEditarCategoria(categoria);
            }

            @Override
            public void onDeleteClick(Categoria categoria) {
                // Lógica para eliminar la categoría
                new AlertDialog.Builder(getContext())
                        .setTitle("Eliminar Categoría")
                        .setMessage("¿Seguro que deseas eliminar la categoría " + categoria.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            categoriaViewModel.delete(categoria);
                            Toast.makeText(getContext(), "Categoría " + categoria.getNombre() + " eliminada", Toast.LENGTH_SHORT).show();
                            categoriaViewModel.getCategorias().observe(getViewLifecycleOwner(), categoriaAdapter::setCategorias);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        return root;
    }

    private void mostrarDialogoEditarCategoria(Categoria categoria) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_categoria, null);
        builder.setView(dialogView);

        // Referencias a los elementos del diálogo
        EditText editTextNombre = dialogView.findViewById(R.id.editTextNombreCategoria);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);

        // Prellenar el nombre actual de la categoría
        editTextNombre.setText(categoria.getNombre());

        AlertDialog dialog = builder.create();

        // Botón Cancelar
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        // Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = editTextNombre.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                // Actualiza el nombre de la categoría
                categoria.setNombre(nuevoNombre);
                categoriaViewModel.update(categoria); // Llamar al método update del ViewModel
                Toast.makeText(getContext(), "Categoría actualizada", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}