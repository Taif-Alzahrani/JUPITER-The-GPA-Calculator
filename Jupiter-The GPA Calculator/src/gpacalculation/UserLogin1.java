/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gpacalculation1;

/**
 *
 * @author Taif Alzahrani
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin1 {

    public static void main(String[] args) {
//        JFrame frame = new JFrame("User Login");
//        JPanel panel = new JPanel();
//        frame.setSize(300, 150);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        // Username and password fields for login
//        JLabel userLabel = new JLabel("Username");
//        JTextField userText = new JTextField(20);
//        JLabel passwordLabel = new JLabel("Password");
//        JPasswordField passwordText = new JPasswordField(20);
//
//        // Buttons for login and registration
//        JButton loginButton = new JButton("Login");
//        JButton registerButton = new JButton("Register");
//
//        // Adding components to the panel
//        panel.add(userLabel);
//        panel.add(userText);
//        panel.add(passwordLabel);
//        panel.add(passwordText);
//        panel.add(loginButton);
//        panel.add(registerButton);
//
//        frame.add(panel);
//        frame.setVisible(true);
//
//        // ActionListener for the login button
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = userText.getText();
//                String password = new String(passwordText.getPassword());
//
//                // Call validateLogin method
//                if (validateLogin(username, password)) {
//                    JOptionPane.showMessageDialog(frame, "Login successful. Welcome, " + username + "!");
//                } else {
//                    JOptionPane.showMessageDialog(frame, "Invalid username or password. Please try again.");
//                }
//            }
//        });
//
//        // ActionListener for the register button
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Open registration dialog
//                openRegistrationDialog();
//            }
//        });
//    }

    // Method to validate login credentials
//public static boolean validateLogin(String username, String password) {
//    Connection conn = null;
//    PreparedStatement stmt = null;
//    ResultSet rs = null;
//    boolean isValid = false;
//
//    try {
//        DatabaseManager1 dbManager = new DatabaseManager1();
//        conn = dbManager.getConnection();
//        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
//        stmt = conn.prepareStatement(query);
//        stmt.setString(1, username);
//        stmt.setString(2, password);  // Ensure password matches (hash if necessary!)
//
//        rs = stmt.executeQuery();
//        if (rs.next()) {
//            isValid = true;  // User exists in the database
//        } else {
//            isValid = false;  // User does not exist
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    } finally {
//        try {
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//            if (conn != null) conn.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
//    return isValid;
//}
//
//    // Opens the registration form in a dialog
//    private static void openRegistrationDialog() {
//        JFrame frame = new JFrame("User Registration");
//        JPanel panel = new JPanel();
//        frame.setSize(400, 200);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        // Input fields for registration
//        JLabel userLabel = new JLabel("Username:");
//        JTextField userText = new JTextField(20);
//        JLabel passwordLabel = new JLabel("Password:");
//        JPasswordField passwordText = new JPasswordField(20);
//        JLabel gpaLabel = new JLabel("GPA Scale:");
//        JComboBox<String> gpaComboBox = new JComboBox<>(new String[] {"4.0", "5.0"});
//        JLabel layoutLabel = new JLabel("Layout Preference:");
//        JComboBox<String> layoutComboBox = new JComboBox<>(new String[] {"blue-green", "black-gray-white", "yellow-pink"});
//
//        // Registration button
//        JButton registerButton = new JButton("Register");
//
//        // Add components to panel
//        panel.add(userLabel);
//        panel.add(userText);
//        panel.add(passwordLabel);
//        panel.add(passwordText);
//        panel.add(gpaLabel);
//        panel.add(gpaComboBox);
//        panel.add(layoutLabel);
//        panel.add(layoutComboBox);
//        panel.add(registerButton);
//
//        frame.add(panel);
//        frame.setVisible(true);
//
//        // ActionListener for the registration button
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = userText.getText();
//                String password = new String(passwordText.getPassword());
//                String gpaScale = (String) gpaComboBox.getSelectedItem();
//                String layoutPreference = (String) layoutComboBox.getSelectedItem();
//
//                try {
//                    // Call the registerUser method in UserRegistration1
//                    UserRegistration1.registerUser(username, password);
//                    JOptionPane.showMessageDialog(frame, "User registered successfully!");
//                    frame.dispose();  // Close the registration dialog
//                } catch (SQLException ex) {
//                    JOptionPane.showMessageDialog(frame, "Error during registration: " + ex.getMessage());
//                }
//            }
//        });
//    }
    }}
