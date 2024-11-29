package es.ucm.fdi.keeperly.ui.categorias;

import android.annotation.SuppressLint;
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
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder> {
    private List<Categoria> categorias = new ArrayList<>();
    private OnCategoriaClickListener listener;

    public interface OnCategoriaClickListener {
        void onEditClick(Categoria categoria);
        void onDeleteClick(Categoria categoria);
    }

    public void setOnCategoriaClickListener(OnCategoriaClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_card_categoria, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.nombreTextView.setText(categoria.getNombre());

        // Icono circular con la primera letra
        String inicial = categoria.getNombre().substring(0, 1).toUpperCase();
        holder.iconoTextView.setText(inicial);

        // Botones editar y eliminar
        holder.editarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(categoria); // Llama al listener configurado en el fragmento
            }
        });

        holder.eliminarButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(categoria);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        notifyDataSetChanged();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreTextView;
        private final TextView iconoTextView;
        private final ImageButton editarButton;
        private final ImageButton eliminarButton;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewCategoriaNombre);
            iconoTextView = itemView.findViewById(R.id.iconoCategoria);
            editarButton = itemView.findViewById(R.id.btnEditarCategoria);
            eliminarButton = itemView.findViewById(R.id.btnEliminarCategoria);
        }
    }
}


