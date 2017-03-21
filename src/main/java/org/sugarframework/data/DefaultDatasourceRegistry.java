package org.sugarframework.data;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.reflections.Reflections;
import org.sugarframework.context.DefaultContextInitializer;
import org.sugarframework.data.BindingProcessor.Tuple;
import org.sugarframework.data.util.TypeCheck;

public class DefaultDatasourceRegistry implements DatasourceRegistry {

    private Log log = LogFactory.getLog(getClass());

    private Map<String, DataSource> datasources = new HashMap<>();

    @Override
    public void initialize(Object page) {

    	Reflections r = DefaultContextInitializer.getContext().reflections();
    	
        Set<Class<?>> types = r.getTypesAnnotatedWith(Datasource.class);

        for (Class<?> type : types) {

            Datasource ds = type.getAnnotation(Datasource.class);

            log.info("Setting up data source.");
            DataSource dataSource = setupDataSource(ds.url(), ds.user(), ds.password());
            log.info("Done.");

            log.info("Adding datasource to pool : " + ds.id());

            datasources.put(ds.id(), dataSource);
        }

        Set<Field> fields = getAllFields(page.getClass(), withAnnotation(Dao.class));

        for (Field field : fields) {

            final Dao dao = field.getAnnotation(Dao.class);

            Object newDao = Proxy.newProxyInstance(DefaultDatasourceRegistry.class.getClassLoader(),
                    new Class<?>[] { field.getType() }, new InvocationHandler() {

                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                            if (method.isAnnotationPresent(Update.class)) {
                                Update update = method.getAnnotation(Update.class);
                                                                
                                if (args == null) {
                                    update(datasources.get(dao.value()), update.value());
                                }
                                else {
                                    update(datasources.get(dao.value()), method, update.value(), args);
                                }
                            }
                            else if (method.isAnnotationPresent(Query.class)) {
                                Query query = method.getAnnotation(Query.class);
                                String sql = query.value();

                                Type type = method.getReturnType();

                                if (Collection.class.isAssignableFrom((Class<?>) type)) {
                                    
                                    if (method.getGenericReturnType() instanceof ParameterizedType){

                                        Type[] types = ((ParameterizedType) method.getGenericReturnType())
                                                .getActualTypeArguments();
                                        
                                        Type target = types[0];
                                        if(target instanceof ParameterizedType){
                                            target = ((ParameterizedType) target).getRawType();
                                        }
    
                                        return queryList(datasources.get(dao.value()), method, sql,
                                                (Class<?>) target, args);
                                    } else {
                                        return queryList(datasources.get(dao.value()), method, sql,
                                                (Class<?>) type, args);
                                    }
                                }
                                else {
                                    return query(datasources.get(dao.value()), method, sql, (Class<?>) type,
                                            args);
                                }

                            }
                            return null;
                        }
                    });

            field.setAccessible(true);

            try {
                field.set(page, newDao);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }

        }
    }

    private Tuple<List<?>, String> format(Method method, String sql, Object... args) throws Exception {
        Map<String, Object> bindings = new HashMap<>();

        int i = 0;

        Annotation[][] annotations = method.getParameterAnnotations();

        for (Annotation[] ann : annotations) {

            if (ann.length > 0) {
                for (Annotation a : ann) {
                    if (a.annotationType().equals(Bind.class)) {

                        Bind bind = (Bind) a;
                        bindings.put(bind.value(), args[i]);
                    }
                }
            }
            i++;
        }

        return BindingProcessor.format(sql, bindings);

    }

    private PreparedStatement prepare(Connection conn, Method method, String sql, Object... args) throws Exception {

        Tuple<List<?>, String> tuple = format(method, sql, args);

        PreparedStatement psmt = conn.prepareStatement(tuple.sql);

        int c = 1;
        for (Object value : tuple.values) {
            if (value instanceof java.util.Date) {
                java.util.Date d = (java.util.Date) value;
                value = new java.sql.Date(d.getTime());
            }
            psmt.setObject(c, value);
            c++;
        }

        return psmt;
    }

    private void update(DataSource dataSource, Method method, String sql, Object... args) throws Exception {

        Connection conn = null;

        try {
            conn = dataSource.getConnection();

            PreparedStatement psmt = prepare(conn, method, sql, args);

            psmt.executeUpdate();

        }
        catch (SQLException ex) {
            DbUtils.printStackTrace(ex);
            throw ex;
        }
        finally {
            DbUtils.commitAndCloseQuietly(conn);
        }
    }

    private Object query(DataSource dataSource, Method method, String sql, Class<?> type, Object... args)
            throws Exception {

        QueryRunner run = new QueryRunner(dataSource);

        Tuple<List<?>, String> tuple = format(method, sql, args);

        ResultSetHandler<?> h = new BeanHandler(type);

        Object[] arr = tuple.values.toArray(new Object[tuple.values.size()]);

        return run.query(tuple.sql, h, arr);

    }

    private List<?> queryList(DataSource dataSource, Method method, String sql, Class<?> type, Object... args)
            throws Exception {

        QueryRunner run = new QueryRunner(dataSource);

        Tuple<List<?>, String> tuple = format(method, sql, args);

        ResultSetHandler<?> h = getListHandler(type);

        Object[] arr = tuple.values.toArray(new Object[tuple.values.size()]);
        
        List<?> o = castMembers( (List<?>) run.query(tuple.sql, h, arr), type );

        return o;

    }
    
    private List<?> castMembers(List<?> target, Class<?> castTo){
    	
    	List list = new ArrayList();
    	for(Object o : target){
    		list.add(ConvertUtils.convert(o, castTo));
    	}
    	return list;
    }

    private ResultSetHandler<?> getListHandler(Class<?> type) {

        if (TypeCheck.isBean(type)) {
            return new BeanListHandler(type);
        }
        else if(type.equals(Map.class)){
        	return new MapListHandler();
        }
        else {
            return new ArrayListHandler();
        }

    }

    private void update(DataSource dataSource, String sql) throws SQLException {

        QueryRunner run = new QueryRunner(dataSource);

        run.update(sql);
    }

    private DataSource setupDataSource(String connectURI, String username, String password) {

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, username, password);

        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

        poolableConnectionFactory.setPool(connectionPool);

        PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);

        return dataSource;
    }

}
