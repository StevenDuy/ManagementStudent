package com.fpi.duyhh.Assignment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

public class StudentManagementFrame extends JFrame {
    // UI Components
    private JTextField textFieldStudentID, textFieldName, textFieldScore1,
            textFieldScore2, textFieldScore3, textFieldSearch;
    private JComboBox<String> comboBoxMajor;
    private JLabel labelSubject1, labelSubject2, labelSubject3;
    private JButton btnAdd, btnEdit, btnDelete;
    private JTable table;
    private DefaultTableModel tableModel;

    // Business Logic
    private StudentManager studentManager;
    // Track sorting order for each column: 0: ID, 1: Name, 2: Major, 3: Average Score, 4: Rank
    private boolean[] sortAscending = new boolean[]{true, true, true, true, true};

    public StudentManagementFrame() {
        studentManager = new StudentManager();

        setTitle("Student Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Top Panel: contains input form
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 1. Student Information Panel
        JPanel studentInfoPanel = new JPanel(new GridBagLayout());
        studentInfoPanel.setBorder(new TitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        studentInfoPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        textFieldStudentID = new JTextField(10);
        studentInfoPanel.add(textFieldStudentID, gbc);

        gbc.gridx = 2;
        studentInfoPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        textFieldName = new JTextField(10);
        studentInfoPanel.add(textFieldName, gbc);

        gbc.gridx = 4;
        studentInfoPanel.add(new JLabel("Major:"), gbc);
        gbc.gridx = 5;
        comboBoxMajor = new JComboBox<>(new String[]{"IT", "Design", "Business"});
        studentInfoPanel.add(comboBoxMajor, gbc);

        topPanel.add(studentInfoPanel);

        // 2. Subject Scores Panel
        JPanel subjectPanel = new JPanel(new GridBagLayout());
        subjectPanel.setBorder(new TitledBorder("Subject Scores"));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        labelSubject1 = new JLabel("Subject 1:");
        subjectPanel.add(labelSubject1, gbc);
        gbc.gridx = 1;
        textFieldScore1 = new JTextField(8);
        subjectPanel.add(textFieldScore1, gbc);

        gbc.gridx = 2;
        labelSubject2 = new JLabel("Subject 2:");
        subjectPanel.add(labelSubject2, gbc);
        gbc.gridx = 3;
        textFieldScore2 = new JTextField(8);
        subjectPanel.add(textFieldScore2, gbc);

        gbc.gridx = 4;
        labelSubject3 = new JLabel("Subject 3:");
        subjectPanel.add(labelSubject3, gbc);
        gbc.gridx = 5;
        textFieldScore3 = new JTextField(8);
        subjectPanel.add(textFieldScore3, gbc);

        topPanel.add(subjectPanel);

        // 3. Control Panel: contains action buttons and search bar
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        controlPanel.add(actionPanel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        searchPanel.add(new JLabel("Search:"));
        textFieldSearch = new JTextField(15);
        searchPanel.add(textFieldSearch);
        controlPanel.add(searchPanel, BorderLayout.EAST);
        topPanel.add(controlPanel);

        // 4. Table Panel: displays student list
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Major", "Average Score", "Rank"}, 0);
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(false); // disable auto-sorting to use custom sorting
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ----- Events -----
        comboBoxMajor.addActionListener(e -> updateSubjects());
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                loadStudentFromTable();
            }
        });

        textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        // Sorting by clicking on table header
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col != -1 && !studentManager.getStudents().isEmpty()) {
                    sortAscending[col] = !sortAscending[col];
                    Comparator<Student> comp = studentManager.getComparator(col, sortAscending[col]);
                    studentManager.quickSort(comp);
                    filterTable();
                }
            }
        });

        updateSubjects();
        loadSampleData();
        updateTable();
    }

    // Calculate average of 3 scores, rounded with "#.##" format
    private double computeAverage(double a, double b, double c) {
        return Double.parseDouble(new java.text.DecimalFormat("#.##").format((a + b + c) / 3.0));
    }

    // Update subject labels based on selected Major
    private void updateSubjects() {
        String major = comboBoxMajor.getSelectedItem().toString();
        if (major.equals("IT")) {
            labelSubject1.setText("HTML:");
            labelSubject2.setText("CSS:");
            labelSubject3.setText("Math:");
        } else if (major.equals("Design")) {
            labelSubject1.setText("Color:");
            labelSubject2.setText("Photoshop:");
            labelSubject3.setText("AI:");
        } else if (major.equals("Business")) {
            labelSubject1.setText("Sales:");
            labelSubject2.setText("Marketing:");
            labelSubject3.setText("Finance:");
        }
    }

    // Validate input fields; if invalid, highlight with red border
    // Scores must be within range [0, 10]
    private boolean validateInput() {
        boolean isValid = true;

        // Reset borders
        textFieldStudentID.setBorder(new JTextField().getBorder());
        textFieldName.setBorder(new JTextField().getBorder());
        textFieldScore1.setBorder(new JTextField().getBorder());
        textFieldScore2.setBorder(new JTextField().getBorder());
        textFieldScore3.setBorder(new JTextField().getBorder());

        if (textFieldStudentID.getText().trim().isEmpty()) {
            textFieldStudentID.setBorder(new LineBorder(Color.RED, 2));
            isValid = false;
        }

        if (textFieldName.getText().trim().isEmpty()) {
            textFieldName.setBorder(new LineBorder(Color.RED, 2));
            isValid = false;
        }

        try {
            double s1 = Double.parseDouble(textFieldScore1.getText().trim());
            if (s1 < 0 || s1 > 10) {
                textFieldScore1.setBorder(new LineBorder(Color.RED, 2));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            textFieldScore1.setBorder(new LineBorder(Color.RED, 2));
            isValid = false;
        }

        try {
            double s2 = Double.parseDouble(textFieldScore2.getText().trim());
            if (s2 < 0 || s2 > 10) {
                textFieldScore2.setBorder(new LineBorder(Color.RED, 2));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            textFieldScore2.setBorder(new LineBorder(Color.RED, 2));
            isValid = false;
        }

        try {
            double s3 = Double.parseDouble(textFieldScore3.getText().trim());
            if (s3 < 0 || s3 > 10) {
                textFieldScore3.setBorder(new LineBorder(Color.RED, 2));
                isValid = false;
            }
        } catch (NumberFormatException e) {
            textFieldScore3.setBorder(new LineBorder(Color.RED, 2));
            isValid = false;
        }

        return isValid;
    }

    // Add new student, after ensuring valid input
    private void addStudent() {
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Please correct the highlighted errors! Scores must be between 0 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String id = textFieldStudentID.getText().trim();
            if (studentManager.findById(id) != null) {
                JOptionPane.showMessageDialog(this, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = textFieldName.getText().trim();
            String major = comboBoxMajor.getSelectedItem().toString();
            double s1 = Double.parseDouble(textFieldScore1.getText().trim());
            double s2 = Double.parseDouble(textFieldScore2.getText().trim());
            double s3 = Double.parseDouble(textFieldScore3.getText().trim());
            double avg = computeAverage(s1, s2, s3);
            String rank = studentManager.calculateRank(avg);

            Student s = new Student(id, name, major, s1, s2, s3, avg, rank);
            if (studentManager.addStudent(s)) {
                updateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit student information
    private void editStudent() {
        if (!validateInput()) {
            JOptionPane.showMessageDialog(this, "Please correct the highlighted errors! Scores must be between 0 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int index = selectedRow;
        Student s = studentManager.getStudents().get(index);
        String oldId = s.getId();

        try {
            String id = textFieldStudentID.getText().trim();
            String name = textFieldName.getText().trim();
            String major = comboBoxMajor.getSelectedItem().toString();
            double s1 = Double.parseDouble(textFieldScore1.getText().trim());
            double s2 = Double.parseDouble(textFieldScore2.getText().trim());
            double s3 = Double.parseDouble(textFieldScore3.getText().trim());
            double avg = computeAverage(s1, s2, s3);
            String rank = studentManager.calculateRank(avg);

            s.setId(id);
            s.setName(name);
            s.setMajor(major);
            s.setScore1(s1);
            s.setScore2(s2);
            s.setScore3(s3);
            s.setAverageScore(avg);
            s.setRank(rank);

            if (!oldId.equals(id)) {
                if (studentManager.findById(id) != null) {
                    JOptionPane.showMessageDialog(this, "New Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            studentManager.editStudent(oldId, s);
            updateTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete selected student
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = table.getValueAt(selectedRow, 0).toString();
            if (studentManager.deleteStudentById(id)) {
                updateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Update table based on current student list
    private void updateTable() {
        ArrayList<Student> list = studentManager.getStudents();
        updateTableWithList(list);
    }

    private void updateTableWithList(ArrayList<Student> list) {
        tableModel.setRowCount(0);
        for (Student s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getMajor(), s.getAverageScore(), s.getRank()
            });
        }
    }

    // Filter table based on keyword (searches across all columns)
    private void filterTable() {
        String searchText = textFieldSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            updateTable();
            return;
        }

        ArrayList<Student> filtered = new ArrayList<>();
        for (Student s : studentManager.getStudents()) {
            if (s.getId().toLowerCase().contains(searchText) ||
                s.getName().toLowerCase().contains(searchText) ||
                s.getMajor().toLowerCase().contains(searchText) ||
                String.valueOf(s.getAverageScore()).contains(searchText) ||
                s.getRank().toLowerCase().contains(searchText)) {
                filtered.add(s);
            }
        }

        updateTableWithList(filtered);
    }

    // Load student data from selected table row
    private void loadStudentFromTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int index = selectedRow;
            Student s = studentManager.getStudents().get(index);
            textFieldStudentID.setText(s.getId());
            textFieldName.setText(s.getName());
            comboBoxMajor.setSelectedItem(s.getMajor());
            textFieldScore1.setText(String.valueOf(s.getScore1()));
            textFieldScore2.setText(String.valueOf(s.getScore2()));
            textFieldScore3.setText(String.valueOf(s.getScore3()));
        }
    }

    // Main method
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new StudentManagementFrame().setVisible(true));
    }

    // Load 10 sample student records with varying names, majors, and scores
    private void loadSampleData() {
        studentManager.addStudent(new Student("BC00411", "Alice", "IT", 10, 9.5, 9.5, computeAverage(10,9.5,9.5), studentManager.calculateRank(computeAverage(10,9.5,9.5))));
        studentManager.addStudent(new Student("BC00410", "Bob", "Design", 8, 7, 7, computeAverage(8,7,7), studentManager.calculateRank(computeAverage(8,7,7))));
        studentManager.addStudent(new Student("BC00412", "Charlie", "Business", 6, 5, 7, computeAverage(6,5,7), studentManager.calculateRank(computeAverage(6,5,7))));
        studentManager.addStudent(new Student("BC00413", "David", "IT", 4, 4, 4, computeAverage(4,4,4), studentManager.calculateRank(computeAverage(4,4,4))));
        studentManager.addStudent(new Student("BC00414", "Eva", "Design", 7, 8, 7, computeAverage(7,8,7), studentManager.calculateRank(computeAverage(7,8,7))));
        studentManager.addStudent(new Student("BC00415", "Frank", "Business", 9, 9, 8, computeAverage(9,9,8), studentManager.calculateRank(computeAverage(9,9,8))));
        studentManager.addStudent(new Student("BC00416", "Grace", "IT", 8.5, 8, 8.5, computeAverage(8.5,8,8.5), studentManager.calculateRank(computeAverage(8.5,8,8.5))));
        studentManager.addStudent(new Student("BC00417", "Hank", "Design", 5, 6, 4, computeAverage(5,6,4), studentManager.calculateRank(computeAverage(5,6,4))));
        studentManager.addStudent(new Student("BC00418", "Ivy", "Business", 10, 10, 10, computeAverage(10,10,10), studentManager.calculateRank(computeAverage(10,10,10))));
        studentManager.addStudent(new Student("BC00419", "Jack", "IT", 7, 7, 7, computeAverage(7,7,7), studentManager.calculateRank(computeAverage(7,7,7))));
    }
}
