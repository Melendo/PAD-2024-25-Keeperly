package es.ucm.fdi.keeperly.ui.cuenta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public interface OnCuentaClickListener {
        void onCuentaClick(Cuenta cuenta);
    }

    public void setOnCuentaClickListener(OnCuentaClickListener listener) {
        this.listener = listener;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CuentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_card_cuenta, parent, false);
        return new CuentaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentaViewHolder holder, int position) {
        Cuenta cuenta = cuentas.get(position);
        holder.iconoTextView.setText(String.valueOf(cuenta.getNombre().charAt(0)));
        holder.textViewNombre.setText(cuenta.getNombre());
        holder.textViewBalance.setText(String.format("%.2f€", cuenta.getBalance()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCuentaClick(cuenta);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuentas.size();
    }

    static class CuentaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewBalance, iconoTextView;


        public CuentaViewHolder(@NonNull View itemView) {
            super(itemView);
            iconoTextView = itemView.findViewById(R.id.iconoCuenta);
            textViewNombre = itemView.findViewById(R.id.nombreCuenta);
            textViewBalance = itemView.findViewById(R.id.balanceCuenta);
        }
    }
}
