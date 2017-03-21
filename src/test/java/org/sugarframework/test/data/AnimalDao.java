package org.sugarframework.test.data;

import java.util.Collection;
import java.util.List;

import org.sugarframework.data.Bind;
import org.sugarframework.data.Datasource;
import org.sugarframework.data.Query;
import org.sugarframework.data.Update;
import org.sugarframework.test.vo.Animal;

@Datasource(id="data1", url="jdbc:h2:mem:test", user="username", password="password")
public interface AnimalDao {

	@Update("create table animal (id int primary key, weight int, name varchar(100), family varchar(100), birthdate date, longNumber long, hasAntlers boolean )")
	void create();
	
	@Update("update animal set id={a.id}, weight={a.weight}, name={a.name}, family={a.family}, birthdate={a.birthdate}, longNumber={a.longNumber}, hasAntlers={a.hasAntlers} ) where id={a.id}")
    int update(@Bind("a") Animal a);
	
	@Update("insert into animal ( id, weight, name, family, birthdate, longNumber, hasAntlers ) " +
			   "values ({a.id}, {a.weight}, {a.name}, {a.family}, {a.birthdate}, {a.longNumber}, {a.hasAntlers})")
	void add(@Bind("a") Animal a);	
	
	@Query("select name from animal")
	List<String> names();
	
	@Update("delete from animal where id={a.id}")
	void delete(@Bind("a") Animal a);
	
	@Query("select * from animal")
	Collection<Animal> all();
}
