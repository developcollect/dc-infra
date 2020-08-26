package com.developcollect.dcinfra.utils;

import lombok.Data;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


public class CglibUtilTest {

    @Test
    public void testt() {
        Map<String, String> map = new HashMap<>();

        map.put("test", "hahahah");
        Person person = new Person(5);
        person.setAge(45);


        Object o = CglibUtil.dynamicField(person, map);

        System.out.println(o instanceof Person);
        System.out.println(o);


    }


    @Test
    public void testt2() {

        Person person = new Person(5);
        person.setAge(45);
        person.setName("旺旺");

        Person o = CglibUtil.proxy(person, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                if (method.getName().equals("getAge")) {
                    return 15;
                }
                return methodProxy.invokeSuper(o, objects);
            }
        });

        System.out.println(o instanceof Person);
        System.out.println(o);

        System.out.println(o.getAge());
        System.out.println(o.getName());
    }


    @Test
    public void test3() {
        Class<Person> personClass = Person.class;
        Constructor<?>[] constructors = personClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            for (Parameter parameter : parameters) {
                boolean varArgs = parameter.isVarArgs();
            }
//            constructor.
        }
    }
}


@Data
class Person {

    private int age;

    private int mm;

    private String name;


    public Person(int age) {
        this.age = age;
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }
}