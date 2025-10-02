package com.hrms.service;

import com.hrms.model.Department;
import com.hrms.util.InputUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentService {
    private static final String FILE_PATH = "data/departments.txt";
    private List<Department> departments;
    
    public DepartmentService() {
        this.departments = new ArrayList<>();
        loadFromFile();
    }
    
    public void saveToFile() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(FILE_PATH))) {
            for (Department dept : departments) {
                bw.write(dept.toString());
                bw.newLine();
            }
            System.out.println("Đã lưu dữ liệu phòng ban vào file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File phòng ban chưa tồn tại, tạo danh sách mới.");
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Department dept = Department.fromString(line);
                if (dept != null) departments.add(dept);
            }
            System.out.println("Đã tải dữ liệu phòng ban từ file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            departments = new ArrayList<>();
        }
    }
    
    public void addDepartment() {
        System.out.println("\n=== THÊM PHÒNG BAN MỚI ===");
        
        String departmentId = InputUtil.getNonEmptyString("Nhập mã phòng ban: ");
        
        if (findDepartmentById(departmentId) != null) {
            System.out.println("Mã phòng ban đã tồn tại!");
            return;
        }
        
        String departmentName = InputUtil.getNonEmptyString("Nhập tên phòng ban: ");
        String description = InputUtil.getString("Nhập mô tả: ");
        String managerId = InputUtil.getString("Nhập mã quản lý (có thể để trống): ");
        String location = InputUtil.getString("Nhập địa điểm: ");
        int totalEmployees = InputUtil.getInt("Nhập số lượng nhân viên: ");
        
        Department department = new Department(departmentId, departmentName, description, 
                                             managerId, location, totalEmployees);
        departments.add(department);
        saveToFile();
        
        System.out.println("Đã thêm phòng ban thành công!");
    }
    
    public void displayAllDepartments() {
        System.out.println("\n=== DANH SÁCH PHÒNG BAN ===");
        
        if (departments.isEmpty()) {
            System.out.println("Chưa có phòng ban nào!");
            return;
        }
        
        System.out.printf("%-10s %-25s %-30s %-15s %-20s %-10s%n",
                "Mã PB", "Tên phòng ban", "Mô tả", "Quản lý", "Địa điểm", "Số NV");
        System.out.println("=".repeat(120));
        
        for (Department dept : departments) {
            System.out.printf("%-10s %-25s %-30s %-15s %-20s %-10d%n",
                    dept.getDepartmentId(), dept.getDepartmentName(), 
                    dept.getDescription(), dept.getManagerId(), 
                    dept.getLocation(), dept.getTotalEmployees());
        }
    }
    
    public void searchDepartment() {
        System.out.println("\n=== TÌM KIẾM PHÒNG BAN ===");
        System.out.println("1. Tìm theo mã phòng ban");
        System.out.println("2. Tìm theo tên phòng ban");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        List<Department> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String id = InputUtil.getNonEmptyString("Nhập mã phòng ban: ");
                Department dept = findDepartmentById(id);
                if (dept != null) results.add(dept);
                break;
            case 2:
                String name = InputUtil.getNonEmptyString("Nhập tên phòng ban: ");
                results = departments.stream()
                        .filter(d -> d.getDepartmentName().toLowerCase().contains(name.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy phòng ban nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            displayDepartments(results);
        }
    }
    
    public void updateDepartment() {
        System.out.println("\n=== CẬP NHẬT THÔNG TIN PHÒNG BAN ===");
        String departmentId = InputUtil.getNonEmptyString("Nhập mã phòng ban cần cập nhật: ");
        
        Department department = findDepartmentById(departmentId);
        if (department == null) {
            System.out.println("Không tìm thấy phòng ban!");
            return;
        }
        
        System.out.println("Thông tin hiện tại:");
        System.out.println(department);
        
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
        
        String name = InputUtil.getString("Tên phòng ban (" + department.getDepartmentName() + "): ");
        if (!name.isEmpty()) department.setDepartmentName(name);
        
        String description = InputUtil.getString("Mô tả (" + department.getDescription() + "): ");
        if (!description.isEmpty()) department.setDescription(description);
        
        String managerId = InputUtil.getString("Mã quản lý (" + department.getManagerId() + "): ");
        if (!managerId.isEmpty()) department.setManagerId(managerId);
        
        String location = InputUtil.getString("Địa điểm (" + department.getLocation() + "): ");
        if (!location.isEmpty()) department.setLocation(location);
        
        String employees = InputUtil.getString("Số nhân viên (" + department.getTotalEmployees() + "): ");
        if (!employees.isEmpty()) {
            try {
                department.setTotalEmployees(Integer.parseInt(employees));
            } catch (NumberFormatException e) {
                System.out.println("Số nhân viên không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        saveToFile();
        System.out.println("Đã cập nhật thông tin phòng ban thành công!");
    }
    
    public void deleteDepartment() {
        System.out.println("\n=== XÓA PHÒNG BAN ===");
        String departmentId = InputUtil.getNonEmptyString("Nhập mã phòng ban cần xóa: ");
        
        Department department = findDepartmentById(departmentId);
        if (department == null) {
            System.out.println("Không tìm thấy phòng ban!");
            return;
        }
        
        System.out.println("Thông tin phòng ban sẽ bị xóa:");
        System.out.println(department);
        
        if (InputUtil.getBoolean("Bạn có chắc chắn muốn xóa phòng ban này?")) {
            departments.remove(department);
            saveToFile();
            System.out.println("Đã xóa phòng ban thành công!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public Department findDepartmentById(String departmentId) {
        return departments.stream()
                .filter(dept -> dept.getDepartmentId().equals(departmentId))
                .findFirst()
                .orElse(null);
    }
    
    private void displayDepartments(List<Department> departmentList) {
        System.out.printf("%-10s %-25s %-30s %-15s %-20s %-10s%n",
                "Mã PB", "Tên phòng ban", "Mô tả", "Quản lý", "Địa điểm", "Số NV");
        System.out.println("=".repeat(120));
        
        for (Department dept : departmentList) {
            System.out.printf("%-10s %-25s %-30s %-15s %-20s %-10d%n",
                    dept.getDepartmentId(), dept.getDepartmentName(), 
                    dept.getDescription(), dept.getManagerId(), 
                    dept.getLocation(), dept.getTotalEmployees());
        }
    }
    
    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments);
    }
}
