package com.hrms;

import com.hrms.model.Payroll;
import com.hrms.model.Attendance;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InitData {
    public static void main(String[] args) {
        try {
            List<Payroll> payrollList = new ArrayList<>();
            payrollList.add(new Payroll("P001", "E001", LocalDate.of(2023,1,1), LocalDate.of(2023,1,31), 5000000, 500000, 200000, 100000, 5600000, "PAID", "Thưởng tháng 1"));
            payrollList.add(new Payroll("P002", "E002", LocalDate.of(2023,2,1), LocalDate.of(2023,2,28), 4800000, 400000, 150000, 120000, 5230000, "PAID", "Thưởng tháng 2"));
            payrollList.add(new Payroll("P003", "E003", LocalDate.of(2023,3,1), LocalDate.of(2023,3,31), 7000000, 300000, 100000, 80000, 7220000, "PENDING", ""));
            payrollList.add(new Payroll("P004", "E004", LocalDate.of(2023,4,1), LocalDate.of(2023,4,30), 5100000, 350000, 120000, 90000, 5380000, "PAID", ""));
            payrollList.add(new Payroll("P005", "E005", LocalDate.of(2023,5,1), LocalDate.of(2023,5,31), 4900000, 450000, 180000, 110000, 5270000, "PAID", ""));
            payrollList.add(new Payroll("P006", "E006", LocalDate.of(2023,6,1), LocalDate.of(2023,6,30), 5200000, 320000, 90000, 70000, 5350000, "PAID", ""));
            payrollList.add(new Payroll("P007", "E007", LocalDate.of(2023,7,1), LocalDate.of(2023,7,31), 4800000, 370000, 110000, 95000, 5225000, "PAID", ""));
            payrollList.add(new Payroll("P008", "E008", LocalDate.of(2023,8,1), LocalDate.of(2023,8,31), 5000000, 390000, 130000, 105000, 5385000, "PAID", ""));
            payrollList.add(new Payroll("P009", "E009", LocalDate.of(2023,9,1), LocalDate.of(2023,9,30), 4950000, 410000, 140000, 115000, 5385000, "PAID", ""));
            payrollList.add(new Payroll("P010", "E010", LocalDate.of(2023,10,1), LocalDate.of(2023,10,31), 5300000, 430000, 160000, 125000, 5595000, "PAID", ""));
            ObjectOutputStream payrollOut = new ObjectOutputStream(new FileOutputStream("data/payroll.txt"));
            payrollOut.writeObject(payrollList);
            payrollOut.close();

            List<Attendance> attendanceList = new ArrayList<>();
            attendanceList.add(new Attendance("A001", "E001", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(17,0), 8.0, "PRESENT", ""));
            attendanceList.add(new Attendance("A002", "E002", LocalDate.of(2023,1,2), LocalTime.of(8,10), LocalTime.of(17,0), 7.8, "LATE", ""));
            attendanceList.add(new Attendance("A003", "E003", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(16,50), 7.8, "EARLY_LEAVE", ""));
            attendanceList.add(new Attendance("A004", "E004", LocalDate.of(2023,1,2), LocalTime.of(8,5), LocalTime.of(17,0), 7.9, "LATE", ""));
            attendanceList.add(new Attendance("A005", "E005", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(17,0), 8.0, "PRESENT", ""));
            attendanceList.add(new Attendance("A006", "E006", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(17,0), 8.0, "PRESENT", ""));
            attendanceList.add(new Attendance("A007", "E007", LocalDate.of(2023,1,2), LocalTime.of(8,15), LocalTime.of(17,0), 7.7, "LATE", ""));
            attendanceList.add(new Attendance("A008", "E008", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(16,45), 7.75, "EARLY_LEAVE", ""));
            attendanceList.add(new Attendance("A009", "E009", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(17,0), 8.0, "PRESENT", ""));
            attendanceList.add(new Attendance("A010", "E010", LocalDate.of(2023,1,2), LocalTime.of(8,0), LocalTime.of(17,0), 8.0, "PRESENT", ""));
            ObjectOutputStream attendanceOut = new ObjectOutputStream(new FileOutputStream("data/attendance.txt"));
            attendanceOut.writeObject(attendanceList);
            attendanceOut.close();

            System.out.println("Đã tạo dữ liệu mẫu cho payroll.txt và attendance.txt!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
