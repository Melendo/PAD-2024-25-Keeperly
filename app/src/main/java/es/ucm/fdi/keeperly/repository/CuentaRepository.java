package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CuentaDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentaRepository {
    private final MutableLiveData<Integer> operStatus = new MutableLiveData<>();

    private final CuentaDAO cuentaDao;
    private final ExecutorService executorService;

    public CuentaRepository() {
        cuentaDao = KeeperlyDB.getInstance().cuentaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Cuenta cuenta) {
        if (cuenta.getNombre().trim().isEmpty() || cuenta.getNombre().trim().length() > 30) {
            operStatus.postValue(-2);
        }
        else if (cuenta.getBalance() <= 0.0) {
            operStatus.postValue(-3);
        }
        else {
            try {
                executorService.execute(() -> {
                    try {
                        cuentaDao.insert(cuenta);
                        operStatus.postValue(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        operStatus.postValue(-1);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Cuenta cuenta) {
        executorService.execute(() -> cuentaDao.update(cuenta));
    }

    public void delete(Cuenta cuenta) {
        executorService.execute(() -> cuentaDao.delete(cuenta));
    }

    public List<Cuenta> getAllCuentas(int id_usuario) {
        return cuentaDao.getCuentasByUsuario(id_usuario);
    }

    public Cuenta getCuentaById(int id) {
        return cuentaDao.getCuentaById(id);
    }

    public Cuenta creaCuenta(String nombre, double balance, int usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setBalance(balance);
        cuenta.setIdUsuario(usuario);
        return cuenta;
    }

    public LiveData<Integer> getOperationStatus() {
        return operStatus;
    }
}
