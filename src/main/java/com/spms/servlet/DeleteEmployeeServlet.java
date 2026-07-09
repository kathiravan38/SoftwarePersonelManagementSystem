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
 * DeleteEmployeeServlet
 * POST /DeleteEmployeeServlet
 * Params: emp_id
 * Response: plain text success or error message
 * Note: Deleting an employee will also cascade-delete their assignments (FK ON DELETE CASCADE).
 */
@WebServlet("/DeleteEmployeeServlet")
public class DeleteEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String empIdStr = request.getParameter("emp_id");

        if (empIdStr == null || empIdStr.trim().isEmpty()) {
            out.print("Error: Employee ID is required.");
            return;
        }

        int empId;
        try {
            empId = Integer.parseInt(empIdStr.trim());
        } catch (NumberFormatException e) {
            out.print("Error: Invalid Employee ID.");
            return;
        }

        String sql = "DELETE FROM employee WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.print("Employee Deleted Successfully! (ID: " + empId + ")");
            } else {
                out.print("Error: No employee found with ID " + empId + ".");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error: " + e.getMessage());
        }
    }
}
