package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.ucm.fdi.keeperly.R;

public class TransaccionFragment extends Fragment {

    private TransaccionViewModel mViewModel;

    public static TransaccionFragment newInstance() {
        return new TransaccionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaccion, container, false);

        FloatingActionButton fabCreateTransaccion = view.findViewById(R.id.fab_add_transaction);
        fabCreateTransaccion.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.nav_host_fragment_content_menu);
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransaccionViewModel.class);
        // TODO: Use the ViewModel
    }

}