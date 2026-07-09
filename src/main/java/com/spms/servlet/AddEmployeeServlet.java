package com.spms.servlet;

import com.spms.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * AddEmployeeServlet
 * POST /AddEmployeeServlet
 * Params: emp_id, name, email, phone, role, department, salary
 * Response: plain text success or error message
 */
@WebServlet("/AddEmployeeServlet")
public class AddEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String empIdStr    = request.getParameter("emp_id");
        String name        = request.getParameter("name");
        String email       = request.getParameter("email");
        String phone       = request.getParameter("phone");
        String role        = request.getParameter("role");
        String department  = request.getParameter("department");
        String salaryStr   = request.getParameter("salary");

        // Basic validation
        if (empIdStr == null || empIdStr.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            out.print("Error: Required fields are missing.");
            return;
        }

        int    empId;
        double salary;
        try {
            empId  = Integer.parseInt(empIdStr.trim());
            salary = Double.parseDouble(salaryStr == null ? "0" : salaryStr.trim());
        } catch (NumberFormatException e) {
            out.print("Error: Invalid numeric value for Employee ID or Salary.");
            return;
        }

        String sql = "INSERT INTO employee (emp_id, name, email, phone, role, department, salary) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, name.trim());
            ps.setString(3, email.trim());
            ps.setString(4, phone == null ? "" : phone.trim());
            ps.setString(5, role == null ? "" : role.trim());
            ps.setString(6, department == null ? "" : department.trim());
            ps.setDouble(7, salary);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.print("Employee Added Successfully! (ID: " + empId + ")");
            } else {
                out.print("Error: Employee could not be added.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                out.print("Error: Employee ID " + empId + " already exists.");
            } else {
                out.print("Error: " + e.getMessage());
            }
        }
    }
}
