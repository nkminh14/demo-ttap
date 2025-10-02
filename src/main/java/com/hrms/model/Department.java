package com.hrms.model;

import java.io.Serializable;

public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String departmentId;
    private String departmentName;
    private String description;
    private String managerId;
    private String location;
    private int totalEmployees;
    
    public Department() {
    }
    
    public Department(String departmentId, String departmentName, String description, 
                     String managerId, String location, int totalEmployees) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.description = description;
        this.managerId = managerId;
        this.location = location;
        this.totalEmployees = totalEmployees;
    }
    
    public String getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getTotalEmployees() {
        return totalEmployees;
    }
    
    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
    
    @Override
    public String toString() {
        return departmentId + "," + departmentName + "," + description + "," + managerId + "," + location + "," + totalEmployees;
    }

    public static Department fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 6) return null;
        try {
            return new Department(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                Integer.parseInt(parts[5])
            );
        } catch (Exception e) {
            return null;
        }
    }
}
