package com.example.smart;

public class locationdatabase {
    String id;
    String name;
    String blood;
    String ec;
    String vhm;

    public locationdatabase(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBlood() {
        return blood;
    }

    public String getEc() {
        return ec;
    }

    public String getVhm() {
        return vhm;
    }
    public locationdatabase(String id,String name,String blood,String ec,String vhm)
    {
        this.id=id;
        this.name=name;
        this.blood=blood;
        this.ec=ec;
        this.vhm=vhm;
    }
}
