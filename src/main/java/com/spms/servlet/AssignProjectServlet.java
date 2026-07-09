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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * AssignProjectServlet
 *
 * POST /AssignProjectServlet  → assign employee to project
 *   Params: emp_id, project_id, role
 *   Response: plain text success or error
 *
 * GET  /AssignProjectServlet  → return HTML table of all assignments
 */
@WebServlet("/AssignProjectServlet")
public class AssignProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    // ---- POST: create assignment ----
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String empIdStr    = request.getParameter("emp_id");
        String projectIdStr = request.getParameter("project_id");
        String role        = request.getParameter("role");

        if (empIdStr == null || empIdStr.trim().isEmpty() ||
            projectIdStr == null || projectIdStr.trim().isEmpty()) {
            out.print("Error: Employee ID and Project ID are required.");
            return;
        }

        int empId, projectId;
        try {
            empId     = Integer.parseInt(empIdStr.trim());
            projectId = Integer.parseInt(projectIdStr.trim());
        } catch (NumberFormatException e) {
            out.print("Error: Invalid numeric value.");
            return;
        }

        // Verify employee exists
        if (!recordExists("SELECT 1 FROM employee WHERE emp_id=?", empId, out)) return;
        // Verify project exists
        if (!recordExists("SELECT 1 FROM project WHERE project_id=?", projectId, out)) return;

        String sql = "INSERT INTO assignment (emp_id, project_id, role) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, projectId);
            ps.setString(3, role == null ? "" : role.trim());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.print("Employee Assigned to Project Successfully!");
            } else {
                out.print("Error: Assignment could not be saved.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error: " + e.getMessage());
        }
    }

    // ---- GET: view all assignments ----
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String sql = "SELECT a.assign_id, e.emp_id, e.name AS emp_name, "
                   + "p.project_id, p.project_name, a.role "
                   + "FROM assignment a "
                   + "JOIN employee e ON a.emp_id = e.emp_id "
                   + "JOIN project p ON a.project_id = p.project_id "
                   + "ORDER BY a.assign_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            StringBuilder sb = new StringBuilder();
            sb.append("<table>")
              .append("<tr>")
              .append("<th>Assign ID</th><th>Emp ID</th><th>Employee Name</th>")
              .append("<th>Project ID</th><th>Project Name</th><th>Role</th>")
              .append("</tr>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                sb.append("<tr>")
                  .append("<td>").append(rs.getInt("assign_id")).append("</td>")
                  .append("<td>").append(rs.getInt("emp_id")).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("emp_name"))).append("</td>")
                  .append("<td>").append(rs.getInt("project_id")).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("project_name"))).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("role"))).append("</td>")
                  .append("</tr>");
            }

            if (!hasData) {
                sb.append("<tr><td colspan='6'>No assignments found.</td></tr>");
            }
            sb.append("</table>");
            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p style='color:red;'>Error loading assignments: " + escHtml(e.getMessage()) + "</p>");
        }
    }

    /** Returns true if the record exists, false and writes error message otherwise */
    private boolean recordExists(String sql, int id, PrintWriter out) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    out.print("Error: No record found with ID " + id + ".");
                    return false;
                }
            }
        } catch (Exception e) {
            out.print("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
