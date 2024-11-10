package es.ucm.fdi.keeperly.integracion;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.ucm.fdi.keeperly.integracion.daos.*;
import es.ucm.fdi.keeperly.integracion.entities.*;

@Database(
        entities = {Usuario.class, Cuenta.class, Presupuesto.class, Categoria.class, Transaccion.class},
        version = 1
)
public abstract class KeeperlyDB extends RoomDatabase {
    public abstract UsuarioDAO usuarioDao();

    public abstract CuentaDAO cuentaDao();

    public abstract PresupuestoDAO presupuestoDao();

    public abstract CategoriaDAO categoriaDao();

    public abstract TransaccionDAO transaccionDao();

    // Instancia única de la base de datos
    private static volatile KeeperlyDB INSTANCE;

    // Método para obtener la instancia de la base de datos
    public static KeeperlyDB getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (KeeperlyDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    KeeperlyDB.class, "keeperly_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
