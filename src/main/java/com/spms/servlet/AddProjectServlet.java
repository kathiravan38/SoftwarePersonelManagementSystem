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
import java.sql.Date;
import java.sql.PreparedStatement;

/**
 * AddProjectServlet
 * POST /AddProjectServlet
 * Params: project_id, project_name, start_date (yyyy-MM-dd), end_date (yyyy-MM-dd), manager
 * Response: plain text success or error message
 */
@WebServlet("/AddProjectServlet")
public class AddProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String projectIdStr  = request.getParameter("project_id");
        String projectName   = request.getParameter("project_name");
        String startDateStr  = request.getParameter("start_date");
        String endDateStr    = request.getParameter("end_date");
        String manager       = request.getParameter("manager");

        if (projectIdStr == null || projectIdStr.trim().isEmpty() ||
            projectName == null || projectName.trim().isEmpty()) {
            out.print("Error: Required fields are missing.");
            return;
        }

        int projectId;
        try {
            projectId = Integer.parseInt(projectIdStr.trim());
        } catch (NumberFormatException e) {
            out.print("Error: Invalid Project ID.");
            return;
        }

        Date startDate = null;
        Date endDate   = null;
        try {
            if (startDateStr != null && !startDateStr.trim().isEmpty())
                startDate = Date.valueOf(startDateStr.trim());
            if (endDateStr != null && !endDateStr.trim().isEmpty())
                endDate = Date.valueOf(endDateStr.trim());
        } catch (IllegalArgumentException e) {
            out.print("Error: Invalid date format. Use yyyy-MM-dd.");
            return;
        }

        String sql = "INSERT INTO project (project_id, project_name, start_date, end_date, manager) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setString(2, projectName.trim());
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            ps.setString(5, manager == null ? "" : manager.trim());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.print("Project Added Successfully! (ID: " + projectId + ")");
            } else {
                out.print("Error: Project could not be added.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                out.print("Error: Project ID " + projectId + " already exists.");
            } else {
                out.print("Error: " + e.getMessage());
            }
        }
    }
}
