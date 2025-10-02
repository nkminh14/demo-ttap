package com.hrms.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String projectId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String managerId;
    private double budget;
    private String priority;
    
    public Project() {
    }
    
    public Project(String projectId, String projectName, String description, 
                   LocalDate startDate, LocalDate endDate, String status, 
                   String managerId, double budget, String priority) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.managerId = managerId;
        this.budget = budget;
        this.priority = priority;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    @Override
    public String toString() {
        return projectId + "," + projectName + "," + description + "," + startDate + "," + endDate + "," + status + "," + managerId + "," + budget + "," + priority;
    }

    public static Project fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 9) return null;
        try {
            return new Project(
                parts[0],
                parts[1],
                parts[2],
                java.time.LocalDate.parse(parts[3]),
                java.time.LocalDate.parse(parts[4]),
                parts[5],
                parts[6],
                Double.parseDouble(parts[7]),
                parts[8]
            );
        } catch (Exception e) {
            return null;
        }
    }
}
