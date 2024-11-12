package io.github.hellomaker.encipherjar;

import io.github.hellomaker.encipherjar.classloader.MyClassLoader;
import io.github.hellomaker.encipherjar.classloader.claz.Claz;
import io.github.hellomaker.encipherjar.classloader.claz.ClazManager;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DemoRun {

    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        // 设置线程上下文类加载器
        Thread.currentThread().setContextClassLoader(myClassLoader);

        File file = new File("C:\\oxygen\\java\\encipher_jar\\jar/praise.server-1.0-SNAPSHOT.jar");
        try {
            recursionUnzip(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Class<?> aClass = null;
        try {
            aClass = Class.forName("io.github.hellomaker.praise.BootApplication", true, myClassLoader);
            Method main = aClass.getMethod("main", String[].class);
            main.invoke(null, (Object) new String[]{});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


    }

    private static void recursionUnzip(InputStream inputStream) {
        try (ZipInputStream zipIn = new ZipInputStream(inputStream)) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = entry.getName();
                if (!entry.isDirectory()) {
//                    extractFile(zipIn, filePath);
                    byte[] bytes = zipIn.readAllBytes();
//                    System.out.println("file : " + filePath + ", size : " + bytes.length);
                    if (filePath.endsWith(".jar")) {
                        System.out.println("recursion -------------> " );
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        recursionUnzip(byteArrayInputStream);
                    }
                    if (filePath.endsWith(".class")) {
                        Claz claz = new Claz();
                        claz.setData(bytes);
                        claz.setName(filePath.replace("/", ".").substring(0, filePath.length() - 6));
                        if (claz.getName().startsWith("BOOT-INF.classes")) {
                            claz.setName(claz.getName().substring(17));
                        }
                        ClazManager.getInstance().addClaz(claz);
                    }
                } else {
//                    File dirEntry = new File(filePath);
//                    dirEntry.mkdirs();
//                    System.out.println("dir : " + filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[1024];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }


}
