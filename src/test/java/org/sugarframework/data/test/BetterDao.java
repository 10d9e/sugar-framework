package org.sugarframework.data.test;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.sugarframework.data.Bind;
import org.sugarframework.data.Datasource;
import org.sugarframework.data.Query;
import org.sugarframework.data.Update;

@Datasource(id="data1", url="jdbc:h2:mem:test", user="username", password="password")
public interface BetterDao {

    @Update("create table something(id int, name varchar(100), birthday date)")
    void create();
    
    @Update("insert into something(id, name, birthday) values ({id}, {name}, {birthday})")
    void insert(@Bind("id") int id, @Bind("name") String name, @Bind("birthday") Date birthday);

    @Update("insert into something(id, name) values ({s.id}, {s.name})")
    void add(@Bind("s") Something s);

    @Update("update something set id={s.id}, name={s.name}, birthday={s.birthday} where id={s.id}")
    void update(@Bind("s") Something s);

    @Update("delete from something where id={s.id}")
    void delete(@Bind("s") Something s);

    @Query("select * from something")
    List<Something> getAll();
    
    @Query("select name from something")
    Collection<String> findAllNames();

    @Query("select * from something where id = {id} and name = {name}")
    Something find(@Bind("id") int id, @Bind("name") String name);
    
    @Query("select name from something where name = {n}")
    List<Map<String, String>> findNames(@Bind("n") String name);
    
    @Query("select name, id from something")
    List<Map<String, ?>> findNamesAndIds();
    
    @Query("select id from something")
    List<Integer> findIds();
}
