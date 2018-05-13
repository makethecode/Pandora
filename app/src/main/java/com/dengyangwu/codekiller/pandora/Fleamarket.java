package com.dengyangwu.codekiller.pandora;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CodeKiller on 2017/5/28.
 */
public class Fleamarket implements Serializable {
    public String type;
    public String demand;
    public String price;
    public ArrayList<String> photo=new ArrayList<String>();
    public int id;
    public int state;
    public String userid;


    /*public Fleamarket(String type, String demand, String price) {
        this.type=type;
        this.demand=demand;
        this.price=price;
      //  this.photo=photo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPhoto(ArrayList<String> photo){ this.photo=photo;}
    public String getType() {
        return type;
    }

    public String  getPrice() {
        return price;
    }

    public String getDemand() {
        return demand;
    }

    public  ArrayList<String> getPhoto(){ return photo;}
*/
}
