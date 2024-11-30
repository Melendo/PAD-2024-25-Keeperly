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
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentasInicioAdapter extends RecyclerView.Adapter<CuentasInicioAdapter.CuentaInicioViewHolder>{
    private List<Cuenta> cuentas = new ArrayList<>();

    @NonNull
    @Override
    public CuentaInicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_card_cuenta, parent, false);
        return new CuentaInicioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentasInicioAdapter.CuentaInicioViewHolder holder, int position) {
        Cuenta cuenta = cuentas.get(position);

        holder.nombreTextView.setText(cuenta.getNombre());
        holder.saldoTextView.setText(String.valueOf(String.format("%.2f€", cuenta.getBalance())));

        // Icono circular con la primera letra
        String initial = cuenta.getNombre().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(initial);

    }

    @Override
    public int getItemCount() {
        return cuentas.size();
    }

    static class CuentaInicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView nombreTextView;
        private final TextView saldoTextView;
        private final TextView iconoTextView;

        public CuentaInicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewCuentaMainNombre);
            saldoTextView = itemView.findViewById(R.id.textViewSaldoCuentaMain);
            iconoTextView = itemView.findViewById(R.id.iconoPresupuestoInicioMain);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

}
