package com.hyd.pojofactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class POJOFactory<T> {

    //////////////////////////////////////////////////////////// static methods

    public static <T> T nullObject() {
        return null;
    }

    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> List<T> nullList(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Parameter can not be negative.");
        }

        return Collections.nCopies(size, null);
    }

    public static <T> POJOFactory<T> buildWithType(Class<T> type) {
        return new POJOFactory<>(type);
    }

    //////////////////////////////////////////////////////////// instance members & methods

    Class<T> type;

    boolean emptyAllFields;

    Map<String, int[]> randomStringFields = new HashMap<>();

    Map<String, Object> fixedValues = new HashMap<>();

    Map<String, Supplier<?>> valueSuppliers = new HashMap<>();

    Map<String, AtomicLong> longSequenceMap = new HashMap<>();

    Map<String, AtomicInteger> intSequenceMap = new HashMap<>();

    Map<String, Object[]> roundRobinOptions = new HashMap<>();

    Map<String, Integer> roundRobinPointers = new HashMap<>();

    Map<String, Object[]> randomOptions = new HashMap<>();

    private POJOFactory(Class<T> type) {
        this.type = type;
    }

    ////////////////////////////////////////////////////////////

    public POJOFactory<T> emptyAllFields() {
        this.emptyAllFields = true;
        return this;
    }

    public POJOFactory<T> randomString(String field, int minLength, int maxLength) {
        this.randomStringFields.put(field, new int[]{minLength, maxLength});
        return this;
    }

    public POJOFactory<T> setField(String fieldName, Object value) {
        this.fixedValues.put(fieldName, value);
        return this;
    }

    public POJOFactory<T> setField(String fieldName, Supplier<?> valueSupplier) {
        this.valueSuppliers.put(fieldName, valueSupplier);
        return this;
    }

    public POJOFactory<T> longSequence(String fieldName, long sequenceStart) {
        this.longSequenceMap.put(fieldName, new AtomicLong(sequenceStart));
        return this;
    }

    public POJOFactory<T> intSequence(String fieldName, int sequenceStart) {
        this.intSequenceMap.put(fieldName, new AtomicInteger(sequenceStart));
        return this;
    }

    public <V> POJOFactory<T> options(String fieldName, OptionOrder order, V... values) {
        if (order == OptionOrder.RoundRobin) {
            this.roundRobinOptions.put(fieldName, values);
            this.roundRobinPointers.put(fieldName, 0);
        } else {
            this.randomOptions.put(fieldName, values);
        }
        return this;
    }

    ////////////////////////////////////////////////////////////

    private void fixInstance(T t) {
        if (t != null) {
            POJOFixer.fix(t, this);
        }
    }

    public T oneInstance() {
        T t = POJOReflection.newInstanceOrNull(type);
        fixInstance(t);
        return t;
    }

    public List<T> list(int size) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(oneInstance());
        }
        return result;
    }
}
