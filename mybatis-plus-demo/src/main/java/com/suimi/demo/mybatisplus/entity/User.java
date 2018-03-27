/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.entity;

import lombok.*;

/**
 * @author suimi
 * @date 2018-03-27
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String userName;

    private PhoneEnum phone;

}
