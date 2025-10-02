package com.hrms.service;

import com.hrms.model.Project;
import com.hrms.util.InputUtil;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private static final String FILE_PATH = "data/projects.txt";
    private List<Project> projects;
    
    public ProjectService() {
        this.projects = new ArrayList<>();
        loadFromFile();
    }
    
    public void saveToFile() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(FILE_PATH))) {
            for (Project project : projects) {
                bw.write(project.toString());
                bw.newLine();
            }
            System.out.println("Đã lưu dữ liệu dự án vào file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File dự án chưa tồn tại, tạo danh sách mới.");
            return;
        }
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                Project project = Project.fromString(line);
                if (project != null) projects.add(project);
            }
            System.out.println("Đã tải dữ liệu dự án từ file thành công!");
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            projects = new ArrayList<>();
        }
    }
    
    public void addProject() {
        System.out.println("\n=== THÊM DỰ ÁN MỚI ===");
        
        String projectId = InputUtil.getNonEmptyString("Nhập mã dự án: ");
        
        if (findProjectById(projectId) != null) {
            System.out.println("Mã dự án đã tồn tại!");
            return;
        }
        
        String projectName = InputUtil.getNonEmptyString("Nhập tên dự án: ");
        String description = InputUtil.getString("Nhập mô tả dự án: ");
        LocalDate startDate = InputUtil.getDate("Nhập ngày bắt đầu");
        LocalDate endDate = InputUtil.getDate("Nhập ngày kết thúc");
        
        if (endDate.isBefore(startDate)) {
            System.out.println("Ngày kết thúc phải sau ngày bắt đầu!");
            return;
        }
        
        System.out.println("Chọn trạng thái dự án:");
        System.out.println("1. Đang lên kế hoạch (PLANNING)");
        System.out.println("2. Đang thực hiện (IN_PROGRESS)");
        System.out.println("3. Đã hoàn thành (COMPLETED)");
        System.out.println("4. Đã hủy (CANCELLED)");
        int statusChoice = InputUtil.getInt("Lựa chọn: ");
        
        String status;
        switch (statusChoice) {
            case 1: status = "PLANNING"; break;
            case 2: status = "IN_PROGRESS"; break;
            case 3: status = "COMPLETED"; break;
            case 4: status = "CANCELLED"; break;
            default: status = "PLANNING"; break;
        }
        
        String managerId = InputUtil.getString("Nhập mã quản lý dự án: ");
        double budget = InputUtil.getDouble("Nhập ngân sách dự án: ");
        
        System.out.println("Chọn mức độ ưu tiên:");
        System.out.println("1. Cao (HIGH)");
        System.out.println("2. Trung bình (MEDIUM)");
        System.out.println("3. Thấp (LOW)");
        int priorityChoice = InputUtil.getInt("Lựa chọn: ");
        
        String priority;
        switch (priorityChoice) {
            case 1: priority = "HIGH"; break;
            case 2: priority = "MEDIUM"; break;
            case 3: priority = "LOW"; break;
            default: priority = "MEDIUM"; break;
        }
        
        Project project = new Project(projectId, projectName, description, startDate,
                                    endDate, status, managerId, budget, priority);
        projects.add(project);
        saveToFile();
        
        System.out.println("Đã thêm dự án thành công!");
    }
    
    public void displayAllProjects() {
        System.out.println("\n=== DANH SÁCH DỰ ÁN ===");
        
        if (projects.isEmpty()) {
            System.out.println("Chưa có dự án nào!");
            return;
        }
        
        System.out.printf("%-10s %-25s %-30s %-12s %-12s %-15s %-10s %-12s %-10s%n",
                "Mã DA", "Tên dự án", "Mô tả", "Ngày BĐ", "Ngày KT", "Trạng thái", "Quản lý", "Ngân sách", "Ưu tiên");
        System.out.println("=".repeat(150));
        
        for (Project project : projects) {
            System.out.printf("%-10s %-25s %-30s %-12s %-12s %-15s %-10s %-12.0f %-10s%n",
                    project.getProjectId(), project.getProjectName(),
                    project.getDescription(), project.getStartDate(),
                    project.getEndDate(), project.getStatus(),
                    project.getManagerId(), project.getBudget(),
                    project.getPriority());
        }
    }
    
    public void searchProject() {
        System.out.println("\n=== TÌM KIẾM DỰ ÁN ===");
        System.out.println("1. Tìm theo mã dự án");
        System.out.println("2. Tìm theo tên dự án");
        System.out.println("3. Tìm theo trạng thái");
        System.out.println("4. Tìm theo mức độ ưu tiên");
        System.out.println("5. Tìm theo quản lý dự án");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        List<Project> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String id = InputUtil.getNonEmptyString("Nhập mã dự án: ");
                Project project = findProjectById(id);
                if (project != null) results.add(project);
                break;
            case 2:
                String name = InputUtil.getNonEmptyString("Nhập tên dự án: ");
                results = projects.stream()
                        .filter(p -> p.getProjectName().toLowerCase().contains(name.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            case 3:
                System.out.println("Chọn trạng thái:");
                System.out.println("1. PLANNING");
                System.out.println("2. IN_PROGRESS");
                System.out.println("3. COMPLETED");
                System.out.println("4. CANCELLED");
                int statusChoice = InputUtil.getInt("Lựa chọn: ");
                String status = "";
                switch (statusChoice) {
                    case 1: status = "PLANNING"; break;
                    case 2: status = "IN_PROGRESS"; break;
                    case 3: status = "COMPLETED"; break;
                    case 4: status = "CANCELLED"; break;
                }
                if (!status.isEmpty()) {
                    final String finalStatus = status;
                    results = projects.stream()
                            .filter(p -> p.getStatus().equals(finalStatus))
                            .collect(Collectors.toList());
                }
                break;
            case 4:
                System.out.println("Chọn mức độ ưu tiên:");
                System.out.println("1. HIGH");
                System.out.println("2. MEDIUM");
                System.out.println("3. LOW");
                int priorityChoice = InputUtil.getInt("Lựa chọn: ");
                String priority = "";
                switch (priorityChoice) {
                    case 1: priority = "HIGH"; break;
                    case 2: priority = "MEDIUM"; break;
                    case 3: priority = "LOW"; break;
                }
                if (!priority.isEmpty()) {
                    final String finalPriority = priority;
                    results = projects.stream()
                            .filter(p -> p.getPriority().equals(finalPriority))
                            .collect(Collectors.toList());
                }
                break;
            case 5:
                String managerId = InputUtil.getNonEmptyString("Nhập mã quản lý: ");
                results = projects.stream()
                        .filter(p -> p.getManagerId().equals(managerId))
                        .collect(Collectors.toList());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy dự án nào!");
        } else {
            System.out.println("Kết quả tìm kiếm:");
            displayProjectList(results);
        }
    }
    
    public void updateProject() {
        System.out.println("\n=== CẬP NHẬT DỰ ÁN ===");
        String projectId = InputUtil.getNonEmptyString("Nhập mã dự án cần cập nhật: ");
        
        Project project = findProjectById(projectId);
        if (project == null) {
            System.out.println("Không tìm thấy dự án!");
            return;
        }
        
        System.out.println("Thông tin hiện tại:");
        System.out.println(project);
        
        System.out.println("\nNhập thông tin mới (Enter để giữ nguyên):");
        
        String name = InputUtil.getString("Tên dự án (" + project.getProjectName() + "): ");
        if (!name.isEmpty()) project.setProjectName(name);
        
        String description = InputUtil.getString("Mô tả (" + project.getDescription() + "): ");
        if (!description.isEmpty()) project.setDescription(description);
        
        System.out.println("Cập nhật trạng thái:");
        System.out.println("1. PLANNING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        System.out.println("0. Giữ nguyên (" + project.getStatus() + ")");
        int statusChoice = InputUtil.getInt("Lựa chọn: ");
        
        switch (statusChoice) {
            case 1: project.setStatus("PLANNING"); break;
            case 2: project.setStatus("IN_PROGRESS"); break;
            case 3: project.setStatus("COMPLETED"); break;
            case 4: project.setStatus("CANCELLED"); break;
        }
        
        String managerId = InputUtil.getString("Mã quản lý (" + project.getManagerId() + "): ");
        if (!managerId.isEmpty()) project.setManagerId(managerId);
        
        String budget = InputUtil.getString("Ngân sách (" + project.getBudget() + "): ");
        if (!budget.isEmpty()) {
            try {
                project.setBudget(Double.parseDouble(budget));
            } catch (NumberFormatException e) {
                System.out.println("Ngân sách không hợp lệ, giữ nguyên giá trị cũ.");
            }
        }
        
        System.out.println("Cập nhật mức độ ưu tiên:");
        System.out.println("1. HIGH");
        System.out.println("2. MEDIUM");
        System.out.println("3. LOW");
        System.out.println("0. Giữ nguyên (" + project.getPriority() + ")");
        int priorityChoice = InputUtil.getInt("Lựa chọn: ");
        
        switch (priorityChoice) {
            case 1: project.setPriority("HIGH"); break;
            case 2: project.setPriority("MEDIUM"); break;
            case 3: project.setPriority("LOW"); break;
        }
        
        saveToFile();
        System.out.println("Đã cập nhật dự án thành công!");
    }
    
    public void deleteProject() {
        System.out.println("\n=== XÓA DỰ ÁN ===");
        String projectId = InputUtil.getNonEmptyString("Nhập mã dự án cần xóa: ");
        
        Project project = findProjectById(projectId);
        if (project == null) {
            System.out.println("Không tìm thấy dự án!");
            return;
        }
        
        System.out.println("Thông tin dự án sẽ bị xóa:");
        System.out.println(project);
        
        if (InputUtil.getBoolean("Bạn có chắc chắn muốn xóa dự án này?")) {
            projects.remove(project);
            saveToFile();
            System.out.println("Đã xóa dự án thành công!");
        } else {
            System.out.println("Hủy thao tác xóa.");
        }
    }
    
    public void generateProjectReport() {
        System.out.println("\n=== BÁO CÁO DỰ ÁN ===");
        System.out.println("1. Báo cáo tổng quan");
        System.out.println("2. Báo cáo theo trạng thái");
        System.out.println("3. Báo cáo theo mức độ ưu tiên");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        
        switch (choice) {
            case 1:
                generateOverviewReport();
                break;
            case 2:
                generateStatusReport();
                break;
            case 3:
                generatePriorityReport();
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
        }
    }
    
    private void generateOverviewReport() {
        System.out.println("\n=== BÁO CÁO TỔNG QUAN DỰ ÁN ===");
        
        if (projects.isEmpty()) {
            System.out.println("Không có dự án nào để báo cáo!");
            return;
        }
        
        long totalProjects = projects.size();
        long planningProjects = projects.stream().filter(p -> "PLANNING".equals(p.getStatus())).count();
        long inProgressProjects = projects.stream().filter(p -> "IN_PROGRESS".equals(p.getStatus())).count();
        long completedProjects = projects.stream().filter(p -> "COMPLETED".equals(p.getStatus())).count();
        long cancelledProjects = projects.stream().filter(p -> "CANCELLED".equals(p.getStatus())).count();
        
        double totalBudget = projects.stream().mapToDouble(Project::getBudget).sum();
        double averageBudget = totalBudget / totalProjects;
        
        long highPriorityProjects = projects.stream().filter(p -> "HIGH".equals(p.getPriority())).count();
        long mediumPriorityProjects = projects.stream().filter(p -> "MEDIUM".equals(p.getPriority())).count();
        long lowPriorityProjects = projects.stream().filter(p -> "LOW".equals(p.getPriority())).count();
        
        System.out.println("=".repeat(60));
        System.out.println("TỔNG SỐ DỰ ÁN: " + totalProjects);
        System.out.println();
        System.out.println("PHÂN LOẠI THEO TRẠNG THÁI:");
        System.out.println("- Đang lên kế hoạch: " + planningProjects);
        System.out.println("- Đang thực hiện: " + inProgressProjects);
        System.out.println("- Đã hoàn thành: " + completedProjects);
        System.out.println("- Đã hủy: " + cancelledProjects);
        System.out.println();
        System.out.println("PHÂN LOẠI THEO ƯU TIÊN:");
        System.out.println("- Cao: " + highPriorityProjects);
        System.out.println("- Trung bình: " + mediumPriorityProjects);
        System.out.println("- Thấp: " + lowPriorityProjects);
        System.out.println();
        System.out.println("THÔNG TIN NGÂN SÁCH:");
        System.out.println("- Tổng ngân sách: " + String.format("%.0f", totalBudget) + " VND");
        System.out.println("- Ngân sách trung bình: " + String.format("%.0f", averageBudget) + " VND");
        System.out.println("=".repeat(60));
    }
    
    private void generateStatusReport() {
        System.out.println("Chọn trạng thái cần báo cáo:");
        System.out.println("1. PLANNING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        String status = "";
        
        switch (choice) {
            case 1: status = "PLANNING"; break;
            case 2: status = "IN_PROGRESS"; break;
            case 3: status = "COMPLETED"; break;
            case 4: status = "CANCELLED"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
    final String statusFinal = status;
    List<Project> statusProjects = projects.stream()
        .filter(p -> p.getStatus().equals(statusFinal))
        .collect(Collectors.toList());
        
        System.out.printf("\n=== BÁO CÁO DỰ ÁN TRẠNG THÁI: %s ===\n", status);
        
        if (statusProjects.isEmpty()) {
            System.out.println("Không có dự án nào với trạng thái này!");
            return;
        }
        
        System.out.println("Số lượng dự án: " + statusProjects.size());
        double totalBudget = statusProjects.stream().mapToDouble(Project::getBudget).sum();
        System.out.println("Tổng ngân sách: " + String.format("%.0f", totalBudget) + " VND");
        
        System.out.println("\nDanh sách chi tiết:");
        displayProjectList(statusProjects);
    }
    
    private void generatePriorityReport() {
        System.out.println("Chọn mức độ ưu tiên cần báo cáo:");
        System.out.println("1. HIGH");
        System.out.println("2. MEDIUM");
        System.out.println("3. LOW");
        
        int choice = InputUtil.getInt("Lựa chọn: ");
        String priority = "";
        
        switch (choice) {
            case 1: priority = "HIGH"; break;
            case 2: priority = "MEDIUM"; break;
            case 3: priority = "LOW"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        
    final String priorityFinal = priority;
    List<Project> priorityProjects = projects.stream()
        .filter(p -> p.getPriority().equals(priorityFinal))
        .collect(Collectors.toList());
        
        System.out.printf("\n=== BÁO CÁO DỰ ÁN ƯU TIÊN: %s ===\n", priority);
        
        if (priorityProjects.isEmpty()) {
            System.out.println("Không có dự án nào với mức độ ưu tiên này!");
            return;
        }
        
        System.out.println("Số lượng dự án: " + priorityProjects.size());
        double totalBudget = priorityProjects.stream().mapToDouble(Project::getBudget).sum();
        System.out.println("Tổng ngân sách: " + String.format("%.0f", totalBudget) + " VND");
        
        System.out.println("\nDanh sách chi tiết:");
        displayProjectList(priorityProjects);
    }
    
    public Project findProjectById(String projectId) {
        return projects.stream()
                .filter(project -> project.getProjectId().equals(projectId))
                .findFirst()
                .orElse(null);
    }
    
    private void displayProjectList(List<Project> projectList) {
        System.out.printf("%-10s %-25s %-30s %-12s %-12s %-15s %-10s %-12s %-10s%n",
                "Mã DA", "Tên dự án", "Mô tả", "Ngày BĐ", "Ngày KT", "Trạng thái", "Quản lý", "Ngân sách", "Ưu tiên");
        System.out.println("=".repeat(150));
        
        for (Project project : projectList) {
            System.out.printf("%-10s %-25s %-30s %-12s %-12s %-15s %-10s %-12.0f %-10s%n",
                    project.getProjectId(), project.getProjectName(),
                    project.getDescription(), project.getStartDate(),
                    project.getEndDate(), project.getStatus(),
                    project.getManagerId(), project.getBudget(),
                    project.getPriority());
        }
    }
    
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }
}
