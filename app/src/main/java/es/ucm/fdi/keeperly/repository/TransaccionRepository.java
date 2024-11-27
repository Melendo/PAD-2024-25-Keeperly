package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

public class TransaccionRepository {


    private final TransaccionDAO transaccionDao;
    private final ExecutorService executorService;

    public TransaccionRepository() {
        transaccionDao = KeeperlyDB.getInstance().transaccionDao();
        executorService = Executors.newSingleThreadExecutor();
    }


    public void insert(Transaccion transaccion) {
        executorService.execute(() -> transaccionDao.insert(transaccion));
    }

    public void update(Transaccion transaccion) {
        executorService.execute(() -> transaccionDao.update(transaccion));
    }

    public void delete(Transaccion transaccion) {
        executorService.execute(() -> transaccionDao.delete(transaccion));
    }

    public List<Transaccion> getTransaccionesByCuenta(int id_cuenta) {
        return transaccionDao.getTransaccionesByCuenta(id_cuenta);
    }

    public Transaccion getTransaccionById(int id) {
        return transaccionDao.getTransaccionById(id);
    }

    public Result<Transaccion> createTransaccion(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        Transaccion transaccion = new Transaccion();
        transaccion.setConcepto(concepto);
        transaccion.setCantidad(cantidad);
        transaccion.setIdCuenta(cuenta);
        transaccion.setIdCategoria(categoria);
        transaccion.setFecha(fecha.toString());
        transaccionDao.insert(transaccion);

        return new Result.Success<Boolean>(true);

    }
}
