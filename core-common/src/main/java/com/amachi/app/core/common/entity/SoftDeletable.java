package com.amachi.app.core.common.entity;

/**
 * Interface for soft deletion implementation.
 */
public interface SoftDeletable {
    Boolean getDeleted();
    void setDeleted(Boolean deleted);
    void delete();
}
