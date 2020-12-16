package com.nyl.enrollee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name="enrollee")
public class Enrollee {
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)     // remove this restriction
    private int id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Column(name="activationstatus", columnDefinition = "BOOLEAN", nullable = false)
    private boolean activationStatus;
    @NotBlank
    @Column(columnDefinition = "DATE", nullable = false)
    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING, timezone = "America/New_York")
    private Date dob;
    @Column(name="phonenum", nullable = true)
    private String phoneNum;
    @Column(name="dependentgroupid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int dependentgroupid;

    public Enrollee() {

    }

    public Enrollee(int id, String name, boolean activationStatus, Date dob, int dependentgroupid) {
        this.id = id;
        this.name = name;
        this.activationStatus = activationStatus;
        this.dob = dob;
        this.dependentgroupid = dependentgroupid;
    }

    public int getDependentgroupid() {
        return dependentgroupid;
    }

    public void setDependentgroupid(int dependentgroupid) {
        this.dependentgroupid = dependentgroupid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(boolean activationStatus) {
        this.activationStatus = activationStatus;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "Enrollee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activationStatus=" + activationStatus +
                ", dob=" + dob +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
