package com.hyd.pojofactory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class User extends Closeable {

    private Long id;

    private String email;

    private Date createTime;

    private Boolean active;

    private Address address;
}
