package es.ucm.fdi.keeperly.ui.inicio;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;

public class PresupuestosInicioAdapter extends RecyclerView.Adapter<PresupuestosInicioAdapter.PresupuestoInicioViewHolder>{

    // Lista de presupuestos
    private List<Presupuesto> presupuestos = new ArrayList<>();

    @NonNull
    @Override
    public PresupuestosInicioAdapter.PresupuestoInicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// Inflar cada elemento del RecyclerView y devolver un ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_card_presupuesto, parent, false);
        return new PresupuestoInicioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestosInicioAdapter.PresupuestoInicioViewHolder holder, int position) {// Configurar los datos de cada elemento del RecyclerView
        Presupuesto presupuesto = presupuestos.get(position);

        holder.nombreTextView.setText(presupuesto.getNombre());
        holder.dineroGastadoTextView.setText(String.valueOf(presupuesto.getGastado()));
        holder.dineroGastadoTextView.setText(String.format("%.2f€", presupuesto.getGastado()) + " / " + String.format("%.2f€", presupuesto.getCantidad()));

        //Icono circular con la primera letra
        String initial = presupuesto.getNombre().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(initial);

        //Sacar el valor android:progress de la barra de progreso en base a (dinero gastado/dinero total) * 100 o 100 si es mayor
        int progress = (int) ((presupuesto.getGastado() / presupuesto.getCantidad()) * 100);
        if (progress > 100) {
            progress = 100;
        }
        holder.progressBar.setProgress(progress);



    }

    @Override
    public int getItemCount() {
        return presupuestos.size();
    }

    static class PresupuestoInicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombreTextView;
        private final TextView dineroGastadoTextView;
        private final TextView iconoTextView;
        private final ProgressBar progressBar;

        public PresupuestoInicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewPresupuestoMainNombre);
            dineroGastadoTextView = itemView.findViewById(R.id.textViewDineroGastadoPresupuestoMain);
            iconoTextView = itemView.findViewById(R.id.iconoPresupuestoInicioMain);
            progressBar = itemView.findViewById(R.id.progressBarPresupuestoMain);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
        notifyDataSetChanged();
    }
}
