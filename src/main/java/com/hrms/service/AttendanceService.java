package com.hrms.service;

import com.hrms.model.Attendance;
import com.hrms.util.InputUtil;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceService {
    private static final String FILE_PATH = "data/attendance.txt";
    private List<Attendance> attendanceList;
    
    public AttendanceService() {
        this.attendanceList = new ArrayList<>();
        loadFromFile();
    }
    
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(attendanceList);
            System.out.println("Đã lưu dữ liệu chấm công vào file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }
    
    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File chấm công chưa tồn tại, tạo danh sách mới.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            attendanceList = (List<Attendance>) ois.readObject();
            System.out.println("Đã tải dữ liệu chấm công từ file thành công!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            attendanceList = new ArrayList<>();
        }
    }
    
    public void addAttendance() {
        System.out.println("\n=== THÊM BẢN GHI CHẤM CÔNG ===");
        
        String attendanceId = InputUtil.getNonEmptyString("Nhập mã chấm công: ");
        
        if (findAttendanceById(attendanceId) != null) {
            System.out.println("Mã chấm công đã tồn tại!");
            return;
        }
        
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        LocalDate date = InputUtil.getDate("Nhập ngày chấm công");
        
        if (hasAttendanceForDate(employeeId, date)) {
            System.out.println("Nhân viên đã có bản ghi chấm công trong ngày này!");
            return;
        }
        
        LocalTime checkInTime = InputUtil.getTime("Nhập giờ vào");
        LocalTime checkOutTime = null;
        double hoursWorked = 0;
        
        String checkOutInput = InputUtil.getString("Nhập giờ ra (Enter nếu chưa ra): ");
        if (!checkOutInput.isEmpty()) {
            try {
                checkOutTime = LocalTime.parse(checkOutInput);
                hoursWorked = calculateHoursWorked(checkInTime, checkOutTime);
            } catch (Exception e) {
                System.out.println("Giờ ra không hợp lệ, để trống.");
            }
        }
        
        String status = determineStatus(checkInTime, checkOutTime);
        String notes = InputUtil.getString("Nhập ghi chú (tùy chọn): ");
        
        Attendance attendance = new Attendance(attendanceId, employeeId, date, 
                                             checkInTime, checkOutTime, hoursWorked, status, notes);
        attendanceList.add(attendance);
        saveToFile();
        
        System.out.println("Đã thêm bản ghi chấm công thành công!");
    }
    
    public void checkOut() {
        System.out.println("\n=== CHECK OUT ===");
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        LocalDate today = LocalDate.now();
        
        Attendance attendance = findAttendanceByEmployeeAndDate(employeeId, today);
        if (attendance == null) {
            System.out.println("Không tìm thấy bản ghi chấm công hôm nay cho nhân viên này!");
            return;
        }
        
        if (attendance.getCheckOutTime() != null) {
            System.out.println("Nhân viên đã check out rồi!");
            System.out.println("Giờ ra: " + attendance.getCheckOutTime());
            return;
        }
        
        LocalTime checkOutTime = LocalTime.now();
        attendance.setCheckOutTime(checkOutTime);
        attendance.setHoursWorked(calculateHoursWorked(attendance.getCheckInTime(), checkOutTime));
        attendance.setStatus(determineStatus(attendance.getCheckInTime(), checkOutTime));
        
        saveToFile();
        System.out.println("Check out thành công!");
        System.out.println("Giờ ra: " + checkOutTime);
        System.out.println("Tổng giờ làm: " + attendance.getHoursWorked() + " giờ");
    }
    
    public void displayAllAttendance() {
        System.out.println("\n=== DANH SÁCH CHẤM CÔNG ===");
        
        if (attendanceList.isEmpty()) {
            System.out.println("Chưa có bản ghi chấm công nào!");
            return;
        }
        
        System.out.printf("%-12s %-12s %-12s %-10s %-10s %-8s %-12s %-20s%n",
                "Mã CC", "Mã NV", "Ngày", "Giờ vào", "Giờ ra", "Giờ làm", "Trạng thái", "Ghi chú");
        System.out.println("=".repeat(120));
        
        for (Attendance att : attendanceList) {
            System.out.printf("%-12s %-12s %-12s %-10s %-10s %-8.1f %-12s %-20s%n",
                    att.getAttendanceId(), att.getEmployeeId(), att.getDate(),
                    att.getCheckInTime(), 
                    att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : "---",
                    att.getHoursWorked(), att.getStatus(), att.getNotes());
        }
    }
    
    public void searchAttendance() {
        System.out.println("\n=== TÌM KIẾM CHẤM CÔNG ===");
        System.out.println("1. Tìm theo mã nhân viên");
        System.out.println("2. Tìm theo ngày");
        System.out.println("3. Tìm theo tháng/năm");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        List<Attendance> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
                results = attendanceList.stream()
                        .filter(a -> a.getEmployeeId().equals(employeeId))
                        .collect(Collectors.toList());
                break;
            case 2:
                LocalDate date = InputUtil.getDate("Nhập ngày cần tìm");
                results = attendanceList.stream()
                        .filter(a -> a.getDate().equals(date))
                        .collect(Collectors.toList());
                break;
            case 3:
                int month = InputUtil.getInt("Nhập tháng (1-12): ");
                int year = InputUtil.getInt("Nhập năm: ");
                results = attendanceList.stream()
                        .filter(a -> a.getDate().getMonthValue() == month && a.getDate().getYear() == year)
                        .collect(Collectors.toList());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy bản ghi chấm công nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            displayAttendanceList(results);
        }
    }
    
    public void updateAttendance() {
        System.out.println("\n=== CẬP NHẬT CHẤM CÔNG ===");
        String attendanceId = InputUtil.getNonEmptyString("Nhập mã chấm công cần cập nhật: ");
        
        Attendance attendance = findAttendanceById(attendanceId);
        if (attendance == null) {
            System.out.println("Không tìm thấy bản ghi chấm công!");
            return;
        }
        
        System.out.println("Thông tin hiện tại:");
        System.out.println(attendance);
        
        System.out.println("\nCập nhật thông tin:");
        
        LocalTime newCheckIn = InputUtil.getTime("Nhập giờ vào mới (hiện tại: " + attendance.getCheckInTime() + ")");
        attendance.setCheckInTime(newCheckIn);
        
        String checkOutInput = InputUtil.getString("Nhập giờ ra mới (hiện tại: " + attendance.getCheckOutTime() + ") - Enter để giữ nguyên: ");
        if (!checkOutInput.isEmpty()) {
            try {
                LocalTime newCheckOut = LocalTime.parse(checkOutInput);
                attendance.setCheckOutTime(newCheckOut);
                attendance.setHoursWorked(calculateHoursWorked(newCheckIn, newCheckOut));
            } catch (Exception e) {
                System.out.println("Giờ ra không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        attendance.setStatus(determineStatus(attendance.getCheckInTime(), attendance.getCheckOutTime()));
        
        String notes = InputUtil.getString("Ghi chú (" + attendance.getNotes() + "): ");
        if (!notes.isEmpty()) attendance.setNotes(notes);
        
        saveToFile();
        System.out.println("Đã cập nhật bản ghi chấm công thành công!");
    }
    
    public void deleteAttendance() {
        System.out.println("\n=== XÓA BẢN GHI CHẤM CÔNG ===");
        String attendanceId = InputUtil.getNonEmptyString("Nhập mã chấm công cần xóa: ");
        
        Attendance attendance = findAttendanceById(attendanceId);
        if (attendance == null) {
            System.out.println("Không tìm thấy bản ghi chấm công!");
            return;
        }
        
        System.out.println("Thông tin bản ghi sẽ bị xóa:");
        System.out.println(attendance);
        
        if (InputUtil.getBoolean("Bạn có chắc chắn muốn xóa bản ghi này?")) {
            attendanceList.remove(attendance);
            saveToFile();
            System.out.println("Đã xóa bản ghi chấm công thành công!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public void generateMonthlyReport() {
        System.out.println("\n=== BÁO CÁO CHẤM CÔNG THÁNG ===");
        String employeeId = InputUtil.getNonEmptyString("Nhập mã nhân viên: ");
        int month = InputUtil.getInt("Nhập tháng (1-12): ");
        int year = InputUtil.getInt("Nhập năm: ");
        
        List<Attendance> monthlyAttendance = attendanceList.stream()
                .filter(a -> a.getEmployeeId().equals(employeeId) && 
                            a.getDate().getMonthValue() == month && 
                            a.getDate().getYear() == year)
                .collect(Collectors.toList());
        
        if (monthlyAttendance.isEmpty()) {
            System.out.println("Không có dữ liệu chấm công trong tháng này!");
            return;
        }
        
        System.out.printf("\nBáo cáo chấm công tháng %d/%d - Nhân viên: %s%n", month, year, employeeId);
        System.out.println("=".repeat(60));
        
        double totalHours = monthlyAttendance.stream()
                .mapToDouble(Attendance::getHoursWorked)
                .sum();
        
        long presentDays = monthlyAttendance.stream()
                .filter(a -> "PRESENT".equals(a.getStatus()) || "LATE".equals(a.getStatus()))
                .count();
        
        long lateDays = monthlyAttendance.stream()
                .filter(a -> "LATE".equals(a.getStatus()))
                .count();
        
        System.out.println("Tổng số ngày làm việc: " + monthlyAttendance.size());
        System.out.println("Số ngày có mặt: " + presentDays);
        System.out.println("Số ngày đi trễ: " + lateDays);
        System.out.println("Tổng giờ làm việc: " + String.format("%.1f", totalHours));
        System.out.println("Giờ làm việc trung bình/ngày: " + String.format("%.1f", totalHours / monthlyAttendance.size()));
        
        System.out.println("\nChi tiết:");
        displayAttendanceList(monthlyAttendance);
    }
    
    private double calculateHoursWorked(LocalTime checkIn, LocalTime checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        return Duration.between(checkIn, checkOut).toMinutes() / 60.0;
    }
    
    private String determineStatus(LocalTime checkIn, LocalTime checkOut) {
        if (checkIn == null) return "ABSENT";
        
        LocalTime standardStart = LocalTime.of(8, 0); // 8:00 AM
        LocalTime standardEnd = LocalTime.of(17, 0);  // 5:00 PM
        
        boolean isLate = checkIn.isAfter(standardStart);
        boolean isEarlyLeave = checkOut != null && checkOut.isBefore(standardEnd);
        
        if (isLate && isEarlyLeave) return "LATE_EARLY_LEAVE";
        if (isLate) return "LATE";
        if (isEarlyLeave) return "EARLY_LEAVE";
        
        return "PRESENT";
    }
    
    public Attendance findAttendanceById(String attendanceId) {
        return attendanceList.stream()
                .filter(att -> att.getAttendanceId().equals(attendanceId))
                .findFirst()
                .orElse(null);
    }
    
    public Attendance findAttendanceByEmployeeAndDate(String employeeId, LocalDate date) {
        return attendanceList.stream()
                .filter(att -> att.getEmployeeId().equals(employeeId) && att.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }
    
    private boolean hasAttendanceForDate(String employeeId, LocalDate date) {
        return findAttendanceByEmployeeAndDate(employeeId, date) != null;
    }
    
    private void displayAttendanceList(List<Attendance> list) {
        System.out.printf("%-12s %-12s %-12s %-10s %-10s %-8s %-12s %-20s%n",
                "Mã CC", "Mã NV", "Ngày", "Giờ vào", "Giờ ra", "Giờ làm", "Trạng thái", "Ghi chú");
        System.out.println("=".repeat(120));
        
        for (Attendance att : list) {
            System.out.printf("%-12s %-12s %-12s %-10s %-10s %-8.1f %-12s %-20s%n",
                    att.getAttendanceId(), att.getEmployeeId(), att.getDate(),
                    att.getCheckInTime(), 
                    att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : "---",
                    att.getHoursWorked(), att.getStatus(), att.getNotes());
        }
    }
    
    public List<Attendance> getAllAttendance() {
        return new ArrayList<>(attendanceList);
    }
}
