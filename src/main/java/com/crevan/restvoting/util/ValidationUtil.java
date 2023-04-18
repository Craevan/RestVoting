package com.crevan.restvoting.util;

import com.crevan.restvoting.error.IllegalRequestDataException;
import com.crevan.restvoting.model.BaseEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(final BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(final BaseEntity entity, final int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}
