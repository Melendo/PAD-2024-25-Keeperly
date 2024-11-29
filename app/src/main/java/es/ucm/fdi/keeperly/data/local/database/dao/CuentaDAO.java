package es.ucm.fdi.keeperly.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;

@Dao
public interface CuentaDAO {
    @Insert
    void insert(Cuenta cuenta);

    @Update
    void update(Cuenta cuenta);

    @Delete
    void delete(Cuenta cuenta);

    @Query("SELECT * FROM cuentas WHERE idUsuario = :userId")
    LiveData<List<Cuenta>> getCuentasByUsuario(int userId);

    @Query("SELECT * FROM cuentas WHERE idUsuario = :id")
    Cuenta getCuentaById(int id);
}
