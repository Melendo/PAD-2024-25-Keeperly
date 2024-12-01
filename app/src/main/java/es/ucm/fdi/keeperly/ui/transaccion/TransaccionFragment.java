package es.ucm.fdi.keeperly.ui.transaccion;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.databinding.FragmentTransaccionBinding;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;
import es.ucm.fdi.keeperly.ui.cuenta.CuentaSpinnerAdapter;
import es.ucm.fdi.keeperly.ui.cuenta.CuentasViewModel;
import es.ucm.fdi.keeperly.ui.cuenta.CuentasViewModelFactory;

public class TransaccionFragment extends Fragment {

    private TransaccionViewModel transaccionViewModel;
    private CuentasViewModel cuentasViewModel;
    private CategoriasViewModel categoriasViewModel;
    private FragmentTransaccionBinding binding;
    private TransaccionAdapter transaccionAdapter;

    private String categoriaSeleccionada;
    private int cuentaSeleccionadaID;

    private List<Cuenta> cuentasLista = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        transaccionViewModel =  new ViewModelProvider(this, new TransaccionViewModelFactory()).get(TransaccionViewModel.class);

        binding = FragmentTransaccionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fabCreateTransaccion = root.findViewById(R.id.fab_add_transaction);
        fabCreateTransaccion.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_transaccion_to_createTransaccionFragment);
        });

        RecyclerView recyclerView = binding.recyclerViewTransacciones;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (transaccionAdapter == null) { // Prevent re-initialization
            transaccionAdapter = new TransaccionAdapter();
        }
        recyclerView.setAdapter(transaccionAdapter);

        transaccionViewModel.getTransacciones().observe(getViewLifecycleOwner(), transaccionAdapter::setTransacciones);

        //On Click Listeners
        transaccionAdapter.setOnTransaccionClickListener(new TransaccionAdapter.OnTransaccionClickListener() {
            @Override
            public void onEditClick(TransaccionAdapter.TransaccionconCategoria transaccion) {
                mostrarDialogoEditarTransaccion(transaccion.getTransaccion());
            }

            @Override
            public void onDeleteClick(TransaccionAdapter.TransaccionconCategoria transaccion) {

            }

        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void mostrarDialogoEditarTransaccion(Transaccion transaccion) {
        // Crea el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_editar_transaccion, null);
        builder.setView(dialogView);

        // Elementos
        EditText editTextConcepto = dialogView.findViewById(R.id.etConcepto);
        EditText editTextCantidad = dialogView.findViewById(R.id.etCantidad);
        Spinner spinnerCategoria = dialogView.findViewById(R.id.spCategoria);
        Spinner spinnerCuenta = dialogView.findViewById(R.id.spCuenta);
        EditText editTextFecha = dialogView.findViewById(R.id.etFecha);
        Button btnGuardar = dialogView.findViewById(R.id.btnGuardar);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Cargar spinner categorias
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        categoriasViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
           if (categorias != null) {
               // Crea una lista de nombres de categorías
               List<String> nombresCategorias = new ArrayList<>();
               nombresCategorias.add(0, "Selecciona una categoría");
               for (Categoria categoria : categorias) {
                   nombresCategorias.add(categoria.getNombre());
               }

               // Configura el adaptador
               ArrayAdapter<String> adapter = new ArrayAdapter<>(
                       requireContext(),
                       android.R.layout.simple_spinner_item,
                       nombresCategorias
               );

               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               spinnerCategoria.setAdapter(adapter);
           }
        });

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    categoriaSeleccionada = adapterView.getItemAtPosition(i).toString();
                } else {
                    categoriaSeleccionada = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });

        // Cargar spinner cuentas
        cuentasViewModel = new ViewModelProvider(this, new CuentasViewModelFactory())
                .get(CuentasViewModel.class);
        cuentasViewModel.getCuentas().observe(getViewLifecycleOwner(), cuentas -> {
            // Crea una lista de nombre de cuentas
            Cuenta aux = new Cuenta();
            aux.setBalance(0.0);
            aux.setNombre("Selecciona una cuenta");
            aux.setIdUsuario(-1);

            cuentasLista.add(0, aux);
            cuentasLista.addAll(cuentas);

            //Configurar el adaptador
            CuentaSpinnerAdapter adapter = new CuentaSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.setData(cuentasLista);
            spinnerCuenta.setAdapter(adapter);
        });

        spinnerCuenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCuentaName = parent.getItemAtPosition(position).toString();
                Cuenta selectedCuenta = null;
                for (Cuenta cuenta : cuentasLista) {
                    if (cuenta.getNombre().equals(selectedCuentaName)) {
                        selectedCuenta = cuenta;
                        break;
                    }
                }

                if (selectedCuenta != null && selectedCuenta.getId() != -1)
                    cuentaSeleccionadaID = selectedCuenta.getId();
                else
                    cuentaSeleccionadaID = -1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //ignore
            }
        });

        // Crear Date Field
        editTextFecha.setOnClickListener(v -> showDatePickerDialog(editTextFecha));

        editTextConcepto.setText(transaccion.getConcepto());
        editTextCantidad.setText(String.valueOf(transaccion.getCantidad()));

        LocalDate fechaantigua = transaccion.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = fechaantigua.format(formatter);
        editTextFecha.setText(formattedDate);

        spinnerCategoria.setSelection(transaccion.getIdCategoria());
        spinnerCuenta.setSelection(transaccion.getIdCuenta());

        AlertDialog dialog = builder.create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnGuardar.setOnClickListener(v -> {
           if (editTextConcepto.getText().toString().isEmpty()) {
               editTextConcepto.setError("El concepto es obligatorio");
           } else if (editTextCantidad.getText().toString().isEmpty()) {
                editTextCantidad.setError("La cantidad es obligatoria");
           } else if (editTextFecha.getText().toString().isEmpty()) {
                editTextFecha.setError("La fecha es obligatoria");
           } else {
                String concepto = editTextConcepto.getText().toString();
                double cantidad = Double.parseDouble(editTextCantidad.getText().toString());

                int idcategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                int idcuenta = cuentaSeleccionadaID;

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date fecha = dateFormat.parse(editTextFecha.getText().toString());
                    transaccionViewModel.editTransaccion(transaccion.getId(), concepto, cantidad, idcuenta, idcategoria, fecha);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
           }
        });

        transaccionViewModel.getEditTransaccionFormState().observe(getViewLifecycleOwner(), editTransaccionFormState -> {
           if (editTransaccionFormState == null) {
               return;
           }
           btnGuardar.setEnabled(editTransaccionFormState.isDataValid());
           if (editTransaccionFormState.isDataValid()) {
               editTextFecha.setError(null);
           }
           if (editTransaccionFormState.getConceptoError() != null) {
               editTextConcepto.setError(getString(editTransaccionFormState.getConceptoError()));
           }
           if (editTransaccionFormState.getCantidadError() != null) {
               editTextCantidad.setError(getString(editTransaccionFormState.getCantidadError()));
           }
           if (editTransaccionFormState.getFechaError() != null) {
               editTextFecha.setError(getString(editTransaccionFormState.getFechaError()));
           }
        });

        transaccionViewModel.getEditTransaccionResult().observe(getViewLifecycleOwner(), editTransaccionResult -> {
            if (editTransaccionResult == null)
                return;
            if (editTransaccionResult.getError() != null) {
                Toast.makeText(getContext(), getString(editTransaccionResult.getError()), Toast.LENGTH_SHORT).show();
            }
            if (editTransaccionResult.getSuccess() != null) {
                Toast.makeText(getContext(), "Transacción editada correctamente", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                int idCuenta = cuentaSeleccionadaID;
                int idCategoria;
                if (!Objects.equals(categoriaSeleccionada, ""))
                    idCategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                else
                    idCategoria = -1;

                double cantidad;
                if (editTextCantidad.getText().toString().isEmpty() || editTextCantidad.getText().toString().equals("-"))
                    cantidad = -1;
                else
                    cantidad = Double.parseDouble(editTextCantidad.getText().toString());

                Date fecha;
                if (editTextFecha.getText().toString().isEmpty() || editTextFecha.getText().toString().equals("0"))
                    fecha = null;
                else {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        fecha = dateFormat.parse(editTextFecha.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    transaccionViewModel.editTransaccionDataChanged(editTextConcepto.getText().toString(),
                            cantidad,
                            idCuenta,
                            idCategoria,
                            fecha);
                }
            }
        };

        editTextConcepto.addTextChangedListener(afterTextChangedListener);
        editTextCantidad.addTextChangedListener(afterTextChangedListener);
        editTextFecha.addTextChangedListener(afterTextChangedListener);

        dialog.show();
    }

    private void showDatePickerDialog(EditText editText) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> {
                    // Formatear la fecha seleccionada
                    String fechaSeleccionada = String.format("%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                    editText.setText(fechaSeleccionada);
                },
                year, month, day
        );


        // Mostrar el diálogo
        datePickerDialog.show();
    }
}