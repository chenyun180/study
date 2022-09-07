package com.cloud.common.model.test;

import com.cloud.common.model.BaseModel;

import java.util.Date;

public class PersonModel extends BaseModel{

    private Long id;

    private Integer personAge;

    private String personName;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPersonAge() {
        return personAge;
    }

    public void setPersonAge(Integer personAge) {
        this.personAge = personAge;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", personAge=" + personAge +
                ", personName='" + personName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
