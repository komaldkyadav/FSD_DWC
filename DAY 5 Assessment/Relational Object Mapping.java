// Employee.java
public class Employee {
    private long empId;
    private String firstName;
    private String lastName;
    private String email;
    private long deptId;
    
    public Employee(long empId, String firstName, String lastName, String email, long deptId) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.deptId = deptId;
    }
    
    public long getEmpId() { return empId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public long getDeptId() { return deptId; }
    
    @Override
    public String toString() {
        return "Employee{" + empId + ": " + firstName + " " + lastName + ", " + email + "}";
    }
}

// Department.java
import java.util.ArrayList;
import java.util.List;

public class Department {
    private long deptId;
    private String deptName;
    private List<Employee> employees;
    
    public Department(long deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.employees = new ArrayList<>();
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
    
    public long getDeptId() { return deptId; }
    public String getDeptName() { return deptName; }
    public List<Employee> getEmployees() { return employees; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Department: ").append(deptName).append(" (ID: ").append(deptId).append(")\n");
        for (Employee emp : employees) {
            sb.append("  - ").append(emp).append("\n");
        }
        return sb.toString();
    }
}

// DepartmentMapper.java
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DepartmentMapper {
    private static final String URL = "jdbc:postgresql://localhost:5432/company_db";
    private static final String USER = "company_admin";
    private static final String PASSWORD = "company_pass";
    
    private static final String QUERY = 
        "SELECT d.dept_id, d.dept_name, " +
        "       e.emp_id, e.first_name, e.last_name, e.email " +
        "FROM departments d " +
        "INNER JOIN employees e ON d.dept_id = e.dept_id " +
        "ORDER BY d.dept_id";
    
    public Map<Long, Department> fetchDepartmentsWithEmployees() {
        Map<Long, Department> deptMap = new HashMap<>();
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(QUERY);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("=== Departments and Employees ===");
            
            while (rs.next()) {
                long deptId = rs.getLong("dept_id");
                String deptName = rs.getString("dept_name");
                long empId = rs.getLong("emp_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                // Check if department already exists in map
                Department dept = deptMap.get(deptId);
                if (dept == null) {
                    dept = new Department(deptId, deptName);
                    deptMap.put(deptId, dept);
                }
                
                // Add employee to department
                Employee emp = new Employee(empId, firstName, lastName, email, deptId);
                dept.addEmployee(emp);
            }
            
            System.out.println("Successfully mapped " + deptMap.size() + " departments");
            
        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
        
        return deptMap;
    }
}

// CompanyDashboard.java
public class CompanyDashboard {
    public static void main(String[] args) {
        DepartmentMapper mapper = new DepartmentMapper();
        Map<Long, Department> departments = mapper.fetchDepartmentsWithEmployees();
        
        System.out.println("\n=== Organizational Structure ===");
        for (Department dept : departments.values()) {
            System.out.println(dept);
        }
        
        System.out.println("\n=== Query Optimization Analysis ===");
        System.out.println("Single INNER JOIN query");
        System.out.println("Network round-trips: 1 (instead of N+1)");
        System.out.println("Data fetched in single ResultSet");
        System.out.println("HashMap used to reconstruct hierarchical structure");
        System.out.println("Duplicated department data collapsed in memory");
    }
}