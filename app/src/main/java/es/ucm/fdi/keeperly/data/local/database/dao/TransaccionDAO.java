package es.ucm.fdi.keeperly.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.Date;
import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;

@Dao
public interface TransaccionDAO {
    @Insert
    void insert(Transaccion transaccion);

    @Update
    void update(Transaccion transaccion);

    @Delete
    void delete(Transaccion transaccion);

    @Query("DELETE FROM transacciones WHERE idCuenta = :idCuenta")
    void deleteTransaccionesByCuenta(int idCuenta);

    @Query("SELECT * FROM transacciones")
    List<Transaccion> getAllTransacciones();

    @Query("SELECT * FROM transacciones WHERE idCuenta = :cuentaId")
    LiveData<List<Transaccion>> getTransaccionesByCuenta(int cuentaId);

    @Query("SELECT * FROM transacciones WHERE idCuenta IN (:accountIds)")
    LiveData<List<Transaccion>> getTransaccionesByCuentas(List<Integer> accountIds);

    @Query("SELECT * FROM transacciones WHERE id = :id")
    Transaccion getTransaccionById(int id);

    @Query("SELECT * FROM transacciones WHERE idCategoria = :id")
    List<Transaccion> getTransaccionesPorCategoria(int id);


    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin AND idCategoria = :categoria")
    LiveData<List<Transaccion>> obtenerTransaccionesEntreFechas(Date fechaInicio, Date fechaFin, int categoria);

    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin AND idCategoria = :categoria")
    List<Transaccion> obtenerTransaccionesEntreFechasDirect(Date fechaInicio, Date fechaFin, int categoria);

    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin AND idCuenta IN (:cuentas)")
    List<Transaccion> obtenerTodasTransaccionesEntreFechas(Date fechaInicio, Date fechaFin, List<Integer> cuentas);

    @Query("SELECT cuentas.nombre FROM transacciones JOIN cuentas ON transacciones.idCuenta = cuentas.id WHERE transacciones.id = :id")
    String getNombreCuenta(int id);
}
