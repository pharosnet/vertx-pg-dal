package org.pharosnet.vertx.pg.dal.core;

import org.pharosnet.vertx.pg.dal.core.annotations.Column;
import org.pharosnet.vertx.pg.dal.core.annotations.ColumnKind;

import java.time.OffsetDateTime;

public class AbstractRow {

    @Column(name = "ID", kind = ColumnKind.ID)
    protected String id;
    @Column(name = "CREATE_BY", kind = ColumnKind.CREATE_BY)
    protected String createBY;
    @Column(name = "CREATE_AT", kind = ColumnKind.CREATE_AT)
    protected OffsetDateTime createAT;
    @Column(name = "MODIFY_BY", kind = ColumnKind.MODIFY_BY)
    protected String modifyBY;
    @Column(name = "MODIFY_AT", kind = ColumnKind.MODIFY_AT)
    protected OffsetDateTime modifyAT;
    @Column(name = "DELETE_BY", kind = ColumnKind.DELETE_BY)
    protected String deleteBY;
    @Column(name = "DELETE_AT", kind = ColumnKind.DELETE_AT)
    protected OffsetDateTime deleteAT;
    @Column(name = "VERSION", kind = ColumnKind.VERSION)
    protected Long version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBY() {
        return createBY;
    }

    public void setCreateBY(String createBY) {
        this.createBY = createBY;
    }

    public OffsetDateTime getCreateAT() {
        return createAT;
    }

    public void setCreateAT(OffsetDateTime createAT) {
        this.createAT = createAT;
    }

    public String getModifyBY() {
        return modifyBY;
    }

    public void setModifyBY(String modifyBY) {
        this.modifyBY = modifyBY;
    }

    public OffsetDateTime getModifyAT() {
        return modifyAT;
    }

    public void setModifyAT(OffsetDateTime modifyAT) {
        this.modifyAT = modifyAT;
    }

    public String getDeleteBY() {
        return deleteBY;
    }

    public void setDeleteBY(String deleteBY) {
        this.deleteBY = deleteBY;
    }

    public OffsetDateTime getDeleteAT() {
        return deleteAT;
    }

    public void setDeleteAT(OffsetDateTime deleteAT) {
        this.deleteAT = deleteAT;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
