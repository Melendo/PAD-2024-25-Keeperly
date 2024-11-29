package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;

public class PresupuestoDetalladoFragment extends Fragment {

    private CategoriasViewModel categoriasViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
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
        //TextView categoriaTextView = view.findViewById(R.id.textViewCategoriaValor);

        //Falta agregar categoria

        // Obtener datos desde el Bundle
        Bundle args = getArguments();
        if (args != null) {
            //String categoriaNombre = categoriasViewModel.getCategoriaById(args.getInt("categoriaId")).getNombre();
            nombreTextView.setText(args.getString("nombre", "N/A"));
            cantidadTextView.setText(args.getString("cantidad", "N/A"));
            gastadoTextView.setText(args.getString("gastado", "N/A"));
            fechaInicioTextView.setText(args.getString("fechaInicio", "N/A"));
            fechaFinTextView.setText(args.getString("fechaFin", "N/A"));
            //categoriaTextView.setText(args.getString("categoria", "N/A"));
        }
    }
}
