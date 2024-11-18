package es.ucm.fdi.keeperly.data.local.database.entities;

import androidx.room.*;

@Entity(tableName = "categorias")
public class Categoria {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
