package io.github.hellomaker.encipherjar.classloader;

import io.github.hellomaker.encipherjar.classloader.claz.Claz;
import io.github.hellomaker.encipherjar.classloader.claz.ClazManager;

import java.io.FileNotFoundException;

/**
 * @author hellomaker
 */
public class MyClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] result = getClassFromCustomPath(name);
        if (result == null) {
//            return super.loadClass(name);
            throw new ClassNotFoundException(name);
        } else {
            //defineClass和findClass搭配使用
            return defineClass(name, result, 0, result.length);
        }

    }

    private byte[] getClassFromCustomPath(String name) {
        Claz claz = ClazManager.getInstance().getClaz(name);
        if (claz != null) {
            return claz.getData();
        }
        return null;
    }
}
