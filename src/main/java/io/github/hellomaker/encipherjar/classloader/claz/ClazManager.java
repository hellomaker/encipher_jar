package io.github.hellomaker.encipherjar.classloader.claz;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hellomaker
 */
public class ClazManager {

    @Getter
    private static ClazManager instance;
    static {
        instance = new ClazManager();
    }
    private ClazManager(){}

    Map<String, Claz> clazMap = new ConcurrentHashMap<>();

    public void addClaz(Claz claz){
        if (claz.getName().startsWith("io.github.hellomaker.praise")) {
            System.out.println(claz.getName());
        }
        clazMap.put(claz.getName(), claz);
    }

    public Claz getClaz(String name){
        return clazMap.get(name);
    }



}
