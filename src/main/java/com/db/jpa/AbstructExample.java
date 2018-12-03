//package com.db.jpa;
//
//import com.zhibi.taoke.annotation.CustomColumn;
//import com.zhibi.taoke.annotation.CustomTable;
//import com.zhibi.taoke.annotation.LeftJoin;
//import com.zhibi.taoke.annotation.LeftJoins;
//import org.apache.commons.lang.StringUtils;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import javax.persistence.Table;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class AbstructExample {
//
//    private StringBuilder select = new StringBuilder("SELECT ");
//    private StringBuilder selectColumn = new StringBuilder("");
//    private StringBuilder table = new StringBuilder(" FROM ");
//    private StringBuilder where = new StringBuilder(" WHERE 1=1 ");
//
//    public AbstructExample(Object object) {
//        List<String> tableNames = new ArrayList<>();
//        Class<?> clazz = object.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        CustomTable t1 = clazz.getAnnotation(CustomTable.class);
//        String tableOne = t1.value();
//        tableNames.add(t1.value());
//        StringBuilder tableNameBuilder = new StringBuilder(tableOne);
//        //拼接要查询的表
//        table.append(t1.value());
//        if(clazz.isAnnotationPresent(LeftJoins.class)){
//            LeftJoins leftJoins = clazz.getAnnotation(LeftJoins.class);
//            for (LeftJoin leftJoin : leftJoins.value()) {
//                tableNames.add(leftJoin.name());
//                table.append(" LEFT JOIN ").append(leftJoin.name()).append(" ON ").
//                        append(leftJoin.onOne()).append(" = ").append(leftJoin.onTwo());
//            }
//        }
//        //遍历
//        for (int i = 0; i < fields.length; i++) {
//            try {
//                //得到属性
//                Field field = fields[i];
//                //打开私有访问
//                field.setAccessible(true);
//                //获取属性
//                String name = field.getName();
//                if (field.isAnnotationPresent(CustomColumn.class)) {
//                    CustomColumn column = field.getAnnotation(CustomColumn.class);
//                    if(i != 1){
//                        selectColumn.append(", ");
//                    }
//                    if(StringUtils.isNotBlank(column.table())){
//                        selectColumn.append(column.table());
//                    }else {
//                        selectColumn.append(tableOne);
//                    }
//                    selectColumn.append(".").append(column.value());
//                    Object value = field.get(object);
//                    if(null != value){
//                        where.append(column.value()).append(" = ").append(value);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(select.append(selectColumn).append(table).append(where));
//    }
//
//    public void appendParam(Object object, String connector, String key, String value){
//        Class<?> clazz = object.getClass();
//        Table table = clazz.getAnnotation(Table.class);
//        where.append(" AND ").append(table.name()).append(".").append(key).append(connector).append(value);
//    }
//
//    public Query createQuery(EntityManager entityManager){
//        String sqlStr = select.append(selectColumn).append(table).append(where).toString();
//        //映射到对象里
//        Query query = entityManager.createNativeQuery(sqlStr, OrderBonus.class);
//        return query;
//    }
//
//    public static void main(String[] args) throws ClassNotFoundException {
//        OrderBonus orderBonus = new OrderBonus();
//        orderBonus.setId(1L);
//        AbstructExample abstructExample = new AbstructExample(orderBonus);
//        abstructExample.appendParam(orderBonus,"=","id","1");
//    }
//}
//
//
