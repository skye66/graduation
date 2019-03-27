package com.gdut.graduation.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 类目的数据库映射
 * @Author Skye
 * @Date 2019/3/26 17:31
 * @Version 1.0
 **/
@Data
public class Category {
    /**
     * 类目的id
     */
    private Integer id;
    /**
     * 类目的父级id
     */
//    @JsonProperty("parent_id")
    private Integer parentId;
    /**
     * 类目的名字
     */
    private String name;
    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonIgnore
    private Date updateTime;
}
