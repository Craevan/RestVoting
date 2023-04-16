package com.crevan.restvoting;

import com.crevan.restvoting.model.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@ToString(of = "user")
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthUser(@NonNull final User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }
}
