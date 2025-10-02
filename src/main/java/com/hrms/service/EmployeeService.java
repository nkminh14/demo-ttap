package com.hrms.service;

import com.hrms.model.Employee;
import com.hrms.util.InputUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {
    private static final String FILE_PATH = "data/employees.txt";
    private List<Employee> employees;

    public EmployeeService() {
        this.employees = new ArrayList<>();
        loadFromFile();
    }

    public void saveToFile() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(FILE_PATH))) {
            for (Employee emp : employees) {
                bw.write(emp.toString());
                bw.newLine();
            }
            System.out.println("Đã lưu dữ liệu nhân viên vào file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File nhân viên chưa tồn tại, tạo danh sách mới.");
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Employee emp = Employee.fromString(line);
                if (emp != null) employees.add(emp);
            }
            System.out.println("Đã tải dữ liệu nhân viên từ file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            employees = new ArrayList<>();
        }
    }
    
    public void addEmployee() {
        System.out.println("\n=== THÊM NHÂN VIÊN MỚI ===");
        
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        
        if (findEmployeeById(employeeId) != null) {
            System.out.println("Mã nhân viên đã tồn tại!");
            return;
        }
        
        String name = InputUtil.getNonEmptyString("Nhập tên nhân viên: ");
        String email = InputUtil.getNonEmptyString("Nhập email: ");
        String phone = InputUtil.getNonEmptyString("Nhập số điện thoại: ");
        String address = InputUtil.getNonEmptyString("Nhập địa chỉ: ");
        String position = InputUtil.getNonEmptyString("Nhập chức vụ: ");
        String departmentId = InputUtil.getNonEmptyString("Nhập mã phòng ban: ");
        double baseSalary = InputUtil.getDouble("Nhập lương cơ bản: ");
        LocalDate hireDate = InputUtil.getDate("Nhập ngày vào làm");
        
        System.out.println("Chọn loại nhân viên:");
        System.out.println("1. Fulltime");
        System.out.println("2. Parttime");
        int choice = InputUtil.getInt("Lựa chọn: ");
        String status = (choice == 1) ? "FULLTIME" : "PARTTIME";
        
        Employee employee = new Employee(employeeId, name, email, phone, address, 
                                       position, departmentId, baseSalary, hireDate, status);
        employees.add(employee);
        saveToFile();
        
        System.out.println("Đã thêm nhân viên thành công!");
    }
    
    public void displayAllEmployees() {
        System.out.println("\n=== DANH SÁCH NHÂN VIÊN ===");
        
        if (employees.isEmpty()) {
            System.out.println("Chưa có nhân viên nào!");
            return;
        }
        
        System.out.printf("%-10s %-20s %-25s %-15s %-20s %-15s %-10s %-15s %-12s %-10s%n",
                "Mã NV", "Tên", "Email", "Điện thoại", "Địa chỉ", "Chức vụ", "Phòng ban", "Lương CB", "Ngày vào", "Loại");
        System.out.println("=".repeat(150));
        
        for (Employee emp : employees) {
            System.out.printf("%-10s %-20s %-25s %-15s %-20s %-15s %-10s %-15.0f %-12s %-10s%n",
                    emp.getEmployeeId(), emp.getName(), emp.getEmail(), emp.getPhone(),
                    emp.getAddress(), emp.getPosition(), emp.getDepartmentId(),
                    emp.getBaseSalary(), emp.getHireDate(), emp.getStatus());
        }
    }
    
    public void searchEmployee() {
        System.out.println("\n=== TÌM KIẾM NHÂN VIÊN ===");
        System.out.println("1. Tìm theo mã nhân viên");
        System.out.println("2. Tìm theo tên");
        System.out.println("3. Tìm theo phòng ban");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        List<Employee> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String id = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
                Employee emp = findEmployeeById(id);
                if (emp != null) results.add(emp);
                break;
            case 2:
                String name = InputUtil.getNonEmptyString("Nhập tên nhân viên: ");
                results = employees.stream()
                        .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            case 3:
                String deptId = InputUtil.getNonEmptyString("Nhập mã phòng ban: ");
                results = employees.stream()
                        .filter(e -> e.getDepartmentId().equals(deptId))
                        .collect(Collectors.toList());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy nhân viên nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            displayEmployees(results);
        }
    }
    
    public void updateEmployee() {
        System.out.println("\n=== CẬP NHẬT THÔNG TIN NHÂN VIÊN ===");
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên cần cập nhật: ");
        
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        
        System.out.println("Thông tin hiện tại:");
        System.out.println(employee);
        
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
        
        String name = InputUtil.getString("Tên (" + employee.getName() + "): ");
        if (!name.isEmpty()) employee.setName(name);
        
        String email = InputUtil.getString("Email (" + employee.getEmail() + "): ");
        if (!email.isEmpty()) employee.setEmail(email);
        
        String phone = InputUtil.getString("Điện thoại (" + employee.getPhone() + "): ");
        if (!phone.isEmpty()) employee.setPhone(phone);
        
        String address = InputUtil.getString("Địa chỉ (" + employee.getAddress() + "): ");
        if (!address.isEmpty()) employee.setAddress(address);
        
        String position = InputUtil.getString("Chức vụ (" + employee.getPosition() + "): ");
        if (!position.isEmpty()) employee.setPosition(position);
        
        String salary = InputUtil.getString("Lương cơ bản (" + employee.getBaseSalary() + "): ");
        if (!salary.isEmpty()) {
            try {
                employee.setBaseSalary(Double.parseDouble(salary));
            } catch (NumberFormatException e) {
                System.out.println("Lương không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        saveToFile();
        System.out.println("Đã cập nhật thông tin nhân viên thành công!");
    }
    
    public void deleteEmployee() {
        System.out.println("\n=== XÓA NHÂN VIÊN ===");
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên cần xóa: ");
        
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Không tìm thấy nhân viên!");
            return;
        }
        
        System.out.println("Thông tin nhân viên sẽ bị xóa:");
        System.out.println(employee);
        
        if (InputUtil.getBoolean("Bạn có chắc chắn muốn xóa nhân viên này?")) {
            employees.remove(employee);
            saveToFile();
            System.out.println("Đã xóa nhân viên thành công!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public Employee findEmployeeById(String employeeId) {
        return employees.stream()
                .filter(emp -> emp.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Employee> getEmployeesByDepartment(String departmentId) {
        return employees.stream()
                .filter(emp -> emp.getDepartmentId().equals(departmentId))
                .collect(Collectors.toList());
    }
    
    public List<Employee> getEmployeesByStatus(String status) {
        return employees.stream()
                .filter(emp -> emp.getStatus().equals(status))
                .collect(Collectors.toList());
    }
    
    private void displayEmployees(List<Employee> employeeList) {
        System.out.printf("%-10s %-20s %-25s %-15s %-20s %-15s %-10s %-15s %-12s %-10s%n",
                "Mã NV", "Tên", "Email", "Điện thoại", "Địa chỉ", "Chức vụ", "Phòng ban", "Lương CB", "Ngày vào", "Loại");
        System.out.println("=".repeat(150));
        
        for (Employee emp : employeeList) {
            System.out.printf("%-10s %-20s %-25s %-15s %-20s %-15s %-10s %-15.0f %-12s %-10s%n",
                    emp.getEmployeeId(), emp.getName(), emp.getEmail(), emp.getPhone(),
                    emp.getAddress(), emp.getPosition(), emp.getDepartmentId(),
                    emp.getBaseSalary(), emp.getHireDate(), emp.getStatus());
        }
    }
    
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
}
