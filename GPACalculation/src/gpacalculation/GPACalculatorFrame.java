/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gpacalculation1;

/**
 *
 * @author Taif Alzahrani
 */
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GPACalculatorFrame extends javax.swing.JFrame {

    private static String userID;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private DefaultTableModel model;

    public GPACalculatorFrame(String userID) {
    this.userID = userID;
    initComponents();  // Initialize the components
    connectToDatabase();  // Connect to the database
    loadUserScale();  // Load the user's GPA scale
    loadUserGPA();  // Load the user's saved GPA
    loadUserSubjects();  // Load the user's subjects
    // Apply theme
    String theme = loadUserTheme();
    applyTheme(theme);
}

// Method to apply the theme to the Greeting interface
private void applyTheme(String theme) {
    if (theme == null || theme.isEmpty()) {
        System.err.println("Theme is not set. Applying default theme.");
        return;  // Apply default behavior if the theme is null
    }

    switch (theme) {
        case "dark":
            jPanel1.setBackground(new java.awt.Color(50, 50, 50));
            jLabel1.setForeground(java.awt.Color.WHITE);
            break;
        case "light":
            jPanel1.setBackground(java.awt.Color.WHITE);
            jLabel1.setForeground(java.awt.Color.BLACK);
            break;
        default:
            System.err.println("Unknown theme. Applying default theme.");
            break;
    }
}


    private void connectToDatabase() {
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/GPA", "GPA", "GPA");
            System.out.println("Connected to the database successfully.");
        } catch (SQLException ex) {
            System.err.println("Error connecting to the database: " + ex.getMessage());
        }
    }

    private void loadUserData() {
        loadGpaScale();  // Load user's GPA scale
        loadUserSubjects();  // Load previously added subjects
        loadUserGPA();  // Load previously calculated GPA
    }

    private void loadGpaScale() {
        try {
            String query = "SELECT scale FROM users WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userID);
            rs = pst.executeQuery();

            if (rs.next()) {
                String scale = rs.getString("scale");
                if ("5.0".equals(scale)) {
                    jRadioButton5Point0.setSelected(true);
                } else if ("4.0".equals(scale)) {
                    jRadioButton4Point0.setSelected(true);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading GPA scale: " + ex.getMessage());
        }
    }

    private void loadUserGPA() {
    try {
        String query = "SELECT GPA FROM Users WHERE user_id = ?";
        pst = con.prepareStatement(query);
        pst.setString(1, userID);  // Use the logged-in user's ID
        rs = pst.executeQuery();

        if (rs.next()) {
            double gpa = rs.getDouble("GPA");
            jTextFieldGPA.setText(String.format("%.2f", gpa));  // Display the GPA in the text field
        } else {
            jTextFieldGPA.setText("");  // No GPA found
        }
    } catch (SQLException ex) {
        System.err.println("Error loading GPA: " + ex.getMessage());
    }
}


    private void saveGPAInDatabase(double gpa) {
    try {
        String query = "UPDATE Users SET GPA = ? WHERE user_id = ?";
        pst = con.prepareStatement(query);
        pst.setDouble(1, gpa);  // Set the calculated GPA
        pst.setString(2, userID);  // Use the logged-in user's ID
        pst.executeUpdate();
        System.out.println("GPA saved successfully.");
    } catch (SQLException ex) {
        System.err.println("Error saving GPA: " + ex.getMessage());
    }
}

    private void loadUserSubjects() {
        try {
            String query = "SELECT subject_Name, credit_hours, grade FROM Subjects WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userID);
            rs = pst.executeQuery();

            while (rs.next()) {
                String subjectName = rs.getString("subject_Name");
                int hours = rs.getInt("credit_hours");
                String grade = rs.getString("grade");
                model.addRow(new Object[]{subjectName, hours, grade});
            }
        } catch (SQLException ex) {
            System.err.println("Error loading subjects: " + ex.getMessage());
        }
    }

private void calculateGPA() {
    if (!jRadioButton5Point0.isSelected() && !jRadioButton4Point0.isSelected()) {
        JOptionPane.showMessageDialog(this, "Please select a GPA scale before calculating.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Get the selected GPA scale
    double selectedScale = jRadioButton5Point0.isSelected() ? 5.0 : 4.0;

    // Save the selected GPA scale to the database
    saveScaleToDatabase(selectedScale);

    double totalPoints = 0;
    int totalCredits = 0;

    for (int i = 0; i < model.getRowCount(); i++) {
        String grade = (String) model.getValueAt(i, 2);
        int hours = (int) model.getValueAt(i, 1);
        totalCredits += hours;
        totalPoints += hours * convertGradeToPoints(grade, selectedScale);
    }

    double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
    jTextFieldGPA.setText(String.format("%.2f", gpa));  // Display GPA in the text field

    // Save GPA to the database
    saveGPAInDatabase(gpa);
}





    private double convertGradeToPoints(String grade, double scale) {
        switch (grade) {
            case "A+":
                return scale == 5.0 ? 5.0 : 4.0;
            case "A":
                return scale == 5.0 ? 4.75 : 4.0;
            case "B+":
                return scale == 5.0 ? 4.5 : 3.5;
            case "B":
                return scale == 5.0 ? 4.0 : 3.0;
            case "C":
                return scale == 5.0 ? 3.0 : 2.0;
            case "F":
                return 0.0;
            default:
                return 0.0;
        }
    }

    private void addSubject() {
        String subjectName = jTextFieldSubject.getText();
        int hours;
        try {
            hours = Integer.parseInt(jTextFieldHours.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for credit hours.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String grade = (String) jComboBoxGrades.getSelectedItem();
        model.addRow(new Object[]{subjectName, hours, grade});
        saveSubjectToDatabase(subjectName, hours, grade);
    }

    private void saveSubjectToDatabase(String subjectName, int hours, String grade) {
        try {
            String query = "INSERT INTO Subjects (subject_Name, credit_hours, grade, user_id) VALUES (?, ?, ?, ?)";
            pst = con.prepareStatement(query);
            pst.setString(1, subjectName);
            pst.setInt(2, hours);
            pst.setString(3, grade);
            pst.setString(4, userID);
            pst.executeUpdate();
            System.out.println("Subject saved to database.");
        } catch (SQLException ex) {
            System.err.println("Error saving subject: " + ex.getMessage());
        }
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
    int selectedRow = jTableSubjects.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a subject to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String subjectName = (String) jTableSubjects.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this subject?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        deleteSubjectFromDatabase(subjectName);
        ((DefaultTableModel) jTableSubjects.getModel()).removeRow(selectedRow);
        
        // Recalculate GPA after subject deletion
        calculateGPA();
    }
}


    private void deleteSubjectFromDatabase(String subjectName) {
        try {
            String query = "DELETE FROM Subjects WHERE subject_Name = ? AND user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, subjectName);
            pst.setString(2, userID);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Subject deleted from the database.");
            } else {
                System.out.println("No subject found to delete.");
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting subject: " + ex.getMessage());
        }
    }

    private void returnToGreeting() {
        Greeting greetingFrame = new Greeting(userID);
        greetingFrame.setVisible(true);  // Open the Greeting interface
        this.dispose();  // Close the GPA Calculator interface
    }
    
private void saveScaleToDatabase(double scale) {
    try {
        String query = "UPDATE Users SET scale = ? WHERE user_id = ?";
        pst = con.prepareStatement(query);
        pst.setDouble(1, scale);  // Set the selected GPA scale
        pst.setString(2, userID);  // Use the logged-in user's ID
        pst.executeUpdate();
        System.out.println("GPA scale saved successfully.");
    } catch (SQLException ex) {
        System.err.println("Error saving GPA scale: " + ex.getMessage());
    }
}


private void loadUserScale() {
    try {
        String query = "SELECT scale FROM Users WHERE user_id = ?";
        pst = con.prepareStatement(query);
        pst.setString(1, userID);
        rs = pst.executeQuery();

        if (rs.next()) {
            double scale = rs.getDouble("scale");
            if (scale == 5.0) {
                jRadioButton5Point0.setSelected(true);
            } else {
                jRadioButton4Point0.setSelected(true);
            }
        }
    } catch (SQLException ex) {
        System.err.println("Error loading scale: " + ex.getMessage());
    }
}

private String loadUserTheme() {
    String theme = "light";  // Default theme
    try {
        String query = "SELECT theme FROM Users WHERE user_id = ?";
        pst = con.prepareStatement(query);
        pst.setString(1, userID);  // Use the current user ID
        rs = pst.executeQuery();
        if (rs.next()) {
            theme = rs.getString("theme");
        }
    } catch (SQLException ex) {
        System.err.println("Error fetching user theme: " + ex.getMessage());
    }
    return theme;
}

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JButton deleteButton = new javax.swing.JButton("Delete Subject");
        deleteButton.addActionListener(this::deleteButtonActionPerformed);

        // Labels, Text Fields, Combo Box
        jLabel1 = new javax.swing.JLabel("Subject:");
        jTextFieldSubject = new javax.swing.JTextField(10);
        jLabel2 = new javax.swing.JLabel("Credit Hours:");
        jTextFieldHours = new javax.swing.JTextField(5);
        jComboBoxGrades = new javax.swing.JComboBox<>(new String[] { "A+", "A", "B+", "B", "C", "F" });
        jLabel3 = new javax.swing.JLabel("Grade:");
        jButtonAdd = new javax.swing.JButton("Add Subject");
        jButtonAdd.addActionListener(evt -> addSubject());

        jTableSubjects = new javax.swing.JTable();
        model = new DefaultTableModel(new Object[][] {}, new String[] { "Subject", "Credit Hours", "Grade" });
        jTableSubjects.setModel(model);
        jScrollPane1 = new javax.swing.JScrollPane(jTableSubjects);

        jLabel4 = new javax.swing.JLabel("GPA Scale:");
        jRadioButton5Point0 = new javax.swing.JRadioButton("5.0");
        jRadioButton4Point0 = new javax.swing.JRadioButton("4.0");
        buttonGroup1.add(jRadioButton5Point0);
        buttonGroup1.add(jRadioButton4Point0);

        jButtonCalculate = new javax.swing.JButton("Calculate GPA");
        jButtonCalculate.addActionListener(evt -> calculateGPA());

        jTextFieldGPA = new javax.swing.JTextField(10);
        jTextFieldGPA.setEditable(false);
        jLabel5 = new javax.swing.JLabel("Your GPA:");

        // Layout Setup
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Subject, Credit Hours, Grade, Add Button
        gbc.gridx = 0; gbc.gridy = 0;
        jPanel1.add(jLabel1, gbc);
        gbc.gridx = 1;
        jPanel1.add(jTextFieldSubject, gbc);
        gbc.gridx = 2;
        jPanel1.add(jLabel2, gbc);
        gbc.gridx = 3;
        jPanel1.add(jTextFieldHours, gbc);
        gbc.gridx = 4;
        jPanel1.add(jLabel3, gbc);
        gbc.gridx = 5;
        jPanel1.add(jComboBoxGrades, gbc);
        gbc.gridx = 6;
        jPanel1.add(jButtonAdd, gbc);

        // Row 2: Subject Table
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        jPanel1.add(jScrollPane1, gbc);

        // Row 3: GPA Scale Radio Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        jPanel1.add(jLabel4, gbc);
        gbc.gridx = 1;
        jPanel1.add(jRadioButton5Point0, gbc);
        gbc.gridx = 2;
        jPanel1.add(jRadioButton4Point0, gbc);

        // Row 4: Calculate GPA Button, GPA Label, GPA Text Field
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        jPanel1.add(jButtonCalculate, gbc);
        gbc.gridx = 1;
        jPanel1.add(jLabel5, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        jPanel1.add(jTextFieldGPA, gbc);

        // Return button
        JButton returnButton = new JButton("Return to Greeting");
        returnButton.addActionListener(evt -> returnToGreeting());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        jPanel1.add(returnButton, gbc);

        // Add Delete Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        jPanel1.add(deleteButton, gbc);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        add(jPanel1);
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new GPACalculatorFrame(userID).setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCalculate;
    private javax.swing.JComboBox<String> jComboBoxGrades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableSubjects;
    private javax.swing.JTextField jTextFieldGPA;
    private javax.swing.JTextField jTextFieldHours;
    private javax.swing.JTextField jTextFieldSubject;
    private javax.swing.JRadioButton jRadioButton5Point0;
    private javax.swing.JRadioButton jRadioButton4Point0;
}
