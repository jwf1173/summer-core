package cn.cerc.jdb.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@Select("select * from s_userInfo")
public class StubUser {

    @Column(name="ID_")
    @UpdateKey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Id
    @Column(name = "code_")
    private String code;
    
    @Column(name = "name_")
    private String name;

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

    public static void main(String[] args) {
        ClassFactory.printDebugInfo(StubUser.class);
    }
}
