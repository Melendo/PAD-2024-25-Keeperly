package es.ucm.fdi.keeperly.ui.cuenta;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentaSpinnerAdapter extends ArrayAdapter<Cuenta> {
    public CuentaSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getNombre());
        return view;
    }

    public void setData(List<Cuenta> cuentas) {
        addAll(cuentas);
        cuentas.clear();
        notifyDataSetChanged();
    }
}
