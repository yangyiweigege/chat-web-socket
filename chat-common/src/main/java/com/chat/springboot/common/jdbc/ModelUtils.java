package com.chat.springboot.common.jdbc;
import com.chat.springboot.common.annotation.ClassPro;
import com.chat.springboot.common.annotation.FieldPro;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    private static ResultSet rs = null;

    private static PreparedStatement pst = null;

    private static List<String> methodList;

    private static List<Field> fieldList;

    private static List<String> dbFieldList;

    private static void getPropList(Class cls, String methodType) {
        methodList = new ArrayList<>();
        fieldList = new ArrayList<>();
        dbFieldList = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldPro.class)) {
                FieldPro fieldPro = field.getAnnotation(FieldPro.class);
                dbFieldList.add(fieldPro.JDBCName());
                String fieldName = field.getName();
                fieldList.add(field);
                methodList.add(methodType
                        + fieldName.replaceFirst(fieldName.substring(0, 1),
                        fieldName.substring(0, 1).toUpperCase()));

            }
        }

    }


    public static <T> T getOne(Class<T> cls, Integer id) {
        T model = null;
        try {
            getPropList(cls, "set");
            StringBuilder sql = new StringBuilder("select ");
            int index = 0;
            for (String dbField : dbFieldList) {
                sql.append(dbField);
                sql.append(" as");
                sql.append(" ");
                sql.append(fieldList.get(index).getName());
                if (dbField.length() > index) {
                    sql.append(",");
                }
                index++;
            }
            ClassPro classPro = cls.getAnnotation(ClassPro.class);
            sql.append(" from ");
            sql.append(classPro.tableName());
            sql.append(" where id = ");
            sql.append(id);
            System.out.println(sql.toString());
            pst = JDBCConnectionUtils.getPreparedStatement(sql.toString());
            rs = pst.executeQuery();
            List<T> modelList = buildModel(cls);
            if (!modelList.isEmpty()) {
                model = modelList.get(0);
            }
            JDBCConnectionUtils.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectionUtils.closeConnection();
        }
        return model;
    }

    public static <E> List<E> getAll(Class<E> cls) {
        List<E> modelList = null;
        try {
            getPropList(cls, "set");
            StringBuilder sql = new StringBuilder("select ");
            int index = 0;
            for (String dbField : dbFieldList) {
                sql.append(dbField);
                sql.append(" as ");
                sql.append(fieldList.get(index).getName());
                if (dbField.length() > index) {
                    sql.append(",");
                }
                index++;
            }
            ClassPro classPro = cls.getAnnotation(ClassPro.class);
            sql.append(" from ");
            sql.append(classPro.tableName());
            System.out.println(sql.toString());
            pst = JDBCConnectionUtils.getPreparedStatement(sql.toString());
            rs = pst.executeQuery();
            modelList = buildModel(cls);
            JDBCConnectionUtils.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectionUtils.closeConnection();
        }
        return modelList;
    }

    public static <T> Boolean update(T model) {
        Class cls = model.getClass();
        getPropList(cls, "get");
        Integer id = null;
        Boolean isSuccess = false;
        ClassPro classPro = (ClassPro) cls.getAnnotation(ClassPro.class);
        StringBuilder sql = new StringBuilder("update ");
        sql.append(classPro.tableName());
        sql.append(" set ");
        int index = 0;
        try {
            for (Field field : fieldList) {
                Object value;
                Method method = cls.getMethod(methodList.get(index));
                value = method.invoke(model);
                if ("id".equals(field.getName())) {
                    id = (Integer) value;
                    index++;
                    continue;
                }
                sql.append(dbFieldList.get(index));
                sql.append(" = ");
                sql.append(value != null ? ("\"" + value.toString() + "\"") : null);
                if (fieldList.size() > (index + 1)) {
                    sql.append(",");
                }
                index++;
            }
            sql.append(" where id = ");
            sql.append(id);
            System.out.println(sql.toString());
            pst = JDBCConnectionUtils.getPreparedStatement(sql.toString());
            isSuccess = pst.execute();
            JDBCConnectionUtils.closeConnection();
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectionUtils.closeConnection();
        }
        return isSuccess;
    }

    public static <T> Boolean insert(T model) {
        Boolean isSuccess = false;
        Class cls = model.getClass();
        getPropList(cls, "get");
        ClassPro classPro = (ClassPro) cls.getAnnotation(ClassPro.class);
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(classPro.tableName());
        sql.append("()values()");
        try {
            for (int i = (fieldList.size() - 1); i >= 0; i--) {
                Object value;
                Method method = cls.getMethod(methodList.get(i));
                value = method.invoke(model);
                String dbFieldName = dbFieldList.get(i);
                if ("id".equalsIgnoreCase(dbFieldName) || value == null) {
                    continue;
                }
                value = "\"" + value.toString() + "\"";
                sql.insert(sql.toString().indexOf("(") + 1
                        , i > 1 ? "," + dbFieldName : dbFieldName);
                sql.insert(sql.toString().lastIndexOf("(") + 1
                        , i > 1 ? "," + value : value);
            }
            System.out.println(sql.toString());
            pst = JDBCConnectionUtils.getPreparedStatement(sql.toString());
            isSuccess = pst.execute();
            JDBCConnectionUtils.closeConnection();
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCConnectionUtils.closeConnection();
        }
        return isSuccess;
    }

    public static Boolean delete(Class<?> cls, Integer id) {
        Boolean isSuccess = false;
        ClassPro classPro = cls.getAnnotation(ClassPro.class);
        StringBuilder sql = new StringBuilder("delete from ");
        sql.append(classPro.tableName());
        sql.append(" where id = ");
        sql.append(id);
        try {
            pst = JDBCConnectionUtils.getPreparedStatement(sql.toString());
            isSuccess = pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private static <T> List<T> buildModel(Class<T> cls) {
        List<T> modelList = new ArrayList();
        try {
            while (rs.next()) {
                T model = cls.newInstance();
                int index = 0;
                for (Field field : fieldList) {
                    String classType = field.getType().toString();
                    Method method = cls.getMethod(methodList.get(index), field.getType());
                    if ("class java.lang.Integer".equals(classType)) {
                        method.invoke(model, rs.getInt(field.getName()));
                    } else if ("class java.lang.String".equals(classType)) {
                        method.invoke(model, rs.getString(field.getName()));
                    } else if ("class java.lang.Long".equals(classType)) {
                        method.invoke(model, rs.getLong(field.getName()));
                    } else if ("class java.util.Date".equals(classType)) {
                        method.invoke(model, rs.getDate(field.getName()));
                    } else if ("class java.lang.Short".equals(classType)) {
                        method.invoke(model, rs.getShort(field.getName()));
                    } else if ("class java.lang.Double".equals(classType)) {
                        method.invoke(model, rs.getDouble(field.getName()));
                    } else if ("class java.lang.Float".equals(classType)) {
                        method.invoke(model, rs.getFloat(field.getName()));
                    } else if ("class java.lang.Byte".equals(classType)) {
                        method.invoke(model, rs.getByte(field.getName()));
                    } else if ("class java.lang.Boolean".equals(classType)) {
                        method.invoke(model, rs.getBoolean(field.getName()));
                    }
                    index++;
                }
                modelList.add(model);
            }
        } catch (NoSuchMethodException
                | IllegalAccessException
                | SQLException
                | InvocationTargetException
                | InstantiationException e) {
            e.printStackTrace();
        }
        return modelList;
    }

}
