package es.ucm.fdi.keeperly.ui.presupuestos;

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


public class PresupuestosAdapter extends RecyclerView.Adapter<PresupuestosAdapter.PresupuestosViewHolder>{
    private List<Presupuesto> presupuestos = new ArrayList<>();
    private PresupuestosAdapter.OnPresupuestoClickListener listener;

    public interface OnPresupuestoClickListener {

    }

    public void setOnPresupuestoClickListener(PresupuestosAdapter.OnPresupuestoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PresupuestosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_presupuestos, parent, false);
        return new PresupuestosAdapter.PresupuestosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestosViewHolder holder, int position) {
        Presupuesto presupuesto = presupuestos.get(position);
        holder.nombreTextView.setText(presupuesto.getNombre());

        // Icono circular con la primera letra
        String inicial = presupuesto.getNombre().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(inicial);




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


        public PresupuestosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewPresupuestoNombre);
            iconoTextView = itemView.findViewById(R.id.iconoPresupuesto);
        }
    }
}
