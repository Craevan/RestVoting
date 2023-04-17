package com.crevan.restvoting.web;

import com.crevan.restvoting.AuthUser;
import com.crevan.restvoting.model.Role;
import com.crevan.restvoting.model.User;
import com.crevan.restvoting.repository.UserRepository;
import com.crevan.restvoting.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "api/account")
public class AccountController {

    private final UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("Get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("Delete {}", authUser);
        userRepository.deleteById(authUser.id());
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("Register {}", user);
        ValidationUtil.checkNew(user);
        user.setRoles(Set.of(Role.USER));
        user = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/account")
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(user);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal final AuthUser authUser) {
        log.info("Update {} to {}", authUser, user);
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(user, oldUser.id());
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        userRepository.save(user);
    }
}
