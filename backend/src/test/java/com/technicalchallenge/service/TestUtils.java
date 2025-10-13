package com.technicalchallenge.service;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException ns) {
            // attempt superclasses
            Field f = null;
            Class<?> c = target.getClass().getSuperclass();
            while (c != null) {
                try {
                    f = c.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    c = c.getSuperclass();
                }
            }
            if (f == null) throw new RuntimeException(ns);
            try {
                f.setAccessible(true);
                f.set(target, value);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
