package org.pharosnet.vertx.pg.dal.gen.table;

import org.pharosnet.vertx.pg.dal.core.annotations.Column;
import org.pharosnet.vertx.pg.dal.core.annotations.ColumnKind;
import org.pharosnet.vertx.pg.dal.core.annotations.Table;
import org.pharosnet.vertx.pg.dal.core.annotations.TableKind;
import org.pharosnet.vertx.pg.dal.gen.SourceGenerator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

public class TableGenerator implements SourceGenerator {

    public TableGenerator() {
        this.ids = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    private String pkg;
    private String name;

    private TypeMirror typeMirror;

    private TableKind tableKind;
    private String schemaName;
    private String tableName;

    private List<RowField> ids;
    private RowField createBy;
    private RowField createBY;
    private RowField createAT;
    private RowField modifyBy;
    private RowField modifyAt;
    private RowField deleteBy;
    private RowField deleteAt;
    private RowField version;
    private List<RowField> columns;

    @Override
    public TableGenerator load(Elements elementUtils, TypeElement typeElement) {

        this.pkg = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.name = typeElement.getSimpleName().toString();

        this.typeMirror = typeElement.asType();

        Table tableAnnotation = typeElement.getAnnotation(Table.class);
        this.tableKind = tableAnnotation.kind();
        this.tableName = tableAnnotation.name().strip();
        this.schemaName = tableAnnotation.schema().strip();

        if (!typeElement.getSuperclass().toString().equals("java.lang.Object")) {
            TypeElement superRow = elementUtils.getTypeElement(typeElement.getSuperclass().toString());
            this.loadColumns(superRow.getEnclosedElements());
        }
        this.loadColumns(typeElement.getEnclosedElements());
        return this;
    }

    private void loadColumns(List<? extends Element> enclosedElements) {
        for (Element enclosedElement : enclosedElements) {
            if (enclosedElement instanceof VariableElement) {
                VariableElement variableElement = (VariableElement) enclosedElement;
                Column column = variableElement.getAnnotation(Column.class);
                if (column == null) {
                    continue;
                }
                RowField rowField = new RowField();
                rowField.setColumn(column.name());
                rowField.setName(variableElement.getSimpleName().toString());
                rowField.setType(variableElement.asType());
                if (column.kind().equals(ColumnKind.ID)) {
                    this.ids.add(rowField);
                } else if (column.kind().equals(ColumnKind.NORMAL)) {
                    this.columns.add(rowField);
                } else if (column.kind().equals(ColumnKind.CREATE_BY)) {
                    this.createBY = rowField;
                } else if (column.kind().equals(ColumnKind.CREATE_AT)) {
                    this.createAT = rowField;
                } else if (column.kind().equals(ColumnKind.MODIFY_BY)) {
                    this.modifyBy = rowField;
                } else if (column.kind().equals(ColumnKind.MODIFY_AT)) {
                    this.modifyAt = rowField;
                } else if (column.kind().equals(ColumnKind.DELETE_BY)) {
                    this.deleteBy = rowField;
                } else if (column.kind().equals(ColumnKind.DELETE_AT)) {
                    this.deleteAt = rowField;
                } else if (column.kind().equals(ColumnKind.VERSION)) {
                    this.version = rowField;
                }
            }
        }
    }

    @Override
    public TableGenerator generate(Filer filer) throws Exception {
        this.generateRowConvert(filer);
        if (this.tableKind.equals(TableKind.TABLE)) {
            this.generateInsertExecBuilder(filer);
            this.generateInsertBatchExecBuilder(filer);
            this.generateUpdateExecBuilder(filer);
            this.generateUpdateBatchExecBuilder(filer);
            this.generateDeleteExecBuilder(filer);
            this.generateDeleteBatchExecBuilder(filer);
            this.generateDeleteForceExecBuilder(filer);
            this.generateDeleteForceBatchExecBuilder(filer);
        }
        return this;
    }

    private void generateRowConvert(Filer filer) throws Exception {
        GenerateRowConvert.generate(filer, this);
    }

    private void generateInsertExecBuilder(Filer filer) throws Exception {
        GenerateInsert.generate(filer, this);
    }

    private void generateUpdateExecBuilder(Filer filer) throws Exception {
        GenerateUpdate.generate(filer, this);
    }

    private void generateDeleteExecBuilder(Filer filer) throws Exception {
        GenerateDelete.generate(filer, this);
    }

    private void generateDeleteForceExecBuilder(Filer filer) throws Exception {
        GenerateDeleteForce.generate(filer, this);
    }

    private void generateInsertBatchExecBuilder(Filer filer) throws Exception {
        GenerateInsert.generateBatch(filer, this);
    }

    private void generateUpdateBatchExecBuilder(Filer filer) throws Exception {
        GenerateUpdate.generateBatch(filer, this);
    }

    private void generateDeleteBatchExecBuilder(Filer filer) throws Exception {
        GenerateDelete.generateBatch(filer, this);
    }

    private void generateDeleteForceBatchExecBuilder(Filer filer) throws Exception {
        GenerateDeleteForce.generateBatch(filer, this);
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TableKind getTableKind() {
        return tableKind;
    }

    public void setTableKind(TableKind tableKind) {
        this.tableKind = tableKind;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<RowField> getIds() {
        return ids;
    }

    public void setIds(List<RowField> ids) {
        this.ids = ids;
    }

    public RowField getCreateBy() {
        return createBy;
    }

    public void setCreateBy(RowField createBy) {
        this.createBy = createBy;
    }

    public RowField getCreateBY() {
        return createBY;
    }

    public void setCreateBY(RowField createBY) {
        this.createBY = createBY;
    }

    public RowField getCreateAT() {
        return createAT;
    }

    public void setCreateAT(RowField createAT) {
        this.createAT = createAT;
    }

    public RowField getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(RowField modifyBy) {
        this.modifyBy = modifyBy;
    }

    public RowField getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(RowField modifyAt) {
        this.modifyAt = modifyAt;
    }

    public RowField getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(RowField deleteBy) {
        this.deleteBy = deleteBy;
    }

    public RowField getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(RowField deleteAt) {
        this.deleteAt = deleteAt;
    }

    public RowField getVersion() {
        return version;
    }

    public void setVersion(RowField version) {
        this.version = version;
    }

    public List<RowField> getColumns() {
        return columns;
    }

    public void setColumns(List<RowField> columns) {
        this.columns = columns;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }
}
