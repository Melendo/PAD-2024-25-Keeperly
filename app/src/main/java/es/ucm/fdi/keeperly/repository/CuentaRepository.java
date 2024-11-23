package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CuentaDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentaRepository {


    private final CuentaDAO cuentaDao;
    private final ExecutorService executorService;

    public CuentaRepository() {
        cuentaDao = KeeperlyDB.getInstance().cuentaDao();
        executorService = Executors.newSingleThreadExecutor();
    }


    public void insert(Cuenta cuenta) {
        executorService.execute(() -> cuentaDao.insert(cuenta));
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
}
