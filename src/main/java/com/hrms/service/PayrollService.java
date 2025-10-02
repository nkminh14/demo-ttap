package com.hrms.service;

import com.hrms.model.Payroll;
import com.hrms.util.InputUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PayrollService {
    private static final String FILE_PATH = "data/payroll.txt";
    private List<Payroll> payrollList;
    
    public PayrollService() {
        this.payrollList = new ArrayList<>();
        loadFromFile();
    }
    
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(payrollList);
            System.out.println("Đã lưu dữ liệu lương thưởng vào file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }
    
    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File lương thưởng chưa tồn tại, tạo danh sách mới.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            payrollList = (List<Payroll>) ois.readObject();
            System.out.println("Đã tải dữ liệu lương thưởng từ file thành công!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            payrollList = new ArrayList<>();
        }
    }
    
    public void addPayroll() {
        System.out.println("\n=== THÊM BẢN GHI LƯƠNG THƯỞNG ===");
        
        String payrollId = InputUtil.getNonEmptyString("Nhập mã lương thưởng: ");
        
        if (findPayrollById(payrollId) != null) {
            System.out.println("Mã lương thưởng đã tồn tại!");
            return;
        }
        
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        LocalDate payPeriodStart = InputUtil.getDate("Nhập ngày bắt đầu kỳ lương");
        LocalDate payPeriodEnd = InputUtil.getDate("Nhập ngày kết thúc kỳ lương");
        double baseSalary = InputUtil.getDouble("Nhập lương cơ bản: ");
        double bonus = InputUtil.getDouble("Nhập thưởng: ");
        double overtime = InputUtil.getDouble("Nhập lương làm thêm giờ: ");
        double deductions = InputUtil.getDouble("Nhập các khoản trừ: ");
        
        double netSalary = baseSalary + bonus + overtime - deductions;
        
        System.out.println("Chọn trạng thái:");
        System.out.println("1. Chờ xử lý (PENDING)");
        System.out.println("2. Đã trả (PAID)");
        System.out.println("3. Đã hủy (CANCELLED)");
        int choice = InputUtil.getInt("Lựa chọn: ");
        
        String status;
        switch (choice) {
            case 1: status = "PENDING"; break;
            case 2: status = "PAID"; break;
            case 3: status = "CANCELLED"; break;
            default: status = "PENDING"; break;
        }
        
        String notes = InputUtil.getString("Nhập ghi chú (tùy chọn): ");
        
        Payroll payroll = new Payroll(payrollId, employeeId, payPeriodStart, payPeriodEnd,
                                    baseSalary, bonus, overtime, deductions, netSalary, status, notes);
        payrollList.add(payroll);
        saveToFile();
        
        System.out.println("Đã thêm bản ghi lương thưởng thành công!");
        System.out.println("Lương thực lĩnh: " + String.format("%.0f", netSalary) + " VND");
    }
    
    public void displayAllPayroll() {
        System.out.println("\n=== DANH SÁCH LƯƠNG THƯỞNG ===");
        
        if (payrollList.isEmpty()) {
            System.out.println("Chưa có bản ghi lương thưởng nào!");
            return;
        }
        
        System.out.printf("%-10s %-10s %-12s %-12s %-12s %-10s %-10s %-10s %-12s %-10s %-15s%n",
                "Mã lương", "Mã NV", "Từ ngày", "Đến ngày", "Lương CB", "Thưởng", "Tăng ca", "Trừ", "Thực lĩnh", "Trạng thái", "Ghi chú");
        System.out.println("=".repeat(150));
        
        for (Payroll payroll : payrollList) {
            System.out.printf("%-10s %-10s %-12s %-12s %-12.0f %-10.0f %-10.0f %-10.0f %-12.0f %-10s %-15s%n",
                    payroll.getPayrollId(), payroll.getEmployeeId(),
                    payroll.getPayPeriodStart(), payroll.getPayPeriodEnd(),
                    payroll.getBaseSalary(), payroll.getBonus(), payroll.getOvertime(),
                    payroll.getDeductions(), payroll.getNetSalary(),
                    payroll.getStatus(), payroll.getNotes());
        }
    }
    
    public void searchPayroll() {
        System.out.println("\n=== TÌM KIẾM LƯƠNG THƯỞNG ===");
        System.out.println("1. Tìm theo mã nhân viên");
        System.out.println("2. Tìm theo tháng/năm");
        System.out.println("3. Tìm theo trạng thái");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        List<Payroll> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
                results = payrollList.stream()
                        .filter(p -> p.getEmployeeId().equals(employeeId))
                        .collect(Collectors.toList());
                break;
            case 2:
                int month = InputUtil.getInt("Nhập tháng (1-12): ");
                int year = InputUtil.getInt("Nhập năm: ");
                results = payrollList.stream()
                        .filter(p -> p.getPayPeriodStart().getMonthValue() == month && 
                                   p.getPayPeriodStart().getYear() == year)
                        .collect(Collectors.toList());
                break;
            case 3:
                System.out.println("Chọn trạng thái:");
                System.out.println("1. PENDING");
                System.out.println("2. PAID");
                System.out.println("3. CANCELLED");
                int statusChoice = InputUtil.getInt("Lựa chọn: ");
                String status = "";
                switch (statusChoice) {
                    case 1: status = "PENDING"; break;
                    case 2: status = "PAID"; break;
                    case 3: status = "CANCELLED"; break;
                }
                if (!status.isEmpty()) {
                    final String finalStatus = status;
                    results = payrollList.stream()
                            .filter(p -> p.getStatus().equals(finalStatus))
                            .collect(Collectors.toList());
                }
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy bản ghi lương thưởng nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            displayPayrollList(results);
        }
    }
    
    public void updatePayroll() {
        System.out.println("\n=== CẬP NHẬT LƯƠNG THƯỞNG ===");
        String payrollId = InputUtil.getNonEmptyString("Nhập mã lương thưởng cần cập nhật: ");
        
        Payroll payroll = findPayrollById(payrollId);
        if (payroll == null) {
            System.out.println("Không tìm thấy bản ghi lương thưởng!");
            return;
        }
        
        System.out.println("Thông tin hiện tại:");
        System.out.println(payroll);
        
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
        
        String baseSalary = InputUtil.getString("Lương cơ bản (" + payroll.getBaseSalary() + "): ");
        if (!baseSalary.isEmpty()) {
            try {
                payroll.setBaseSalary(Double.parseDouble(baseSalary));
            } catch (NumberFormatException e) {
                System.out.println("Lương cơ bản không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        String bonus = InputUtil.getString("Thưởng (" + payroll.getBonus() + "): ");
        if (!bonus.isEmpty()) {
            try {
                payroll.setBonus(Double.parseDouble(bonus));
            } catch (NumberFormatException e) {
                System.out.println("Thưởng không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        String overtime = InputUtil.getString("Lương tăng ca (" + payroll.getOvertime() + "): ");
        if (!overtime.isEmpty()) {
            try {
                payroll.setOvertime(Double.parseDouble(overtime));
            } catch (NumberFormatException e) {
                System.out.println("Lương tăng ca không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        String deductions = InputUtil.getString("Các khoản trừ (" + payroll.getDeductions() + "): ");
        if (!deductions.isEmpty()) {
            try {
                payroll.setDeductions(Double.parseDouble(deductions));
            } catch (NumberFormatException e) {
                System.out.println("Khoản trừ không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        double netSalary = payroll.getBaseSalary() + payroll.getBonus() + payroll.getOvertime() - payroll.getDeductions();
        payroll.setNetSalary(netSalary);
        
        System.out.println("Cập nhật trạng thái:");
        System.out.println("1. PENDING");
        System.out.println("2. PAID");
        System.out.println("3. CANCELLED");
        System.out.println("0. Giữ nguyên (" + payroll.getStatus() + ")");
        int statusChoice = InputUtil.getInt("Lựa chọn: ");
        
        switch (statusChoice) {
            case 1: payroll.setStatus("PENDING"); break;
            case 2: payroll.setStatus("PAID"); break;
            case 3: payroll.setStatus("CANCELLED"); break;
        }
        
        String notes = InputUtil.getString("Ghi chú (" + payroll.getNotes() + "): ");
        if (!notes.isEmpty()) payroll.setNotes(notes);
        
        saveToFile();
        System.out.println("Đã cập nhật bản ghi lương thưởng thành công!");
        System.out.println("Lương thực lĩnh mới: " + String.format("%.0f", netSalary) + " VND");
    }
    
    public void deletePayroll() {
        System.out.println("\n=== XÓA BẢN GHI LƯƠNG THƯỞNG ===");
        String payrollId = InputUtil.getNonEmptyString("Nhập mã lương thưởng cần xóa: ");
        
        Payroll payroll = findPayrollById(payrollId);
        if (payroll == null) {
            System.out.println("Không tìm thấy bản ghi lương thưởng!");
            return;
        }
        
        System.out.println("Thông tin bản ghi sẽ bị xóa:");
        System.out.println(payroll);
        
        if (InputUtil.getBoolean("Bạn có chắc chắn muốn xóa bản ghi này?")) {
            payrollList.remove(payroll);
            saveToFile();
            System.out.println("Đã xóa bản ghi lương thưởng thành công!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public void generateSalaryReport() {
        System.out.println("\n=== BÁO CÁO LƯƠNG THƯỞNG ===");
        System.out.println("1. Báo cáo theo nhân viên");
        System.out.println("2. Báo cáo tổng hợp theo tháng");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        
        switch (choice) {
            case 1:
                generateEmployeeSalaryReport();
                break;
            case 2:
                generateMonthlySalaryReport();
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
    
    private void generateEmployeeSalaryReport() {
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        int year = InputUtil.getInt("Nhập năm: ");
        
        List<Payroll> employeePayrolls = payrollList.stream()
                .filter(p -> p.getEmployeeId().equals(employeeId) && 
                           p.getPayPeriodStart().getYear() == year)
                .collect(Collectors.toList());
        
        if (employeePayrolls.isEmpty()) {
            System.out.println("Không có dữ liệu lương thưởng cho nhân viên này trong năm " + year);
            return;
        }
        
        System.out.printf("\nBáo cáo lương thưởng năm %d - Nhân viên: %s%n", year, employeeId);
        System.out.println("=".repeat(80));
        
        double totalBaseSalary = employeePayrolls.stream().mapToDouble(Payroll::getBaseSalary).sum();
        double totalBonus = employeePayrolls.stream().mapToDouble(Payroll::getBonus).sum();
        double totalOvertime = employeePayrolls.stream().mapToDouble(Payroll::getOvertime).sum();
        double totalDeductions = employeePayrolls.stream().mapToDouble(Payroll::getDeductions).sum();
        double totalNetSalary = employeePayrolls.stream().mapToDouble(Payroll::getNetSalary).sum();
        
        System.out.println("Tổng lương cơ bản: " + String.format("%.0f", totalBaseSalary) + " VND");
        System.out.println("Tổng thưởng: " + String.format("%.0f", totalBonus) + " VND");
        System.out.println("Tổng lương tăng ca: " + String.format("%.0f", totalOvertime) + " VND");
        System.out.println("Tổng các khoản trừ: " + String.format("%.0f", totalDeductions) + " VND");
        System.out.println("Tổng lương thực lĩnh: " + String.format("%.0f", totalNetSalary) + " VND");
        System.out.println("Lương trung bình/tháng: " + String.format("%.0f", totalNetSalary / employeePayrolls.size()) + " VND");
        
        System.out.println("\nChi tiết theo tháng:");
        displayPayrollList(employeePayrolls);
    }
    
    private void generateMonthlySalaryReport() {
        int month = InputUtil.getInt("Nhập tháng (1-12): ");
        int year = InputUtil.getInt("Nhập năm: ");
        
        List<Payroll> monthlyPayrolls = payrollList.stream()
                .filter(p -> p.getPayPeriodStart().getMonthValue() == month && 
                           p.getPayPeriodStart().getYear() == year)
                .collect(Collectors.toList());
        
        if (monthlyPayrolls.isEmpty()) {
            System.out.println("Không có dữ liệu lương thưởng trong tháng " + month + "/" + year);
            return;
        }
        
        System.out.printf("\nBáo cáo tổng hợp lương thưởng tháng %d/%d%n", month, year);
        System.out.println("=".repeat(80));
        
        double totalBaseSalary = monthlyPayrolls.stream().mapToDouble(Payroll::getBaseSalary).sum();
        double totalBonus = monthlyPayrolls.stream().mapToDouble(Payroll::getBonus).sum();
        double totalOvertime = monthlyPayrolls.stream().mapToDouble(Payroll::getOvertime).sum();
        double totalDeductions = monthlyPayrolls.stream().mapToDouble(Payroll::getDeductions).sum();
        double totalNetSalary = monthlyPayrolls.stream().mapToDouble(Payroll::getNetSalary).sum();
        
        System.out.println("Số nhân viên được trả lương: " + monthlyPayrolls.size());
        System.out.println("Tổng lương cơ bản: " + String.format("%.0f", totalBaseSalary) + " VND");
        System.out.println("Tổng thưởng: " + String.format("%.0f", totalBonus) + " VND");
        System.out.println("Tổng lương tăng ca: " + String.format("%.0f", totalOvertime) + " VND");
        System.out.println("Tổng các khoản trừ: " + String.format("%.0f", totalDeductions) + " VND");
        System.out.println("Tổng chi phí lương: " + String.format("%.0f", totalNetSalary) + " VND");
        
        System.out.println("\nChi tiết:");
        displayPayrollList(monthlyPayrolls);
    }
    
    public Payroll findPayrollById(String payrollId) {
        return payrollList.stream()
                .filter(payroll -> payroll.getPayrollId().equals(payrollId))
                .findFirst()
                .orElse(null);
    }
    
    private void displayPayrollList(List<Payroll> list) {
        System.out.printf("%-10s %-10s %-12s %-12s %-12s %-10s %-10s %-10s %-12s %-10s %-15s%n",
                "Mã lương", "Mã NV", "Từ ngày", "Đến ngày", "Lương CB", "Thưởng", "Tăng ca", "Trừ", "Thực lĩnh", "Trạng thái", "Ghi chú");
        System.out.println("=".repeat(150));
        
        for (Payroll payroll : list) {
            System.out.printf("%-10s %-10s %-12s %-12s %-12.0f %-10.0f %-10.0f %-10.0f %-12.0f %-10s %-15s%n",
                    payroll.getPayrollId(), payroll.getEmployeeId(),
                    payroll.getPayPeriodStart(), payroll.getPayPeriodEnd(),
                    payroll.getBaseSalary(), payroll.getBonus(), payroll.getOvertime(),
                    payroll.getDeductions(), payroll.getNetSalary(),
                    payroll.getStatus(), payroll.getNotes());
        }
    }
    
    public List<Payroll> getAllPayroll() {
        return new ArrayList<>(payrollList);
    }
}
