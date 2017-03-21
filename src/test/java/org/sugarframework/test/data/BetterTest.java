package org.sugarframework.test.data;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.sugarframework.data.Dao;
import org.sugarframework.data.DefaultDatasourceRegistry;

public final class BetterTest {

    @Dao("data1")
    private BetterDao dao;

    public void test() {
        dao.create();

        Something jay = new Something(0, "Jay");
        dao.add(jay);

        dao.add(new Something(1, "Maryse"));
        dao.add(new Something(2, "Meli"));
        dao.add(new Something(3, "Maddy"));
        dao.add(new Something(4, "Jojo"));
        dao.insert(23432, "Maryse", new Date());
        dao.insert(2344, "Maryse", new Date());

        jay.setName("Jay Jay");
        jay.setBirthday(new Date());
        dao.update(jay);

        dao.delete(jay);
        for (Something s : dao.getAll()) {
            System.out.println(s);
        }

        Something something = dao.find(1, "Maryse");
        System.out.println(something);
        
        List<Map<String, ?>> list = dao.findNamesAndIds();
        for(Map<String, ?> column : list){
            System.out.println(column.get("name"));
            System.out.println(column.get("id"));

        }
        
        Collection<String> strings = dao.findAllNames();
        for(String s : strings){
        	System.out.println(s);
        }
        
        List<Integer> allIds = dao.findIds();
        for(Integer i : allIds){
        	System.out.println(i);
        }
        
    }

    public static void main(String[] args) throws Exception {
    	
        BetterTest page = new BetterTest();

        new DefaultDatasourceRegistry().initialize(page);

        page.test();

    }

}
