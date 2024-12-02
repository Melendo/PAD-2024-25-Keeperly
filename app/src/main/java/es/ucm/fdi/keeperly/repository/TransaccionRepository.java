package es.ucm.fdi.keeperly.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import es.ucm.fdi.keeperly.data.Result;
import es.ucm.fdi.keeperly.data.local.database.KeeperlyDB;
import es.ucm.fdi.keeperly.data.local.database.dao.CategoriaDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.CuentaDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.ui.transaccion.TransaccionAdapter;

public class TransaccionRepository {


    private final TransaccionDAO transaccionDao;
    private final CuentaDAO cuentaDao;
    private final CategoriaDAO categoriaDAO;
    private final ExecutorService executorService;

    public List<TransaccionAdapter.TransaccionconCategoria> transacciones;

    public TransaccionRepository() {
        transaccionDao = KeeperlyDB.getInstance().transaccionDao();
        cuentaDao = KeeperlyDB.getInstance().cuentaDao();
        categoriaDAO = KeeperlyDB.getInstance().categoriaDao();
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

    public LiveData<List<Transaccion>> getTransaccionesByCuenta(int id_cuenta) {
        return transaccionDao.getTransaccionesByCuenta(id_cuenta);
    }

    public Transaccion getTransaccionById(int id) {
        return transaccionDao.getTransaccionById(id);
    }

    public List<Transaccion> getAllTransacciones() {
        return transaccionDao.getAllTransacciones();
    }

    public LiveData<List<TransaccionAdapter.TransaccionconCategoria>> getAllTransaccionesLoggedIn() {
        int idUsuario = LoginRepository.getInstance(RepositoryFactory.getInstance().getUsuarioRepository()).getLoggedUser().getId();
        LiveData<List<Cuenta>> cuentaslivedata = cuentaDao.getCuentasByUsuario(idUsuario);

        return Transformations.switchMap(cuentaslivedata, cuentas -> {
            if (cuentas == null || cuentas.isEmpty()) {
                return new MutableLiveData<>(Collections.emptyList());
            }

            List<Integer> cuentasIds = cuentas.stream()
                    .map(Cuenta::getId)
                    .collect(Collectors.toList());

            LiveData<List<Transaccion>> transaccionesLiveData = transaccionDao.getTransaccionesByCuentas(cuentasIds);

            return Transformations.map(transaccionesLiveData, transacciones -> {
                if (transacciones == null || transacciones.isEmpty()) {
                    return Collections.emptyList();
                }

                List<TransaccionAdapter.TransaccionconCategoria> result = new ArrayList<>();

                for (Transaccion t : transacciones) {
                    String categoria = categoriaDAO.getCategoriadeTransaccion(t.getId());
                    result.add(new TransaccionAdapter.TransaccionconCategoria(t, categoria));
                }
                return result;
            });
        });
    }

    public Result<Boolean> createTransaccion(String concepto, double cantidad, int cuenta, int categoria, Date fecha) {
        Transaccion transaccion = new Transaccion();
        transaccion.setConcepto(concepto);
        transaccion.setCantidad(cantidad);
        transaccion.setIdCuenta(cuenta);
        transaccion.setIdCategoria(categoria);
        transaccion.setFecha(fecha);

        Cuenta cuentaactualizar = cuentaDao.getCuentaById(transaccion.getIdCuenta());
        double old_bal = cuentaactualizar.getBalance();
        double new_bal = old_bal + transaccion.getCantidad();
        cuentaactualizar.setBalance(new_bal);
        cuentaDao.update(cuentaactualizar);

        transaccionDao.insert(transaccion);

        return new Result.Success<Boolean>(true);

    }

    public Result<Boolean> updateTransaccion(Transaccion transaccion) {
        transaccionDao.update(transaccion);
        return new Result.Success<Boolean>(true);
    }

    public double getTransaccionesMesActual(int idUsuario) {
        double total = 0.0;
        LocalDate now = LocalDate.now();
        // Día 1 del mes actual
        LocalDate inicioMes = now.withDayOfMonth(1);

        // Día 1 del mes siguiente
        LocalDate inicioMesSiguiente = inicioMes.plusMonths(1);

        // Convertir a `Date` para la consulta
        Date fechaInicio = Date.from(inicioMes.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fechaFin = Date.from(inicioMesSiguiente.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Integer> id_cuentas_ususario = cuentaDao.getIdCuentasByUsuario(idUsuario);

        List<Transaccion> transacciones = transaccionDao.obtenerTodasTransaccionesEntreFechas(fechaInicio, fechaFin, id_cuentas_ususario);
        for (Transaccion t : transacciones) {
            total += t.getCantidad();
        }
        return total;
    }
}
