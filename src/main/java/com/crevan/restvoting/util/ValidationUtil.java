package com.crevan.restvoting.util;

import com.crevan.restvoting.model.BaseEntity;

public class ValidationUtil {

    public static void checkNew(final BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(final BaseEntity entity, final int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must has id=" + id);
        }
    }
}
