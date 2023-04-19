package com.crevan.restvoting.web;

import com.crevan.restvoting.UserTestUtil;
import com.crevan.restvoting.model.User;
import com.crevan.restvoting.repository.UserRepository;
import com.crevan.restvoting.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.crevan.restvoting.UrlTestUtils.REGISTER_URL;
import static com.crevan.restvoting.UrlTestUtils.ACCOUNT_URL;
import static com.crevan.restvoting.UserTestUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AccountControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(ACCOUNT_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonMatcher(user, UserTestUtil::assertNoIdEquals));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(ACCOUNT_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(ACCOUNT_URL))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(userRepository.findById(USER_ID).isPresent());
        Assertions.assertTrue(userRepository.findById(ADMIN_ID).isPresent());
    }

    @Test
    void register() throws Exception {
        User user = UserTestUtil.getNew();
        User registered = UserTestUtil.asUser(
                perform(MockMvcRequestBuilders.post(ACCOUNT_URL + REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(user)))
                        .andExpect(status().isCreated()).andReturn());
        int id = registered.id();
        user.setId(id);
        UserTestUtil.assertEquals(registered, user);
        UserTestUtil.assertEquals(registered, userRepository.findById(id).orElseThrow());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        User updated = UserTestUtil.getUpdated();
        perform(MockMvcRequestBuilders.put(ACCOUNT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        UserTestUtil.assertEquals(updated, userRepository.findById(USER_ID).orElseThrow());
    }
}
