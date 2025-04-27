package com.saintsau.slam2.gnotes30.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class MatiereAssociationId implements Serializable {
    private Integer userId;
    private Integer matId;

    public MatiereAssociationId() {}

    public MatiereAssociationId(Integer userId, Integer matId) {
        this.userId = userId;
        this.matId = matId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMatId() {
        return matId;
    }

    public void setMatId(Integer matId) {
        this.matId = matId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatiereAssociationId that = (MatiereAssociationId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(matId, that.matId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, matId);
    }
}
