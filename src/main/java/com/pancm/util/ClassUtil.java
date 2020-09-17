package com.pancm.util;

import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/9/17
 */

public class ClassUtil {

    private ClassUtil() {
    }


    /**
     * 获取包下面的所有 class 默认循环获取子包下面的内容
     *
     * @param packName 包名
     */
    public static Set<Class<?>> getPackClasses(String packName) throws IOException, ClassNotFoundException {
        return getPackClasses(packName, true, null);
    }

    /**
     * 获取包下面的所有 class
     *
     * @param packName  包名
     * @param recursive 是否循环获取子包下面的内容
     */
    public static Set<Class<?>> getPackClasses(String packName, boolean recursive) throws IOException, ClassNotFoundException {
        return getPackClasses(packName, recursive, null);
    }

    /**
     * 获取包下面的所有cla类型的class
     *
     * @param packName 包名
     * @param cla      如果有 则只获取此种类型的Class ; 如果为null 则 获取所有类型的Class
     */
    public static Set<Class<?>> getPackClasses(String packName, Class<?> cla) throws IOException, ClassNotFoundException {
        return getPackClasses(packName, true, cla);
    }


    /**
     * @param packName  包名
     * @param recursive 是否循环获取子包下面的内容
     * @param cla       如果有 则只获取此种类型的Class ; 如果为null 则 获取所有类型的Class
     */
    public static Set<Class<?>> getPackClasses(String packName, boolean recursive, Class<?> cla) throws IOException, ClassNotFoundException {
        //获取包的路径
        Set<Class<?>> result = new HashSet<>();
        String packDirName = packName.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packDirName);
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                // 获取包的物理路径
                String packPath = URLDecoder.decode(url.getFile(), "UTF-8");
                getPackAllClassesByFile(packName, packPath, recursive, result, cla);
            } else if ("jar".equals(protocol)) {
                getPackAllClassesByJar(packName, packDirName, url, result, cla);
            }
        }
        return result;
    }


    /**
     * 获取包下面所有的 类名
     *
     * @param packName  包名
     * @param packPath  包路径
     * @param recursive 是否遍历获取子包
     * @param claNames  类名结果集
     */
    public static Set<String> getPackAllClassesNameByFile(String packName, String packPath, boolean recursive, Set<String> claNames) {
        File dir = new File(packPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return claNames;
        }
        //获取所有以.class结尾的文件
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
        if (dirFiles != null && dirFiles.length != 0) {
            for (File file : dirFiles) {
                // 如果是目录 则继续扫描
                if (file.isDirectory()) {
                    getPackAllClassesNameByFile(packName + "." + file.getName(), file.getAbsolutePath(), recursive, claNames);
                } else {
                    // 如果是java类文件 去掉后面的.class 只留下类名
                    String claName = file.getName().substring(0, file.getName().length() - 6);
                    claNames.add(packName + "." + claName);
                }
            }
        }
        return claNames;
    }


    /**
     * @param packName  包名
     * @param packPath  包路径
     * @param recursive 是否遍历获取子包
     * @param classes   类结果集
     * @param cla       如果有 则只获取此种类型的Class ; 如果为null 则 获取所有类型的Class
     */
    public static void getPackAllClassesByFile(String packName, String packPath, boolean recursive, Set<Class<?>> classes, Class<?> cla) throws ClassNotFoundException {
        Set<String> claNames = getPackAllClassesNameByFile(packName, packPath, recursive, new HashSet<>());
        if (cla != null) {
            for (String claName : claNames) {
                Class<?> clz = Thread.currentThread().getContextClassLoader().loadClass(claName);
                if (cla.isAssignableFrom(clz)) classes.add(clz);
            }
        } else {
            for (String claName : claNames) {
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(claName));
            }
        }
    }


    /**
     * @param packName    包名
     * @param packDirName 包的文件夹路径
     * @param url         文件URL
     * @param classes     类结果集
     * @param cla         如果有 则只获取此种类型的Class ; 如果为null 则 获取所有类型的Class
     */
    public static void getPackAllClassesByJar(String packName, String packDirName, URL url, Set<Class<?>> classes, Class<?> cla) throws IOException, ClassNotFoundException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.charAt(0) == '/') name = name.substring(1);
            if (name.startsWith(packDirName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                    packName = name.substring(0, idx).replace('/', '.');
                }
                if (name.endsWith(".class") && !entry.isDirectory()) {
                    String className = name.substring(packName.length() + 1, name.length() - 6);
                    Class<?> clz = Class.forName(packName + '.' + className);
                    if (cla != null) {
                        if (cla.isAssignableFrom(clz)) classes.add(clz);
                    } else {
                        classes.add(clz);
                    }
                }
            }
        }
    }


    /**
     * 设置对象私有属性值
     *
     * @param source    实例对象
     * @param fieldName 成员变量名
     * @param target    目标值
     */
    public static void setFieldValue(Object source, String fieldName, Object target) {
        Field field = ReflectionUtils.findField(source.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, source, target);
        }
    }

    /**
     * 调用无参的私有方法
     *
     * @param source     目标类
     * @param methodName 方法名字
     * @return 调用方法返回结果
     */
    public static Object invokeNoArgsMethod(Object source, String methodName) throws NoSuchMethodException {
        return invokeArgsMethod(source, methodName, null);
    }

    /**
     * 调用有参的私有方法(注意，参数列表和参数类型必须对应)
     *
     * @param instance   目标类
     * @param methodName 方法名字
     * @param args       参数列表
     * @param argsTypes  参数类型
     * @return 调用方法返回结果
     */
    public static Object invokeArgsMethod(Object instance, String methodName, Object[] args, Class<?>... argsTypes) throws NoSuchMethodException {
        Method method = ReflectionUtils.findMethod(instance.getClass(), methodName, argsTypes);
        if (method == null) throw new NoSuchMethodException();
        ReflectionUtils.makeAccessible(method);
        return ReflectionUtils.invokeMethod(method, instance, args);
    }


}