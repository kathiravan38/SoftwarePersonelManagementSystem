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

/**
 * LoginServlet
 * POST /LoginServlet
 * Params: username, password
 * Response: "SUCCESS" or "FAILURE"
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            out.print("FAILURE");
            return;
        }
        
        
        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    out.print("SUCCESS");
                } else {
                    out.print("FAILURE");
                }
            }

        } catch (Exception e) {
        	System.out.println("Login Error: " + e.getMessage());
            out.print("FAILURE");
        }
    }
}
