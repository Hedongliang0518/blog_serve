package com.hdl.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 分类表(HdlCategory)表实体类
 *
 * @author makejava
 * @since 2024-07-02 14:53:39
 */
@SuppressWarnings("serial")
@Data
@TableName("hdl_category")
public class Category {

    @TableId
    private Long id;
//分类名
    private String name;
//父分类id，如果没有父分类为-1
    private Long pid;
//描述
    private String description;
//状态0:正常,1禁用
    private String status;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
//删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
}

