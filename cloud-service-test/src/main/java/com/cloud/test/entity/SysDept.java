package com.cloud.test.entity;

public class SysDept {

    private Long deptId;
    private String deptName;
    private String leader;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Override
    public String toString() {
        return "SysDept{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", leader='" + leader + '\'' +
                '}';
    }
}
