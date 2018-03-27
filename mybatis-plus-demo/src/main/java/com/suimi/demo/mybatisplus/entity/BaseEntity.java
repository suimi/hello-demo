/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import lombok.*;

/**
 * @author suimi
 * @date 2018-03-27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity extends Model {

    private Long id;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
