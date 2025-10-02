package com.hrms;

import com.hrms.service.EmployeeService;
import com.hrms.service.DepartmentService;
import com.hrms.service.ProjectService;
import com.hrms.service.PayrollService;
import com.hrms.service.AttendanceService;
import com.hrms.util.InputUtil;

public class HRMSApplication {
	public static void main(String[] args) {
		EmployeeService employeeService = new EmployeeService();
		DepartmentService departmentService = new DepartmentService();
		ProjectService projectService = new ProjectService();
		PayrollService payrollService = new PayrollService();
		AttendanceService attendanceService = new AttendanceService();

		while (true) {
			System.out.println("\n===== HỆ THỐNG QUẢN LÝ NHÂN SỰ =====");
			System.out.println("1. Quản lý nhân viên");
			System.out.println("2. Quản lý dự án");
			System.out.println("3. Quản lý phòng ban");
			System.out.println("4. Quản lý lương thưởng");
			System.out.println("5. Quản lý chấm công");
			System.out.println("0. Thoát");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1:
					employeeMenu(employeeService);
					break;
				case 2:
					projectMenu(projectService);
					break;
				case 3:
					departmentMenu(departmentService);
					break;
				case 4:
					payrollMenu(payrollService);
					break;
				case 5:
					attendanceMenu(attendanceService);
					break;
				case 0:
					System.out.println("Thoát chương trình.");
					return;
				default:
					System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void employeeMenu(EmployeeService service) {
		while (true) {
			System.out.println("\n--- Quản lý nhân viên ---");
			System.out.println("1. Thêm nhân viên");
			System.out.println("2. Hiển thị danh sách nhân viên");
			System.out.println("3. Tìm kiếm nhân viên");
			System.out.println("4. Cập nhật nhân viên");
			System.out.println("5. Xóa nhân viên");
			System.out.println("0. Quay lại");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1: service.addEmployee(); break;
				case 2: service.displayAllEmployees(); break;
				case 3: service.searchEmployee(); break;
				case 4: service.updateEmployee(); break;
				case 5: service.deleteEmployee(); break;
				case 0: return;
				default: System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void departmentMenu(DepartmentService service) {
		while (true) {
			System.out.println("\n--- Quản lý phòng ban ---");
			System.out.println("1. Thêm phòng ban");
			System.out.println("2. Hiển thị danh sách phòng ban");
			System.out.println("3. Tìm kiếm phòng ban");
			System.out.println("4. Cập nhật phòng ban");
			System.out.println("5. Xóa phòng ban");
			System.out.println("0. Quay lại");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1: service.addDepartment(); break;
				case 2: service.displayAllDepartments(); break;
				case 3: service.searchDepartment(); break;
				case 4: service.updateDepartment(); break;
				case 5: service.deleteDepartment(); break;
				case 0: return;
				default: System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void projectMenu(ProjectService service) {
		while (true) {
			System.out.println("\n--- Quản lý dự án ---");
			System.out.println("1. Thêm dự án");
			System.out.println("2. Hiển thị danh sách dự án");
			System.out.println("3. Tìm kiếm dự án");
			System.out.println("4. Cập nhật dự án");
			System.out.println("5. Xóa dự án");
			System.out.println("0. Quay lại");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1: service.addProject(); break;
				case 2: service.displayAllProjects(); break;
				case 3: service.searchProject(); break;
				case 4: service.updateProject(); break;
				case 5: service.deleteProject(); break;
				case 0: return;
				default: System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void payrollMenu(PayrollService service) {
		while (true) {
			System.out.println("\n--- Quản lý lương thưởng ---");
			System.out.println("1. Thêm bản ghi lương thưởng");
			System.out.println("2. Hiển thị danh sách lương thưởng");
			System.out.println("3. Tìm kiếm lương thưởng");
			System.out.println("4. Cập nhật lương thưởng");
			System.out.println("5. Xóa bản ghi lương thưởng");
			System.out.println("6. Báo cáo lương thưởng");
			System.out.println("0. Quay lại");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1: service.addPayroll(); break;
				case 2: service.displayAllPayroll(); break;
				case 3: service.searchPayroll(); break;
				case 4: service.updatePayroll(); break;
				case 5: service.deletePayroll(); break;
				case 6: service.generateSalaryReport(); break;
				case 0: return;
				default: System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}

	private static void attendanceMenu(AttendanceService service) {
		while (true) {
			System.out.println("\n--- Quản lý chấm công ---");
			System.out.println("1. Thêm bản ghi chấm công");
			System.out.println("2. Hiển thị danh sách chấm công");
			System.out.println("3. Tìm kiếm chấm công");
			System.out.println("4. Cập nhật chấm công");
			System.out.println("5. Xóa bản ghi chấm công");
			System.out.println("6. Báo cáo chấm công tháng");
			System.out.println("0. Quay lại");
			int choice = InputUtil.getInt("Chọn chức năng: ");
			switch (choice) {
				case 1: service.addAttendance(); break;
				case 2: service.displayAllAttendance(); break;
				case 3: service.searchAttendance(); break;
				case 4: service.updateAttendance(); break;
				case 5: service.deleteAttendance(); break;
				case 6: service.generateMonthlyReport(); break;
				case 0: return;
				default: System.out.println("Lựa chọn không hợp lệ!");
			}
		}
	}
}
