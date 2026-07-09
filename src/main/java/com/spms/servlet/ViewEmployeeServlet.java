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
 * ViewEmployeeServlet
 *
 * GET /ViewEmployeeServlet          → returns HTML table of all employees
 * GET /ViewEmployeeServlet?emp_id=X → returns pipe-delimited single employee row
 *                                      (name|email|phone|role|department|salary)
 *                                      used by updateEmployee.html to pre-fill the form
 */
@WebServlet("/ViewEmployeeServlet")
public class ViewEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String empIdParam = request.getParameter("emp_id");

        // ---- Single employee lookup (for update form pre-fill) ----
        if (empIdParam != null && !empIdParam.trim().isEmpty()) {
            response.setContentType("text/plain");
            int empId;
            try {
                empId = Integer.parseInt(empIdParam.trim());
            } catch (NumberFormatException e) {
                out.print("ERROR: Invalid ID");
                return;
            }

            String sql = "SELECT name, email, phone, role, department, salary FROM employee WHERE emp_id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, empId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out.print(rs.getString("name")       + "|"
                                + rs.getString("email")      + "|"
                                + rs.getString("phone")      + "|"
                                + rs.getString("role")       + "|"
                                + rs.getString("department") + "|"
                                + rs.getDouble("salary"));
                    } else {
                        out.print("ERROR: Not Found");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print("ERROR: " + e.getMessage());
            }
            return;
        }

        // ---- All employees → HTML table ----
        response.setContentType("text/html");
        String sql = "SELECT emp_id, name, email, phone, role, department, salary FROM employee ORDER BY emp_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            StringBuilder sb = new StringBuilder();
            sb.append("<table>")
              .append("<tr>")
              .append("<th>Emp ID</th><th>Name</th><th>Email</th>")
              .append("<th>Phone</th><th>Role</th><th>Department</th><th>Salary</th>")
              .append("</tr>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                sb.append("<tr>")
                  .append("<td>").append(rs.getInt("emp_id")).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("name"))).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("email"))).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("phone"))).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("role"))).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("department"))).append("</td>")
                  .append("<td>").append(rs.getDouble("salary")).append("</td>")
                  .append("</tr>");
            }

            if (!hasData) {
                sb.append("<tr><td colspan='7'>No employees found.</td></tr>");
            }
            sb.append("</table>");
            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p style='color:red;'>Error loading employees: " + escHtml(e.getMessage()) + "</p>");
        }
    }

    /** Minimal HTML escaping to prevent XSS in table output */
    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
