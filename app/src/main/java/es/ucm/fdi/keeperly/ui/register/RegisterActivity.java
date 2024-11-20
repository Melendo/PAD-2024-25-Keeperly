package es.ucm.fdi.keeperly.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.StringRes;

import es.ucm.fdi.keeperly.R;
import es.ucm.fdi.keeperly.databinding.ActivityRegisterBinding;
import es.ucm.fdi.keeperly.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText nameEditText = binding.nameEditText;
        final EditText emailEditText = binding.emailEditText;
        final EditText passwordEditText = binding.passwordEditText;
        final EditText repeatpwEditText = binding.repeatPasswordEditText;

        final Button registerButton = binding.registerButton;
        final Button loginButton = binding.loginButton;

        final ProgressBar loadingProgressBar = binding.loading;
        final ImageView logoImage = binding.appLogo;

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logoImage.startAnimation((fadeIn));
        nameEditText.startAnimation(slideUp);
        emailEditText.startAnimation(slideUp);
        passwordEditText.startAnimation(slideUp);
        repeatpwEditText.startAnimation(slideUp);

        registerButton.startAnimation(slideUp);
        loginButton.startAnimation(slideUp);

        registerViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null)
                return;
            registerButton.setEnabled(registerFormState.isDataValid());
            if (registerFormState.getNombreError() != null) {
                nameEditText.setError(getString(registerFormState.getNombreError()));
            }
            if (registerFormState.getEmailError() != null) {
                emailEditText.setError(getString(registerFormState.getEmailError()));
            }
            if (registerFormState.getPwError() != null) {
                passwordEditText.setError(getString(registerFormState.getPwError()));
            }
            if (registerFormState.getRepeatpwError() != null) {
                repeatpwEditText.setError(getString(registerFormState.getRepeatpwError()));
            }
        });

        registerViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (registerResult.getError() != null) {
                showRegisterFailed(registerResult.getError());
            }
            if (registerResult.getSuccess() != null) {
                updateUiWithRegister(registerResult.getSuccess());
                setResult(Activity.RESULT_OK);

                // Redirigir al login
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                //Complete and destroy register activity once successful
                finish();
            }
            showRegisterFailed(registerResult.getError());

        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.registerDataChanged(nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        repeatpwEditText.getText().toString());
            }
        };

        nameEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        repeatpwEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            registerViewModel.register(emailEditText.getText().toString(),
                    nameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void updateUiWithRegister(RegisteredUserView model) {
        String welcome = "Registro completo, bienvenido " + model.getNombre();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }

}