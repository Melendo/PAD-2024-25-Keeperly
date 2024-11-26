package es.ucm.fdi.keeperly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.keeperly.databinding.ActivityMenuBinding;
import es.ucm.fdi.keeperly.ui.login.LoginActivity;
import es.ucm.fdi.keeperly.ui.login.LoginViewModel;
import es.ucm.fdi.keeperly.ui.login.LoginViewModelFactory;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Configurar destinos de nivel superior
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cuentas, R.id.nav_presupuestos, R.id.nav_categorias)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configurar clics en los ítems del NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) { // Si se selecciona Logout
                showLogoutConfirmationDialog(); // Mostrar diálogo de confirmación
                return true; // Consumir el evento
            }

            // Para otros ítems, delega la navegación al NavController
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (handled) {
                drawer.closeDrawer(GravityCompat.START); // Cierra el menú lateral
            }

            return handled;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Salir", (dialog, which) -> onLogout()) // Confirmar logout
                .setNegativeButton("Cancelar", null) // Cerrar el diálogo sin hacer nada
                .show();
    }


    public void onLogout() {
        // Limpiar datos del usuario (ejemplo con SharedPreferences)
        LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        loginViewModel.logout();
        String logout_successfully = getString(R.string.logout_successfully);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), logout_successfully, Toast.LENGTH_LONG).show();
        // Redirigir al login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}