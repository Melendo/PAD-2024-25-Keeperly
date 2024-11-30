package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;

public class PresupuestoDetalladoFragment extends Fragment {

    private CategoriasViewModel categoriasViewModel;
    private PresupuestosViewModel presupuestosViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        presupuestosViewModel = new ViewModelProvider(this).get(PresupuestosViewModel.class);
        return inflater.inflate(R.layout.fragment_presupuesto_detallado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener referencias a los TextView

        TextView nombreTextView = view.findViewById(R.id.textViewNombreValor);
        TextView cantidadTextView = view.findViewById(R.id.textViewCantidadValor);
        TextView gastadoTextView = view.findViewById(R.id.textViewGastadoValor);
        TextView fechaInicioTextView = view.findViewById(R.id.textViewFechaInicioValor);
        TextView fechaFinTextView = view.findViewById(R.id.textViewFechaFinValor);
        TextView categoriaTextView = view.findViewById(R.id.textViewCategoriaValor);

        Presupuesto presupuesto = new Presupuesto();

        // Obtener datos desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            //Cargar datos del presupuesto
            presupuesto.setId(args.getInt("id", 0));
            presupuesto.setNombre(args.getString("nombre", "N/A"));
            //Settear datos del presupuesto
            nombreTextView.setText(args.getString("nombre", "N/A"));
            cantidadTextView.setText(args.getString("cantidad", "N/A"));
            gastadoTextView.setText(args.getString("gastado", "N/A"));
            fechaInicioTextView.setText(args.getString("fechaInicio", "N/A"));
            fechaFinTextView.setText(args.getString("fechaFin", "N/A"));
            categoriaTextView.setText(args.getString("categoria", "N/A"));
        }

        //Obtener referencias a los botones
        Button buttonEliminar = view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(v -> eliminarPresupuesto(presupuesto));

    }

    void eliminarPresupuesto(Presupuesto presupuesto) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Presupuesto")
                .setMessage("¿Seguro que deseas eliminar el presupuesto " + presupuesto.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    presupuestosViewModel.eliminarPresupuesto(presupuesto);
                    presupuestosViewModel.getDeleteStatus().observe(getViewLifecycleOwner(), status -> {
                        if (status != null) {
                            if (status) {
                                Toast.makeText(getContext(), "Presupuesto eliminado con éxito", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack(); // Redirige a la vista anterior
                            } else {
                                Toast.makeText(getContext(), "Error al eliminar la categoria", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoEditarCategoria(Presupuesto presupuesto){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_categoria, null);
        builder.setView(dialogView);

        // Referencias a los elementos del diálogo
    }

}
