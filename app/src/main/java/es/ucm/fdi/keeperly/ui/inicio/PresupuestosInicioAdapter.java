package es.ucm.fdi.keeperly.ui.inicio;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        //Icono circular con la primera letra
        String initial = presupuesto.getNombre().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(initial);

    }

    @Override
    public int getItemCount() {
        return presupuestos.size();
    }

    static class PresupuestoInicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombreTextView;
        private final TextView dineroGastadoTextView;
        private final TextView iconoTextView;

        public PresupuestoInicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewPresupuestoMainNombre);
            dineroGastadoTextView = itemView.findViewById(R.id.textViewDineroGastadoPresupuestoMain);
            iconoTextView = itemView.findViewById(R.id.iconoPresupuestoInicioMain);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPresupuestos(List<Presupuesto> presupuestos) {
        this.presupuestos = presupuestos;
        notifyDataSetChanged();
    }
}
