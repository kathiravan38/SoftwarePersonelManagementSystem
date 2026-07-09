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
 * UpdateEmployeeServlet
 * POST /UpdateEmployeeServlet
 * Params: emp_id, name, email, phone, role, department, salary
 * Response: plain text success or error message
 */
@WebServlet("/UpdateEmployeeServlet")
public class UpdateEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String empIdStr   = request.getParameter("emp_id");
        String name       = request.getParameter("name");
        String email      = request.getParameter("email");
        String phone      = request.getParameter("phone");
        String role       = request.getParameter("role");
        String department = request.getParameter("department");
        String salaryStr  = request.getParameter("salary");

        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            out.print("Error: Employee ID is required.");
            return;
        }

        int    empId;
        double salary;
        try {
            empId  = Integer.parseInt(empIdStr.trim());
            salary = Double.parseDouble(salaryStr == null ? "0" : salaryStr.trim());
        } catch (NumberFormatException e) {
            out.print("Error: Invalid numeric value.");
            return;
        }

        String sql = "UPDATE employee SET name=?, email=?, phone=?, role=?, department=?, salary=? "
                   + "WHERE emp_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name == null ? "" : name.trim());
            ps.setString(2, email == null ? "" : email.trim());
            ps.setString(3, phone == null ? "" : phone.trim());
            ps.setString(4, role == null ? "" : role.trim());
            ps.setString(5, department == null ? "" : department.trim());
            ps.setDouble(6, salary);
            ps.setInt(7, empId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.print("Employee Updated Successfully! (ID: " + empId + ")");
            } else {
                out.print("Error: No employee found with ID " + empId + ".");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error: " + e.getMessage());
        }
    }
}
