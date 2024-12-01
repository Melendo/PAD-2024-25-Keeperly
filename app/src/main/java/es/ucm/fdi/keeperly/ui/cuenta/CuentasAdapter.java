package es.ucm.fdi.keeperly.ui.cuenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentasAdapter extends RecyclerView.Adapter<CuentasAdapter.CuentaViewHolder> {
    private List<Cuenta> cuentas = new ArrayList<>();
    private OnCuentaClickListener listener;
    CuentasViewModel cuentasViewModel;

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
        notifyDataSetChanged();
    }

    public interface OnCuentaClickListener {
        void onEditClick(Cuenta cuenta);
        void onDeleteClick(Cuenta cuenta);
        //void onCuentaClick(Cuenta cuenta);
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
        holder.editarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(cuenta);
            }
        });
        //Boton eliminar
        holder.eliminarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(cuenta);
            }
        });
        //Calculo gastado
        /*double gastado = cuentasViewModel.getGastoTotal(cuenta);
        //Cuenta detallada
        holder.itemView.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("cuentaId", cuenta.getId());
            args.putString("nombreC", cuenta.getNombre());
            args.putString("balanceC", String.valueOf(cuenta.getBalance()));
            args.putString("gastadoC", String.valueOf(gastado));
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.cuentaDetalladaFragment, args);
        });*/
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