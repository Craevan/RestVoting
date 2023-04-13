package com.crevan.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public abstract class BaseEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }
}
