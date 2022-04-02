package de.craftsarmy.client.utils;

import java.lang.reflect.InvocationTargetException;

public class Touch<E> {

    public E touch(Class<? extends E> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (E) clazz.getDeclaredConstructor().newInstance();
    }

}
