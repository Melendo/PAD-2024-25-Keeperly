package es.ucm.fdi.keeperly.ui.transaccion;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.icu.text.DateFormat;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.databinding.FragmentCreateTransaccionBinding;
import es.ucm.fdi.keeperly.ui.categorias.CategoriasViewModel;

public class CreateTransaccionFragment extends Fragment {

    private TransaccionViewModel transaccionViewModel;
    private CategoriasViewModel categoriasViewModel;
    private FragmentCreateTransaccionBinding binding;

    private String categoriaSeleccionada;

    public CreateTransaccionFragment() {
        // Required empty public constructor
    }

    public static CreateTransaccionFragment newInstance() { return new CreateTransaccionFragment(); }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_transaccion, container, false);

        // Inicializar el Binding
        binding = FragmentCreateTransaccionBinding.inflate(getLayoutInflater());

        // Inicializar el ViewModel
        transaccionViewModel = new ViewModelProvider(this, new TransaccionViewModelFactory()).get(TransaccionViewModel.class);

        // Obtener referencias a los elementos de la interfaz de usuario
        final EditText conceptoEditText = binding.conceptoEditText;
        final EditText cantidadEditText = binding.cantidadEditText;
        final Spinner cuentaSpinner = binding.cuentaSpinner;
        final Spinner categoriaSpinner = binding.categoriaSpinner;
        final EditText fechaField = binding.fechaField;
        final Button crearButton = binding.saveTransactionButton;
        final Button cancelarButton = binding.cancelTransactionButton;

        // Configurar el ViewModel de Categorías
        categoriasViewModel = new ViewModelProvider(this).get(CategoriasViewModel.class);
        categoriasViewModel.getCategorias().observe(getViewLifecycleOwner(), categorias -> {
            // Configurar el adaptador para el Spinner de Categorías
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
                categoriaSpinner.setAdapter(adapter);
            }
        });

        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // Configurar el ViewModel de Cuentas

        // Fecha
        fechaField.setOnClickListener(v -> showDatePickerDialog(fechaField));

        // Boton de crear transaccion
        crearButton.setOnClickListener(v -> {
            if (conceptoEditText.getText().toString().isEmpty()) {
                conceptoEditText.setError("El concepto es obligatorio");
            } else if (cantidadEditText.getText().toString().isEmpty()) {
                cantidadEditText.setError("La cantidad es obligatoria");
            } else if (fechaField.getText().toString().isEmpty()) {
                fechaField.setError("La fecha es obligatoria");
            } else {
                // Obtener los datos del formulario
                String concepto = conceptoEditText.getText().toString();
                double cantidad = Double.parseDouble(cantidadEditText.getText().toString());
                String cuenta = cuentaSpinner.getSelectedItem().toString();
                String categoria = categoriaSpinner.getSelectedItem().toString();
                String fecha = fechaField.getText().toString();

                int idcategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();
                int idcuenta = 1;

                try {
                    transaccionViewModel.createTransaccion(concepto, cantidad, idcuenta, idcategoria, DateFormat.getDateInstance().parse(fecha));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        cancelarButton.setOnClickListener(v -> {
            limpiarCampos();
            Toast.makeText(requireContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

        // Observar el estado del formulario
        transaccionViewModel.getCreateTransaccionFormState().observe(this, createTransaccionFormState -> {
           if (createTransaccionFormState == null)
               return;
           crearButton.setEnabled(createTransaccionFormState.isDataValid());
           if (createTransaccionFormState.getConceptoError() != null) {
               conceptoEditText.setError(getString(createTransaccionFormState.getConceptoError()));
           }
           if (createTransaccionFormState.getCantidadError() != null) {
               cantidadEditText.setError(getString(createTransaccionFormState.getCantidadError()));
           }
           if (createTransaccionFormState.getFechaError() != null) {
               fechaField.setError(getString(createTransaccionFormState.getFechaError()));
           }
        });

        transaccionViewModel.getCreateTransaccionResult().observe(this, createTransaccionResult -> {
            if (createTransaccionResult == null) {
                return;
            }
            if (createTransaccionResult.getError() != null) {
                Toast.makeText(requireContext(), "Error al crear la transaccion", Toast.LENGTH_SHORT).show();
            }
            if (createTransaccionResult.getSuccess() != null) {
                Toast.makeText(requireContext(), "Transaccion creada", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                getParentFragmentManager().popBackStack();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int idCuenta = 1; //TODO
                int idCategoria = categoriasViewModel.getCategoriaByNombre(categoriaSeleccionada).getId();;
                try {
                    transaccionViewModel.createTransaccionDataChanged(conceptoEditText.getText().toString(),
                            Double.parseDouble(cantidadEditText.getText().toString()),
                            idCuenta,
                            idCategoria,
                            DateFormat.getDateInstance().parse(fechaField.getText().toString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        conceptoEditText.addTextChangedListener(afterTextChangedListener);
        cantidadEditText.addTextChangedListener(afterTextChangedListener);
        fechaField.addTextChangedListener(afterTextChangedListener);

        return view;
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
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    editText.setText(fechaSeleccionada);
                },
                year, month, day
        );


        // Mostrar el diálogo
        datePickerDialog.show();
    }

    private void limpiarCampos() {
        binding.conceptoEditText.setText("");
        binding.cantidadEditText.setText("");
        binding.cuentaSpinner.setSelection(0);
        binding.categoriaSpinner.setSelection(0);
        binding.fechaField.setText("");
    }
}