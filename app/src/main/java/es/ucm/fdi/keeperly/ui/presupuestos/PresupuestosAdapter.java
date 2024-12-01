package es.ucm.fdi.keeperly.ui.presupuestos;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

public class PresupuestosAdapter extends RecyclerView.Adapter<PresupuestosAdapter.PresupuestosViewHolder> {
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private OnPresupuestoClickListener listener;
    private PresupuestosViewModel presupuestosViewModel;


    public interface OnPresupuestoClickListener {
        void onPresupuestoClick(Presupuesto presupuesto);
    }


    public void setOnPresupuestoClickListener(OnPresupuestoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PresupuestosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_card_presupuesto, parent, false);
        return new PresupuestosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestosViewHolder holder, int position) {
        Presupuesto presupuesto = presupuestos.get(position);
        presupuestosViewModel  = new PresupuestosViewModel();
        double gastado = presupuestosViewModel.getTotalGastadoEnPresupuesto(presupuesto);
        holder.nombreTextView.setText(presupuesto.getNombre());
        holder.iconoTextView.setText(presupuesto.getNombre().substring(0, 1).toUpperCase());
        holder.dineroTextView.setText(String.format("%.2f€",gastado) + " / " + String.format("%.2f€", presupuesto.getCantidad()));
        //Sacar el valor android:progress de la barra de progreso en base a (dinero gastado/dinero total) * 100 o 100 si es mayor
        int progress = 0; // Initialize to 0
        if (presupuesto.getCantidad() != 0) {
            progress = (int) ((gastado / presupuesto.getCantidad()) * 100);
            if (progress > 100) {
                progress = 100;
            }
        }
        holder.progressBar.setProgress(progress);
        // Formatear las fechas
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fechaInicio = dateFormat.format(presupuesto.getFechaInicio());
        String fechaFin = dateFormat.format(presupuesto.getFechaFin());
        presupuestosViewModel = new PresupuestosViewModel();
        String nombreCategoria = presupuestosViewModel.getNombreCategoria(presupuesto.getIdCategoria());

        // Configurar el click listener
        holder.itemView.setOnClickListener(v -> {

            Bundle args = new Bundle();

            args.putInt("id", presupuesto.getId());
            args.putString("nombre", presupuesto.getNombre());
            args.putString("cantidad", String.valueOf(presupuesto.getCantidad()));
            args.putString("gastado", String.valueOf(gastado));
            args.putString("fechaInicio", fechaInicio);
            args.putString("fechaFin", fechaFin);
            args.putString("categoria", nombreCategoria);
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.presupuestoDetalladoFragment, args);
        });
    }

    @Override
    public int getItemCount() {
        return presupuestos.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
        notifyDataSetChanged();
    }


    static class PresupuestosViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreTextView;
        private final TextView iconoTextView;
        private final TextView dineroTextView;
        private final ProgressBar progressBar;

        public PresupuestosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewPresupuestoMainNombre);
            iconoTextView = itemView.findViewById(R.id.iconoPresupuestoInicioMain);
            dineroTextView = itemView.findViewById(R.id.textViewDineroGastadoPresupuestoMain);
            progressBar = itemView.findViewById(R.id.progressBarPresupuestoMain);
        }
    }
}
