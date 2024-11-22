package es.ucm.fdi.keeperly.ui.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.keeperly.databinding.FragmentAccountsBinding;


public class AccountsFragment extends Fragment {

    private FragmentAccountsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountsViewModel accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);

        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccounts;
        accountsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}