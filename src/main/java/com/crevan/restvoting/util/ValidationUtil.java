package com.crevan.restvoting.util;

import com.crevan.restvoting.model.BaseEntity;

public class ValidationUtil {

    public static void checkNew(final BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }
}
