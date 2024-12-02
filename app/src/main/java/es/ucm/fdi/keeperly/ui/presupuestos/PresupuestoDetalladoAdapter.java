package es.ucm.fdi.keeperly.ui.presupuestos;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

public class PresupuestoDetalladoAdapter extends RecyclerView.Adapter<PresupuestoDetalladoAdapter.PresupuestoViewHolder> {

    private List<Transaccion> transacciones;

    public PresupuestoDetalladoAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @NonNull
    @Override
    public PresupuestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_transacciones_presupuesto_cuenta, parent, false);
        return new PresupuestoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresupuestoViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        holder.textViewNombre.setText(transaccion.getConcepto());
        holder.textViewCantidad.setText(String.format("%.2f €", transaccion.getCantidad()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fechaFormateada = dateFormat.format(transaccion.getFecha());
        holder.textViewFecha.setText(fechaFormateada);
        holder.iconoTransaccion.setText(String.valueOf(transaccion.getConcepto().charAt(0)).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public static class PresupuestoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCantidad, textViewFecha, textViewCategoria, iconoTransaccion;

        public PresupuestoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewTransaccionNombre);
            textViewCantidad = itemView.findViewById(R.id.textViewTransaccionCantidad);
            textViewFecha = itemView.findViewById(R.id.textViewTransaccionFecha);
            iconoTransaccion = itemView.findViewById(R.id.iconoTransaccion);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
        notifyDataSetChanged();
    }
}


