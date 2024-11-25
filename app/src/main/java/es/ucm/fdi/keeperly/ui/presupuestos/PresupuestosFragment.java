package es.ucm.fdi.keeperly.ui.presupuestos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.databinding.FragmentPresupuestosBinding;


public class PresupuestosFragment extends Fragment {

    private FragmentPresupuestosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PresupuestosViewModel budgetsViewModelViewModel =
                new ViewModelProvider(this).get(PresupuestosViewModel.class);

        binding = FragmentPresupuestosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textBudgets;
        budgetsViewModelViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}