package es.ucm.fdi.keeperly.ui.register;

import androidx.annotation.Nullable;

public class RegisteredUserView {
    private Integer userId;

    RegisteredUserView(Integer userId) { this.userId = userId; }

    Integer getUserId() { return userId; }
}
