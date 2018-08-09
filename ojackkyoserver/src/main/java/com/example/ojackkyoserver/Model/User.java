package com.example.ojackkyoserver.Model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String uid;
    String password;
    String StudentCode;
    String name;
    String nickname;
    String callNumber;
    String email;
    String sns;
    String tag1;
    String tag2;
    String tag3;
    String tag4;
    String tag5;
    String tag6;
    String tag7;
    String tag8;
    String tag9;
    String tag10;
    @Transient
    String[] tags;

    @Transient
    public void setNullTags(String[] tags){
        Field[] fields = getClass().getDeclaredFields();
        for(int i = 0 ; i < tags.length ; i++){
            try {
                System.out.println(fields[i+9].getName());
                fields[i+9].set(this, tags[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Transient
    public List<HashMap> notNullTags(){
        ArrayList result = new ArrayList();
        Field[] fields = getClass().getDeclaredFields();
        for(int i = 9 ; i < 19 ; i++){
            try {
                if(fields[i].get(this) != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", fields[i].get(this));
                    result.add(hashMap);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
