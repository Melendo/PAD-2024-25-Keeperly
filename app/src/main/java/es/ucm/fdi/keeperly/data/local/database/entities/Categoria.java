package es.ucm.fdi.keeperly.data.local.database.entities;

import androidx.room.*;

import java.util.Collection;

@Entity(tableName = "categorias")
public class Categoria {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int id_usuario;
    private String nombre;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
