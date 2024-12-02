package es.ucm.fdi.keeperly.ui.cuenta;

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

public class CuentaDetalladaAdapter extends RecyclerView.Adapter<CuentaDetalladaAdapter.CuentaViewHolder> {

    private List<Transaccion> transacciones;

    public CuentaDetalladaAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_transacciones_presupuesto_cuenta, parent, false);
        return new CuentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
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

    public static class CuentaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCantidad, textViewFecha, textViewCategoria, iconoTransaccion;

        public CuentaViewHolder(@NonNull View itemView) {
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


