package com.hyd.pojofactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class POJOFixer {

    public static <T> void fix(T t, POJOFactory<T> pojoFactory) {

        if (pojoFactory.emptyAllFields) {
            emptyAllFields(t);
        }

        pojoFactory.randomStringFields.forEach((field, sizes) -> {
            String randomString = ValueFactory.randomString(sizes[0], sizes[1]);
            POJOReflection.setField(t, field, randomString);
        });

        pojoFactory.fixedValues.forEach(
                (field, value) -> POJOReflection.setField(t, field, value));

        pojoFactory.valueSuppliers.forEach(
                (field, supplier) -> POJOReflection.setField(t, field, supplier.get()));

        pojoFactory.longSequenceMap.forEach(
                (field, counter) -> POJOReflection.setField(t, field, counter.getAndIncrement()));

        pojoFactory.intSequenceMap.forEach(
                (field, counter) -> POJOReflection.setField(t, field, counter.getAndIncrement()));

        pojoFactory.roundRobinOptions.forEach((field, values) -> {
            int pointer = pojoFactory.roundRobinPointers.get(field);
            try {
                POJOReflection.setField(t, field, values[pointer]);
            } finally {
                pointer = (pointer + 1) % values.length;
                pojoFactory.roundRobinPointers.put(field, pointer);
            }
        });

        pojoFactory.randomOptions.forEach((field, values) -> {
            Object value = ValueFactory.randomItem(values);
            POJOReflection.setField(t, field, value);
        });
    }

    // 如果可以的话，将所有的属性置为非 null 的默认值。
    private static <T> void emptyAllFields(T t) {
        if (t == null) {
            return;
        }

        try {
            Class type = t.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor.getWriteMethod() == null) {
                    continue;
                }
                emptyField(descriptor, t);
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new POJOFactoryException(e);
        }
    }

    @SuppressWarnings("UnnecessaryBoxing")
    private static <T> void emptyField(
            PropertyDescriptor descriptor, T t
    ) throws IllegalAccessException, InvocationTargetException {

        Class<?> propertyType = descriptor.getPropertyType();

        if (propertyType.isPrimitive()) {
            // The field already be default
            return;
        }

        Object emptyValue;

        if (propertyType == Integer.class) {
            emptyValue = new Integer(0);
        } else if (propertyType == Long.class) {
            emptyValue = new Long(0);
        } else if (propertyType == Double.class) {
            emptyValue = new Double(0);
        } else if (propertyType == Float.class) {
            emptyValue = new Float(0);
        } else if (propertyType == Byte.class) {
            emptyValue = new Byte((byte) 0);
        } else if (propertyType == Character.class) {
            emptyValue = new Character('\0');
        } else if (propertyType == Boolean.class) {
            emptyValue = false;
        } else if (propertyType == String.class) {
            emptyValue = "";
        } else {
            emptyValue = POJOReflection.newInstanceOrNull(propertyType);
            emptyAllFields(emptyValue);
        }

        descriptor.getWriteMethod().invoke(t, emptyValue);
    }
}
