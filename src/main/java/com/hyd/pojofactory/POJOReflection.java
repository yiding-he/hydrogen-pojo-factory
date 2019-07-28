package com.hyd.pojofactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class POJOReflection {

    public static void setField(Object object, String field, Object fieldValue) {
        try {
            PropertyDescriptor[] descriptors =
                    Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();

            for (PropertyDescriptor descriptor : descriptors) {
                if (descriptor.getName().equals(field) && descriptor.getWriteMethod() != null) {
                    descriptor.getWriteMethod().invoke(object, fieldValue);
                    return;
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new POJOFactoryException(e);
        }
    }

    public static <T> T newInstanceOrNull(Class<T> type) {
        try {
            return usingDefaultConstructor(type);
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    private static <T> T usingDefaultConstructor(Class<T> type)
            throws InstantiationException, IllegalAccessException {
        return type.newInstance();
    }
}
