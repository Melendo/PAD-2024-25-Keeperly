package es.ucm.fdi.keeperly.data.local.database.entities;

import androidx.room.*;

@Entity(tableName = "cuentas", foreignKeys = @ForeignKey(entity = Usuario.class, parentColumns = "id", childColumns = "idUsuario", onDelete = ForeignKey.CASCADE))
public class Cuenta {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idUsuario;
    private double balance;
    private String nombre;
    private double gastado;

// Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toString() {return nombre;}

    public double getGastado() {return gastado;}

    public void setGastado(double gastado) {
        this.gastado = gastado;
    }
}
