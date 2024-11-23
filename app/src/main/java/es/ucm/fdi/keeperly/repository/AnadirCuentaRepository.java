package es.ucm.fdi.keeperly.repository;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;

public class AnadirCuentaRepository {
    private static volatile AnadirCuentaRepository instance;
    private CuentaRepository cuentaRepository;
    private Cuenta cuenta = null;

    private AnadirCuentaRepository(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public static AnadirCuentaRepository getInstance(CuentaRepository cuentaRepository) {
        if (instance == null) {
            instance = new AnadirCuentaRepository(cuentaRepository);
        }
        return instance;
    }

    private void setAddedAccount(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Result<Cuenta> anadirCuenta(String nombre, double balance, int idUsuario) {
        try {
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.setIdUsuario(idUsuario);
            nuevaCuenta.setNombre(nombre.trim());
            nuevaCuenta.setBalance(balance);
            cuentaRepository.insert(nuevaCuenta);
            return new Result.Success<Cuenta>(nuevaCuenta);
        } catch (Exception e) {
            return new Result.Error(new Exception("Error adding a new account", e));
        }
    }
}
