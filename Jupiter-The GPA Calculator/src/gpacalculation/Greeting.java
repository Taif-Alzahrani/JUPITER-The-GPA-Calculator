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
import java.awt.*;
import java.sql.*;

public class Greeting extends javax.swing.JFrame {
    public static String userId;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    public Greeting(String userId) {
        super("Welcome to GPA Calculator");
        this.userId = userId;
        initComponents();
        connectToDatabase();
        
        // Apply theme
        String theme = loadUserTheme();
        applyTheme(theme);
    }

    // Method to connect to the database
    private void connectToDatabase() {
        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/GPA", "GPA", "GPA");
            System.out.println("Connected to the database successfully.");
        } catch (SQLException ex) {
            System.err.println("Error connecting to the database: " + ex.getMessage());
        }
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
                jButton1.setBackground(Color.GRAY);
                jButton2.setBackground(Color.GRAY);
                jButton3.setBackground(Color.GRAY);
                jButton4.setBackground(Color.GRAY);
                break;
            case "light":
                jPanel1.setBackground(java.awt.Color.WHITE);
                jLabel1.setForeground(java.awt.Color.BLACK);
                jButton1.setBackground(Color.LIGHT_GRAY);
                jButton2.setBackground(Color.LIGHT_GRAY);
                jButton3.setBackground(Color.LIGHT_GRAY);
                jButton4.setBackground(Color.LIGHT_GRAY);
                break;
            default:
                System.err.println("Unknown theme. Applying default theme.");
                break;
        }
    }

    // Method to load the user theme from the database
    private String loadUserTheme() {
        String theme = "light";  // Default theme
        try {
            String query = "SELECT theme FROM Users WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);  // Use the current user ID
            rs = pst.executeQuery();
            if (rs.next()) {
                theme = rs.getString("theme");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user theme: " + ex.getMessage());
        }
        return theme;
    }

    // Method to fetch user's name from the database
    private String getUserName() {
        String name = "User";
        try {
            String query = "SELECT name FROM Users WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user name: " + ex.getMessage());
        }
        return name;
    }

    // Method to fetch user's subjects, GPA, and scale
    private String getUserSummary() {
        StringBuilder summary = new StringBuilder();

        try {
            // Get user's scale and GPA
            String query = "SELECT scale, GPA FROM Users WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                double gpa = rs.getDouble("GPA");
                String scale = rs.getString("scale");
                summary.append("Scale: ").append(scale).append("\n");
                summary.append("GPA: ").append(String.format("%.2f", gpa)).append("\n\n");
            }

            // Get user's subjects
            query = "SELECT subject_Name, credit_hours, grade FROM Subjects WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            summary.append("Subjects:\n");
            while (rs.next()) {
                String subjectName = rs.getString("subject_Name");
                int creditHours = rs.getInt("credit_hours");
                String grade = rs.getString("grade");
                summary.append(subjectName).append(" - ").append(creditHours).append(" hours - Grade: ").append(grade).append("\n");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user summary: " + ex.getMessage());
        }

        return summary.toString();
    }

    // Method to create the pop-up summary window
    // Method to create the pop-up summary window
private void showSummaryDialog() {
    // Fetch user summary from the database
    String userName = getUserName();
    String summaryText = getUserSummary();

    // Create a dialog for the summary
    JDialog summaryDialog = new JDialog(this, userName + " Summary", true);  // Modal dialog
    summaryDialog.setSize(400, 300);  // Set size of dialog

    // Create a text area for showing the summary
    JTextArea summaryArea = new JTextArea(10, 30);
    summaryArea.setText(summaryText);
    summaryArea.setEditable(false);

    // Apply the theme to the summary dialog
    String currentTheme = getUserTheme();  // Fetch the user's theme
    applyThemeToDialog(summaryDialog, summaryArea, currentTheme);

    // Add components to the dialog
    summaryDialog.add(new JScrollPane(summaryArea), BorderLayout.CENTER);
    summaryDialog.setLocationRelativeTo(this);  // Center the dialog on the parent frame

    summaryDialog.setVisible(true);  // Show the dialog
}

// Method to apply the theme to the summary dialog
private void applyThemeToDialog(JDialog dialog, JTextArea textArea, String theme) {
    if ("dark".equals(theme)) {
        dialog.getContentPane().setBackground(new java.awt.Color(169, 169, 169)); // Lighter gray for dark theme
        textArea.setBackground(new java.awt.Color(105, 105, 105)); // Darker gray background for text area
        textArea.setForeground(java.awt.Color.WHITE); // White text
    } else {
        dialog.getContentPane().setBackground(java.awt.Color.WHITE); // White background for light theme
        textArea.setBackground(java.awt.Color.LIGHT_GRAY); // Light gray for text area
        textArea.setForeground(java.awt.Color.BLACK); // Black text
    }
}


    // Method to show preferences dialog (theme selection)
    private void showPreferencesDialog() {
        JDialog preferencesDialog = new JDialog(this, "Preferences", true);
        preferencesDialog.setSize(300, 200);
        preferencesDialog.setLayout(new FlowLayout());

        // Create radio buttons for Dark Mode and Light Mode
        JRadioButton darkModeButton = new JRadioButton("Dark Mode");
        JRadioButton lightModeButton = new JRadioButton("Light Mode");

        // Group the buttons
        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(darkModeButton);
        themeGroup.add(lightModeButton);

        // Load current user theme and select the correct radio button
        String currentTheme = getUserTheme();
        if ("dark".equals(currentTheme)) {
            darkModeButton.setSelected(true);
        } else {
            lightModeButton.setSelected(true);
        }

        // Add the buttons to the dialog
        preferencesDialog.add(darkModeButton);
        preferencesDialog.add(lightModeButton);

        // Add an "Apply" button to save the selection
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> {
            String selectedTheme = lightModeButton.isSelected() ? "light" : "dark";
            saveThemeToDatabase(selectedTheme);  // Save the theme selection to the database
            applyTheme(selectedTheme);  // Apply the theme to the current window
            preferencesDialog.dispose();  // Close the dialog
        });

        preferencesDialog.add(applyButton);
        preferencesDialog.setLocationRelativeTo(this);  // Center the dialog on the main window
        preferencesDialog.setVisible(true);
    }

    // Method to save the user's selected theme to the database
    private void saveThemeToDatabase(String theme) {
        try {
            String query = "UPDATE Users SET theme = ? WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, theme);
            pst.setString(2, userId);
            pst.executeUpdate();
            System.out.println("Theme saved successfully.");
        } catch (SQLException ex) {
            System.err.println("Error saving theme: " + ex.getMessage());
        }
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Welcome to ");
        jLabel1.setText("Welcome to the GPA Calculator, " + UserLogin.currentUser + "!");

        jButton1.setText("1.GPA calculator");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("2.summary");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("3.prefrences");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("4.Exit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(172, 172, 172)
                        .addComponent(jLabel1)
                        .addGap(96, 96, 96)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton3))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jButton2))))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(123, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new GPACalculatorFrame(userId).setVisible(true);  // Go to Calculator
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       int confirmed = JOptionPane.showConfirmDialog(null, 
            "Are you sure you want to exit the application?", "Exit Confirmation",
            JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            // Close the application
            System.exit(0);
        }
    
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                showSummaryDialog();  // Call summary pop-up when button is pressed
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        showPreferencesDialog();  // Open Preferences window
        
    }//GEN-LAST:event_jButton3ActionPerformed
    private void saveThemeAndApply(String selectedTheme) {
    saveThemeToDatabase(selectedTheme);
    applyTheme(selectedTheme);  // Apply the theme to the current Preferences window
}
    

    // Method to fetch and apply the user's saved theme from the database
    private String getUserTheme() {
        String theme = "light";  // Default to light mode
        try {
            String query = "SELECT theme FROM Users WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                theme = rs.getString("theme");
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user theme: " + ex.getMessage());
        }
        return theme;
    }

    // Method to save the user's selected theme to the database
    private void saveUserTheme(String theme) {
        try {
            String query = "UPDATE Users SET theme = ? WHERE user_id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, theme);
            pst.setString(2, userId);
            pst.executeUpdate();
            System.out.println("Theme saved successfully.");
        } catch (SQLException ex) {
            System.err.println("Error saving theme: " + ex.getMessage());
        }
    }

    // Method to apply the saved theme to the current interface
    private void applyUserTheme() {
        String theme = getUserTheme();
        if ("dark".equals(theme)) {
            // Apply Dark Mode
            jPanel1.setBackground(Color.DARK_GRAY);
            jLabel1.setForeground(Color.WHITE);
            jButton1.setBackground(Color.GRAY);
            jButton2.setBackground(Color.GRAY);
            jButton3.setBackground(Color.GRAY);
            jButton4.setBackground(Color.GRAY);
        } else {
            // Apply Light Mode
            jPanel1.setBackground(Color.WHITE);
            jLabel1.setForeground(Color.BLACK);
            jButton1.setBackground(Color.LIGHT_GRAY);
            jButton2.setBackground(Color.LIGHT_GRAY);
            jButton3.setBackground(Color.LIGHT_GRAY);
            jButton4.setBackground(Color.LIGHT_GRAY);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Greeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Greeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Greeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Greeting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Greeting(userId).setVisible(true);
            }
        });
    }

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
