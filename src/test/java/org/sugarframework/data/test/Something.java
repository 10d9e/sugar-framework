package org.sugarframework.data.test;

import java.util.Date;

public class Something {
    
    private int id;
    
    private String name;
    
    private Date birthday = new Date();
    
    public Something(){
        
    }
    
    public Something(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Something [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
    }

    
}
