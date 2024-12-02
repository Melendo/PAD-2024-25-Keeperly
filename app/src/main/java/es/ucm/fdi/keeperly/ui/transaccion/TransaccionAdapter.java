package es.ucm.fdi.keeperly.ui.transaccion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {

    private List<TransaccionconCategoria> transacciones;
    private OnTransaccionClickListener listener;
    private TransaccionViewModel transaccionViewModel;

    public TransaccionAdapter(TransaccionViewModel transaccionViewModel) {
        this.transaccionViewModel = transaccionViewModel;
    }

    public void setTransacciones(List<TransaccionconCategoria> transacciones) {
        this.transacciones = transacciones;
        notifyDataSetChanged();
    }

    public interface OnTransaccionClickListener {
        void onEditClick(TransaccionconCategoria transaccion);
        void onDeleteClick(TransaccionconCategoria transaccion);
    }

    public void setOnTransaccionClickListener(OnTransaccionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransaccionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_transaccion, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransaccionViewHolder holder, int position) {
        // Configuracion de los campos

        TransaccionconCategoria transaccion = transacciones.get(position);

        holder.nombreTextView.setText(transaccion.getTransaccion().getConcepto());

        String cantidad = String.format("%.2f€", transaccion.getTransaccion().getCantidad());
        holder.cantidadTextView.setText(cantidad);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date fecha = null;
        try {
            fecha = dateFormat.parse(transaccion.getTransaccion().getFecha().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        holder.fechaTextView.setText(fecha.toString());

        String cuenta = transaccionViewModel.getCategoriaCuenta(transaccion.getTransaccion());
        holder.cuentaTextView.setText("Cuenta" + ": " + cuenta);

        holder.categoriaTextView.setText("Categoria" + ": " + transaccion.getCategoria());
        // Configuracion del icono
        String letra = transaccion.getCategoria().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(letra);
        // Configuracion del boton editar
        ImageButton editarButton = holder.editarButton;
        editarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(transaccion);
            }
        });
        // Configuracion del boton eliminar
        ImageButton eliminarButton = holder.eliminarButton;
        eliminarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(transaccion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transacciones == null ? 0 : transacciones.size();
    }

    public static class TransaccionconCategoria {
        private Transaccion transaccion;
        private String categoria;

        public TransaccionconCategoria(Transaccion transaccion, String categoria) {
            this.transaccion = transaccion;
            this.categoria = categoria;
        }

        public Transaccion getTransaccion() {
            return transaccion;
        }

        public String getCategoria() {
            return categoria;
        }
    }

    public class TransaccionViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreTextView;
        private final TextView cantidadTextView;
        private final TextView fechaTextView;
        private final TextView categoriaTextView;
        private final TextView cuentaTextView;
        private final TextView iconoTextView;
        private final ImageButton editarButton;
        private final ImageButton eliminarButton;
        public TransaccionViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreTextView = itemView.findViewById(R.id.textViewTransaccionNombre);
            this.cantidadTextView = itemView.findViewById(R.id.textViewTransaccionCantidad);
            this.fechaTextView = itemView.findViewById(R.id.textViewTransaccionFecha);
            this.categoriaTextView = itemView.findViewById(R.id.textViewTransaccionCategoria);
            this.cuentaTextView = itemView.findViewById(R.id.textViewTransaccionCuenta);
            this.iconoTextView = itemView.findViewById(R.id.iconoTransaccion);
            this.editarButton = itemView.findViewById(R.id.editarTransaccion);
            this.eliminarButton = itemView.findViewById(R.id.eliminarTransaccion);
        }
    }
}
