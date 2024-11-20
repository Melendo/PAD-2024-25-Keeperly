package es.ucm.fdi.keeperly.ui.register;

import androidx.annotation.Nullable;

public class RegisteredUserView {
    private Integer userId;
    private String nombre;

    RegisteredUserView(Integer userId, String nombre) { this.userId = userId; this.nombre = nombre;}

    Integer getUserId() { return userId; }

    String getNombre() { return nombre; }
}
