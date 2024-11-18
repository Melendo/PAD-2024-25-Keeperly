package es.ucm.fdi.keeperly.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.ucm.fdi.keeperly.data.local.database.dao.CategoriaDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.CuentaDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.PresupuestoDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.TransaccionDAO;
import es.ucm.fdi.keeperly.data.local.database.dao.UsuarioDAO;
import es.ucm.fdi.keeperly.data.local.database.entities.Categoria;
import es.ucm.fdi.keeperly.data.local.database.entities.Cuenta;
import es.ucm.fdi.keeperly.data.local.database.entities.Presupuesto;
import es.ucm.fdi.keeperly.data.local.database.entities.Transaccion;
import es.ucm.fdi.keeperly.data.local.database.entities.Usuario;

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
    public static KeeperlyDB createInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (KeeperlyDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    KeeperlyDB.class, "keeperly_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static KeeperlyDB getInstance() {
        return INSTANCE;
    }
}
