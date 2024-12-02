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
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

public class CuentaRepository {
    private final MutableLiveData<Integer> insertStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> deleteStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> updateStatus = new MutableLiveData<>();

    private final CuentaDAO cuentaDao;
    private final TransaccionDAO transaccionDAO;
    private final ExecutorService executorService;

    public CuentaRepository() {
        cuentaDao = KeeperlyDB.getInstance().cuentaDao();
        transaccionDAO = KeeperlyDB.getInstance().transaccionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Cuenta cuenta) {
        if (cuenta.getNombre().trim().isEmpty() || cuenta.getNombre().trim().length() > 30) {
            insertStatus.postValue(-2);
        }
        else if (cuenta.getBalance() <= 0.0) {
            insertStatus.postValue(-3);
        }
        else {
            try {
                executorService.execute(() -> {
                    try {
                        cuentaDao.insert(cuenta);
                        insertStatus.postValue(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        insertStatus.postValue(-1);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Cuenta cuenta) {
        if (cuenta.getNombre().trim().isEmpty() || cuenta.getNombre().trim().length() > 30) {
            updateStatus.postValue(-2);
        }
        else if (cuenta.getBalance() <= 0.0) {
            updateStatus.postValue(-3);
        }
        else {
            try {
                executorService.execute(() -> {
                    Cuenta cuentaExistente = cuentaDao.getCuentaById(cuenta.getId());
                    if (cuentaExistente != null) {
                        cuentaDao.update(cuenta);
                        updateStatus.postValue(1);
                    } else {
                        updateStatus.postValue(-4);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                updateStatus.postValue(-1);
            }
        }
    }

    public void delete(Cuenta cuenta) {
        executorService.execute(() -> {
            int cont_cuentas = cuentaDao.getCountCuentasByUsuario(cuenta.getIdUsuario());
            if (cont_cuentas == 1) {
                deleteStatus.postValue(-1);
            }
            else {
                //Elimina todas las transacciones vinculadas a la cuenta
                KeeperlyDB.getInstance().transaccionDao().deleteTransaccionesByCuenta(cuenta.getId());
                //Elimina la cuenta
                cuentaDao.delete(cuenta);
                deleteStatus.postValue(1);
            }
        });
    }

    public LiveData<List<Cuenta>> getAllCuentas(int id_usuario) {
        return cuentaDao.getCuentasByUsuario(id_usuario);
    }

    public Cuenta getCuentaById(int id) {
        return cuentaDao.getCuentaById(id);
    }

    public List<Transaccion> getAllTransaccionesByCuenta(int cuentaId) {
        return transaccionDAO.getTransaccionesByCuenta(cuentaId);
    }

    public double gastoTotal(Cuenta cuenta) {
        double total = 0.0;
        TransaccionDAO transaccionDAO = KeeperlyDB.getInstance().transaccionDao();
        List<Transaccion> transacciones = transaccionDAO.getTransaccionesByCuenta(cuenta.getId());
        for (Transaccion transaccion : transacciones) {
            total += transaccion.getCantidad();
        }
        Cuenta cuenta_aux = cuentaDao.getCuentaById(cuenta.getId());
        if(cuenta_aux.getGastado() != total){
            cuenta_aux.setGastado(total);
            cuentaDao.update(cuenta_aux);
        }
        return total * (-1);
    }

    public Cuenta creaCuenta(String nombre, double balance, int usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setBalance(balance);
        cuenta.setIdUsuario(usuario);
        return cuenta;
    }

    public LiveData<Integer> getOperationStatus() {
        return insertStatus;
    }

    public LiveData<Integer> getDeleteStatus() {
        return deleteStatus;
    }

    public void resetDeleteStatus() {
        deleteStatus.postValue(null);
    }

    public LiveData<Integer> getUpdateStatus() {
        return updateStatus;
    }

    public void resetUpdateStatus() {
        updateStatus.postValue(null);
    }

}
