package es.ucm.fdi.keeperly.data.local.database.dao;

import androidx.room.*;

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

    @Query("SELECT * FROM transacciones WHERE idCuenta = :cuentaId")
    List<Transaccion> getTransaccionesByCuenta(int cuentaId);

    @Query("SELECT * FROM transacciones WHERE id = :id")
    Transaccion getTransaccionById(int id);

    @Query("SELECT * FROM transacciones WHERE idCategoria = :id")
    List<Categoria> getTransaccionesPorCategoria(int id);

    @Query("DELETE FROM transacciones WHERE idCuenta = :idCuenta")
    void deleteTransaccionesByCuenta(int idCuenta);
}
