package cn.cerc.jdb.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class ClassData {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    private Class<?> clazz;
    private String tableId = null;
    private String select = null;
    private Map<String, Field> fields = null;
    private Field generationIdentityField = null;
    private String uid = "UID_";

    public ClassData(Class<?> clazz) {
        this.clazz = clazz;
        for (Annotation anno : clazz.getAnnotations()) {
            if (anno instanceof Entity) {
                Entity entity = (Entity) anno;
                if (!"".equals(entity.name()))
                    tableId = entity.name();
            }
            if (anno instanceof Select) {
                Select obj = (Select) anno;
                if (!"".equals(obj.value())) {
                    select = obj.value();
                }
            }
        }

        if (tableId == null)
            throw new RuntimeException("entity.name or select not define");

        this.fields = loadFields();

        if (select == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("select ");
            int i = 0;
            for (String key : fields.keySet()) {
                if (i > 0)
                    sb.append(",");
                sb.append(key);
                i++;
            }
            sb.append(" from ").append(tableId);
            select = sb.toString();
        }
        
        //查找自增字段并赋值
        int count = 0;
        for(String key : fields.keySet()) {
            Field field = fields.get(key);
            for (Annotation item : field.getAnnotations()) {
                if (item instanceof GeneratedValue) {
                    if (((GeneratedValue) item).strategy() == GenerationType.IDENTITY) {
                        generationIdentityField = field;
                        count++;
                    }
                }
                if (item instanceof UID) {
                    uid = field.getName();
                }
            }
        }
        
        if(count > 1)
            throw new RuntimeException("support one generationIdentityField!");
    }

    private Map<String, Field> loadFields() {
        Map<String, Field> fields = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            Column column = null;
            for (Annotation item : field.getAnnotations()) {
                if (item instanceof Column) {
                    column = (Column) item;
                    break;
                }
            }
            if (column != null) {
                String fieldCode = field.getName();
                if (!"".equals(column.name()))
                    fieldCode = column.name();

                if (field.getModifiers() == PUBLIC) {
                    fields.put(fieldCode, field);
                } else if (field.getModifiers() == PRIVATE || field.getModifiers() == PROTECTED) {
                    field.setAccessible(true);
                    fields.put(fieldCode, field);
                }
            }
        }
        return fields;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getTableId() {
        return tableId;
    }

    public String getSelect() {
        return select;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public Field getGenerationIdentityField() {
        return generationIdentityField;
    }

    public String getUid() {
        return uid;
    }
}
