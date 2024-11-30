package es.ucm.fdi.keeperly.ui.cuenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentasAdapter extends RecyclerView.Adapter<CuentasAdapter.CuentaViewHolder> {
    private List<Cuenta> cuentas = new ArrayList<>();
    private OnCuentaClickListener listener;

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

    public interface OnCuentaClickListener {
        void onEditClick(Cuenta cuenta);
        void onDeleteClick(Cuenta cuenta);
    }

    public void setOnCuentaClickListener(OnCuentaClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_cuenta, parent, false);
        return new CuentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentasAdapter.CuentaViewHolder holder, int position) {
        //Nombre
        Cuenta cuenta = cuentas.get(position);
        holder.nombreTextView.setText(cuenta.getNombre());
        //Icono
        String letra = cuenta.getNombre().substring(0, 1).toUpperCase(); //Convierte la inicial a mayuscula
        holder.iconoTextView.setText(letra);
        //Boton editar

        //Boton eliminar
    }

    @Override
    public int getItemCount() {
        return cuentas.size();
    }

    public class CuentaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreTextView;
        private final TextView iconoTextView;
        private final ImageButton editarButton;
        private final ImageButton eliminarButton;
        public CuentaViewHolder(View view) {
            super(view);
            this.nombreTextView = view.findViewById(R.id.nombreCuenta);
            this.iconoTextView = view.findViewById(R.id.iconoCuenta);
            this.editarButton = view.findViewById(R.id.editarCuenta);
            this.eliminarButton = view.findViewById(R.id.eliminarCuenta);
        }
    }
}