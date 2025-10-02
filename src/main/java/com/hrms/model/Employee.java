package com.hrms.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String employeeId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String position;
    private String departmentId;
    private double baseSalary;
    private LocalDate hireDate;
    private String status;
    
    public Employee() {
    }
    
    public Employee(String employeeId, String name, String email, String phone, 
                   String address, String position, String departmentId, 
                   double baseSalary, LocalDate hireDate, String status) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.position = position;
        this.departmentId = departmentId;
        this.baseSalary = baseSalary;
        this.hireDate = hireDate;
        this.status = status;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
    
    public double getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
        @Override
        public String toString() {
            return employeeId + "," + name + "," + email + "," + phone + "," + address + "," + position + "," + departmentId + "," + baseSalary + "," + hireDate + "," + status;
        }

        public static Employee fromString(String line) {
            String[] parts = line.split(",");
            if (parts.length != 10) return null;
            try {
                return new Employee(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    Double.parseDouble(parts[7]),
                    LocalDate.parse(parts[8]),
                    parts[9]
                );
            } catch (Exception e) {
                return null;
            }
        }
}
