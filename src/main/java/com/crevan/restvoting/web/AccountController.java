package com.crevan.restvoting.web;

import com.crevan.restvoting.AuthUser;
import com.crevan.restvoting.model.Role;
import com.crevan.restvoting.model.User;
import com.crevan.restvoting.repository.UserRepository;
import com.crevan.restvoting.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/account")
public class AccountController implements RepresentationModelProcessor<RepositoryLinksResource> {

    @SuppressWarnings("unchecked")
    private static final RepresentationModelAssemblerSupport<User, EntityModel<User>> ASSEMBLER =
            new RepresentationModelAssemblerSupport<>(AccountController.class, (Class<EntityModel<User>>) (Class<?>) EntityModel.class) {
                @Override
                public EntityModel<User> toModel(final User entity) {
                    return EntityModel.of(entity, linkTo(AccountController.class).withSelfRel());
                }
            };

    private final UserRepository userRepository;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<User> get(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("Get {}", authUser);
        return ASSEMBLER.toModel(authUser.getUser());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("Delete {}", authUser);
        userRepository.deleteById(authUser.id());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    public void update(@Valid @RequestBody final User user, @AuthenticationPrincipal final AuthUser authUser) {
        log.info("Update {} to {}", authUser, user);
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(user, oldUser.id());
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public RepositoryLinksResource process(final RepositoryLinksResource model) {
        model.add(linkTo(AccountController.class).withRel("account"));
        return model;
    }
}
