package com.hyd.pojofactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class POJOFactoryTest {

    @Test
    public void createNull() throws Exception {
        Object nullObject = POJOFactory.nullObject();
        assertNull(nullObject);
    }

    @Test
    public void createEmptyList() throws Exception {
        List<Object> emptyList = POJOFactory.emptyList();
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void createNullList() throws Exception {
        List<Object> nullList = POJOFactory.nullList(10);
        assertNotNull(nullList);
        assertEquals(10, nullList.size());
        nullList.forEach(Assert::assertNull);
    }

    @Test
    public void createEmptyPojo() throws Exception {
        User user = POJOFactory
                .buildWithType(User.class)
                .emptyAllFields()
                .oneInstance();

        assertNotNull(user);
        assertEquals(new Long(0), user.getId());
        assertEquals(Boolean.FALSE, user.getClosed());
        System.out.println(user);
    }

    @Test
    public void createEmptyPojoList() throws Exception {
        List<User> users = POJOFactory
                .buildWithType(User.class)
                .emptyAllFields()
                .list(10);

        assertNotNull(users);
        assertEquals(10, users.size());
        assertNotNull(users.get(0));
        assertEquals(new Long(0), users.get(1).getId());
        assertEquals(Boolean.FALSE, users.get(2).getClosed());
    }

    @Test
    public void randomValue() throws Exception {
        User user = POJOFactory
                .buildWithType(User.class)
                .randomString("email", 30, 30)
                .oneInstance();

        assertNotNull(user);
        assertNotNull(user.getEmail());
        System.out.println(user);
    }

    @Test
    public void customFieldValue() throws Exception {
        User user = POJOFactory
                .buildWithType(User.class)
                .setField("email", "abc@123.com")
                .oneInstance();

        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertEquals("abc@123.com", user.getEmail());
    }

    @Test
    public void sequenceFieldValue() throws Exception {
        List<User> users = POJOFactory
                .buildWithType(User.class)
                .longSequence("id", 1)
                .list(5);

        assertNotNull(users);
        assertEquals(5, users.size());
        assertEquals(new Long(1), users.get(0).getId());
        assertEquals(new Long(2), users.get(1).getId());
        assertEquals(new Long(3), users.get(2).getId());
    }

    @Test
    public void customValueSupplier() throws Exception {
        User user = POJOFactory
                .buildWithType(User.class)
                .setField("email", () -> "abc@123.com")
                .oneInstance();

        assertNotNull(user);
        assertNotNull(user.getEmail());
        assertEquals("abc@123.com", user.getEmail());
    }

    @Test
    public void roundRobinOptions() throws Exception {
        List<User> users = POJOFactory
                .buildWithType(User.class)
                .options("email", OptionOrder.RoundRobin,
                        "email1.123.com", "email2.123.com", "email3.123.com")
                .list(5);

        assertNotNull(users);
        assertEquals(5, users.size());
        assertEquals("email1.123.com", users.get(0).getEmail());
        assertEquals("email2.123.com", users.get(1).getEmail());
        assertEquals("email3.123.com", users.get(2).getEmail());
        assertEquals("email1.123.com", users.get(3).getEmail());
        assertEquals("email2.123.com", users.get(4).getEmail());
    }

    @Test
    public void randomOptions() throws Exception {
        List<User> users = POJOFactory
                .buildWithType(User.class)
                .options("email", OptionOrder.Random,
                        "email1.123.com", "email2.123.com", "email3.123.com")
                .list(5);

        assertNotNull(users);
        assertEquals(5, users.size());
        users.forEach(System.out::println);
    }
}