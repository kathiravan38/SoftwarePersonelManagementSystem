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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * ViewProjectServlet
 * GET /ViewProjectServlet → returns an HTML table of all projects
 */
@WebServlet("/ViewProjectServlet")
public class ViewProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String sql = "SELECT project_id, project_name, start_date, end_date, manager FROM project ORDER BY project_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            StringBuilder sb = new StringBuilder();
            sb.append("<table>")
              .append("<tr>")
              .append("<th>Project ID</th><th>Project Name</th>")
              .append("<th>Start Date</th><th>End Date</th><th>Manager</th>")
              .append("</tr>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                sb.append("<tr>")
                  .append("<td>").append(rs.getInt("project_id")).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("project_name"))).append("</td>")
                  .append("<td>").append(rs.getDate("start_date")).append("</td>")
                  .append("<td>").append(rs.getDate("end_date")).append("</td>")
                  .append("<td>").append(escHtml(rs.getString("manager"))).append("</td>")
                  .append("</tr>");
            }

            if (!hasData) {
                sb.append("<tr><td colspan='5'>No projects found.</td></tr>");
            }
            sb.append("</table>");
            out.print(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p style='color:red;'>Error loading projects: " + escHtml(e.getMessage()) + "</p>");
        }
    }

    private String escHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
