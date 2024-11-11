package es.ucm.fdi.keeperly.service;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CuentaDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

public class CuentaSA {
    private final CuentaDAO cuentaDao;
    private final ExecutorService executorService;

    public CuentaSA(Context context) {
        KeeperlyDB db = KeeperlyDB.getInstance(context);
        cuentaDao = db.cuentaDao();
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
