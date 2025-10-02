package com.hrms.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String payrollId;
    private String employeeId;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private double baseSalary;
    private double bonus;
    private double overtime;
    private double deductions;
    private double netSalary;
    private String status;
    private String notes;
    
    public Payroll() {
    }
    
    public Payroll(String payrollId, String employeeId, LocalDate payPeriodStart, 
                   LocalDate payPeriodEnd, double baseSalary, double bonus, 
                   double overtime, double deductions, double netSalary, 
                   String status, String notes) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.overtime = overtime;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.status = status;
        this.notes = notes;
    }
    
    public String getPayrollId() {
        return payrollId;
    }
    
    public void setPayrollId(String payrollId) {
        this.payrollId = payrollId;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public LocalDate getPayPeriodStart() {
        return payPeriodStart;
    }
    
    public void setPayPeriodStart(LocalDate payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }
    
    public LocalDate getPayPeriodEnd() {
        return payPeriodEnd;
    }
    
    public void setPayPeriodEnd(LocalDate payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }
    
    public double getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public double getBonus() {
        return bonus;
    }
    
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
    public double getOvertime() {
        return overtime;
    }
    
    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }
    
    public double getDeductions() {
        return deductions;
    }
    
    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }
    
    public double getNetSalary() {
        return netSalary;
    }
    
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "Payroll{" +
                "payrollId='" + payrollId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", payPeriodStart=" + payPeriodStart +
                ", payPeriodEnd=" + payPeriodEnd +
                ", baseSalary=" + baseSalary +
                ", bonus=" + bonus +
                ", overtime=" + overtime +
                ", deductions=" + deductions +
                ", netSalary=" + netSalary +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
