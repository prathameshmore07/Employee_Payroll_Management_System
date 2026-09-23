import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

public class PayrollGUI extends JFrame {
    protected PayrollManager manager; // payroll manager instance

    // form components
    protected JTextField txtId;
    protected JTextField txtName;
    protected JComboBox<String> cbDepartment;
    protected JTextField txtDesignation;
    protected JTextField txtBasicSalary;
    protected JTextField txtHra;
    protected JTextField txtDa;
    protected JTextField txtTa;
    protected JTextField txtPf;
    protected JTextField txtTax;

    // table and summary components
    protected JTable employeeTable;
    protected DefaultTableModel tableModel;
    protected JLabel lblSummary;

    // constructor to build gui
    public PayrollGUI() {
        manager = new PayrollManager();

        setTitle("Employee Payroll Management System");
        setSize(1020, 730);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // title header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        JLabel titleLabel = new JLabel("EMPLOYEE PAYROLL MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // center split panel
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // left form panel
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Employee & Salary Information",
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(41, 128, 185)));

        formPanel.add(new JLabel(" Employee ID:"));
        txtId = new JTextField();
        formPanel.add(txtId);

        formPanel.add(new JLabel(" Employee Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel(" Department:"));
        String[] departments = {"IT / Engineering", "Human Resources", "Finance & Accounts", "Sales & Marketing", "Operations"};
        cbDepartment = new JComboBox<>(departments);
        formPanel.add(cbDepartment);

        formPanel.add(new JLabel(" Designation:"));
        txtDesignation = new JTextField();
        formPanel.add(txtDesignation);

        formPanel.add(new JLabel(" Basic Salary (₹):"));
        txtBasicSalary = new JTextField();
        formPanel.add(txtBasicSalary);

        formPanel.add(new JLabel(" Allowance - HRA (₹):"));
        txtHra = new JTextField("0");
        formPanel.add(txtHra);

        formPanel.add(new JLabel(" Allowance - DA (₹):"));
        txtDa = new JTextField("0");
        formPanel.add(txtDa);

        formPanel.add(new JLabel(" Allowance - TA (₹):"));
        txtTa = new JTextField("0");
        formPanel.add(txtTa);

        formPanel.add(new JLabel(" Deduction - PF (₹):"));
        txtPf = new JTextField("0");
        formPanel.add(txtPf);

        formPanel.add(new JLabel(" Deduction - Tax (₹):"));
        txtTax = new JTextField("0");
        formPanel.add(txtTax);

        leftPanel.add(formPanel, BorderLayout.CENTER);

        // action buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JButton btnAdd = new JButton("Add Employee");
        JButton btnUpdate = new JButton("Update Employee");
        JButton btnDelete = new JButton("Delete Employee");
        JButton btnSearch = new JButton("Search by ID (HashMap)");
        JButton btnViewAll = new JButton("View All (LinkedList)");
        JButton btnViewSorted = new JButton("View Sorted (TreeMap)");
        JButton btnPayslip = new JButton("Generate Payslip");
        JButton btnClear = new JButton("Clear Form");

        Color btnColor = new Color(52, 152, 219);
        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnSearch, btnViewAll, btnViewSorted, btnPayslip, btnClear};
        for (JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
        }

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnViewSorted);
        buttonPanel.add(btnPayslip);
        buttonPanel.add(btnClear);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        centerPanel.add(leftPanel);

        // right table panel
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Employee Payroll Records",
                0, 0, new Font("Arial", Font.BOLD, 14), new Color(41, 128, 185)));

        String[] columnNames = {"ID", "Name", "Department", "Designation", "Basic Pay", "Allowances", "Deductions", "Net Salary"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(24);
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(230, 240, 250));
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // fill form when table row clicked
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int selectedRow = employeeTable.getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                        Object idObj = tableModel.getValueAt(selectedRow, 0);
                        if (idObj != null) {
                            int empId = Integer.parseInt(idObj.toString());
                            PayrollEmployee emp = manager.searchById(empId);
                            if (emp != null) {
                                populateForm(emp);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);

        // bottom summary bar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footerPanel.setBackground(new Color(245, 245, 245));
        lblSummary = new JLabel("Total Employees: 0 | Total Payroll Payout: ₹ 0.00");
        lblSummary.setFont(new Font("Arial", Font.BOLD, 14));
        lblSummary.setForeground(new Color(44, 62, 80));
        footerPanel.add(lblSummary);
        add(footerPanel, BorderLayout.SOUTH);

        // button listeners
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleUpdate();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDelete();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable(manager.getAllEmployees());
                JOptionPane.showMessageDialog(PayrollGUI.this,
                        "Displaying all records in sequential insertion order from LinkedList.",
                        "View All (LinkedList)", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnViewSorted.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable(manager.getSortedEmployees());
                JOptionPane.showMessageDialog(PayrollGUI.this,
                        "Displaying records sorted by Employee ID from TreeMap.",
                        "View Sorted (TreeMap)", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnPayslip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePayslip();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    // parses employee id with strict error checking
    private int parseId(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.\nPlease enter a valid number (e.g. 105).");
        }
        try {
            int val = Integer.parseInt(text.trim());
            if (val <= 0) {
                throw new IllegalArgumentException("Employee ID must be a positive number greater than 0.");
            }
            return val;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid ID: '" + text.trim() + "'.\nEmployee ID must contain only digits.");
        }
    }

    // parses money amount with positivity check
    private double parseAmount(String text, String fieldName, boolean required) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            if (required) {
                throw new IllegalArgumentException(fieldName + " is required.\nPlease enter an amount.");
            }
            return 0.0;
        }
        try {
            double val = Double.parseDouble(text.trim());
            if (val < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }
            if (required && val <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0.");
            }
            return val;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": '" + text.trim() + "'.\nMust be a valid numeric amount.");
        }
    }

    // adds employee with duplicate prevention
    private void handleAdd() {
        try {
            int id = parseId(txtId.getText());

            // check if id already exists before anything else
            if (manager.searchById(id) != null) {
                JOptionPane.showMessageDialog(this,
                        "Error: Employee with ID " + id + " already exists!\nDuplicate ID is not allowed.",
                        "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Employee Name cannot be empty.");
            }

            String department = (cbDepartment.getSelectedItem() != null)
                    ? cbDepartment.getSelectedItem().toString() : "General";

            String designation = txtDesignation.getText().trim();
            if (designation.isEmpty()) {
                throw new IllegalArgumentException("Designation cannot be empty.");
            }

            double basicSalary = parseAmount(txtBasicSalary.getText(), "Basic Salary", true);

            double hra = parseAmount(txtHra.getText(), "HRA", false);
            double da = parseAmount(txtDa.getText(), "DA", false);
            double ta = parseAmount(txtTa.getText(), "TA", false);
            double[] allowances = {hra, da, ta};

            double pf = parseAmount(txtPf.getText(), "PF", false);
            double tax = parseAmount(txtTax.getText(), "Tax", false);
            double[] deductions = {pf, tax};

            PayrollEmployee newEmp = new PayrollEmployee(id, name, department, designation, basicSalary, allowances, deductions);

            boolean added = manager.addEmployee(newEmp);
            if (added) {
                refreshTable(manager.getAllEmployees());
                JOptionPane.showMessageDialog(this,
                        "Employee added successfully!\nName: " + newEmp.getName()
                                + "\nNet Salary: ₹ " + String.format("%.2f", newEmp.calculateNetSalary()),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Employee with ID " + id + " already exists!\nDuplicate ID is not allowed.",
                        "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // updates employee details
    private void handleUpdate() {
        try {
            int id = parseId(txtId.getText());

            // check if id exists
            if (manager.searchById(id) == null) {
                JOptionPane.showMessageDialog(this,
                        "Error: Employee with ID " + id + " does not exist!\nCannot update a non-existent employee.",
                        "Employee Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Employee Name cannot be empty.");
            }

            String department = (cbDepartment.getSelectedItem() != null)
                    ? cbDepartment.getSelectedItem().toString() : "General";

            String designation = txtDesignation.getText().trim();
            if (designation.isEmpty()) {
                throw new IllegalArgumentException("Designation cannot be empty.");
            }

            double basicSalary = parseAmount(txtBasicSalary.getText(), "Basic Salary", true);

            double hra = parseAmount(txtHra.getText(), "HRA", false);
            double da = parseAmount(txtDa.getText(), "DA", false);
            double ta = parseAmount(txtTa.getText(), "TA", false);
            double[] allowances = {hra, da, ta};

            double pf = parseAmount(txtPf.getText(), "PF", false);
            double tax = parseAmount(txtTax.getText(), "Tax", false);
            double[] deductions = {pf, tax};

            PayrollEmployee updatedEmp = new PayrollEmployee(id, name, department, designation, basicSalary, allowances, deductions);

            boolean updated = manager.updateEmployee(updatedEmp);
            if (updated) {
                refreshTable(manager.getAllEmployees());
                JOptionPane.showMessageDialog(this,
                        "Employee ID " + id + " updated successfully!\nUpdated Net Salary: ₹ " + String.format("%.2f", updatedEmp.calculateNetSalary()),
                        "Update Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: Employee with ID " + id + " could not be updated.",
                        "Update Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // deletes employee by id
    private void handleDelete() {
        try {
            int targetId = -1;

            if (!txtId.getText().trim().isEmpty()) {
                targetId = parseId(txtId.getText());
            } else {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                    targetId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                } else {
                    String input = JOptionPane.showInputDialog(this, "Enter Employee ID to delete:");
                    if (input == null || input.trim().isEmpty()) {
                        return;
                    }
                    targetId = parseId(input);
                }
            }

            PayrollEmployee target = manager.searchById(targetId);
            if (target == null) {
                JOptionPane.showMessageDialog(this,
                        "Error: Employee with ID " + targetId + " does not exist!\nNothing to delete.",
                        "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete employee:\n"
                            + target.getName() + " (ID: " + targetId + ", Dept: " + target.getDepartment() + ")?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteEmployee(targetId);
                refreshTable(manager.getAllEmployees());
                clearForm();
                JOptionPane.showMessageDialog(this, "Employee ID " + targetId + " deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during deletion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // searches employee using hashmap
    private void handleSearch() {
        try {
            String input = JOptionPane.showInputDialog(this, "Enter Employee ID to search (using HashMap):");
            if (input == null || input.trim().isEmpty()) {
                return;
            }

            int id = parseId(input);
            PayrollEmployee emp = manager.searchById(id);

            if (emp != null) {
                populateForm(emp);

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (Integer.parseInt(tableModel.getValueAt(i, 0).toString()) == id) {
                        employeeTable.setRowSelectionInterval(i, i);
                        break;
                    }
                }

                JOptionPane.showMessageDialog(this,
                        "Employee Found via HashMap O(1) Search!\n\n"
                                + "ID: " + emp.getId() + "\n"
                                + "Name: " + emp.getName() + "\n"
                                + "Department: " + emp.getDepartment() + "\n"
                                + "Designation: " + emp.getDesignation() + "\n"
                                + "Net Take-Home Salary: ₹ " + String.format("%.2f", emp.calculateNetSalary()),
                        "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No employee found with ID " + id + ".", "Record Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // displays formatted payslip
    private void handlePayslip() {
        try {
            int targetId = -1;

            if (!txtId.getText().trim().isEmpty()) {
                targetId = parseId(txtId.getText());
            } else {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < tableModel.getRowCount()) {
                    targetId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                } else {
                    String input = JOptionPane.showInputDialog(this, "Enter Employee ID to generate Payslip:");
                    if (input == null || input.trim().isEmpty()) {
                        return;
                    }
                    targetId = parseId(input);
                }
            }

            PayrollEmployee emp = manager.searchById(targetId);
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "Employee with ID " + targetId + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double[] allow = emp.getAllowances();
            double[] deduct = emp.getDeductions();

            String slip = "=================================================\n"
                    + "                 OFFICIAL SALARY PAYSLIP         \n"
                    + "=================================================\n"
                    + " Employee ID   : " + emp.getId() + "\n"
                    + " Employee Name : " + emp.getName() + "\n"
                    + " Department    : " + emp.getDepartment() + "\n"
                    + " Designation   : " + emp.getDesignation() + "\n"
                    + "-------------------------------------------------\n"
                    + " EARNINGS / ALLOWANCES:\n"
                    + "   Basic Salary     : ₹ " + String.format("%.2f", emp.getBasicSalary()) + "\n"
                    + "   HRA (House Rent) : ₹ " + String.format("%.2f", allow[0]) + "\n"
                    + "   DA  (Dearness)   : ₹ " + String.format("%.2f", allow[1]) + "\n"
                    + "   TA  (Travel)     : ₹ " + String.format("%.2f", allow[2]) + "\n"
                    + "   Total Allowances : ₹ " + String.format("%.2f", emp.calculateTotalAllowances()) + "\n"
                    + "   GROSS SALARY     : ₹ " + String.format("%.2f", emp.calculateGrossSalary()) + "\n"
                    + "-------------------------------------------------\n"
                    + " DEDUCTIONS:\n"
                    + "   PF (Provident)   : ₹ " + String.format("%.2f", deduct[0]) + "\n"
                    + "   Income Tax       : ₹ " + String.format("%.2f", deduct[1]) + "\n"
                    + "   Total Deductions : ₹ " + String.format("%.2f", emp.calculateTotalDeductions()) + "\n"
                    + "=================================================\n"
                    + "   NET TAKE-HOME SALARY : ₹ " + String.format("%.2f", emp.calculateNetSalary()) + "\n"
                    + "=================================================\n";

            JTextArea area = new JTextArea(slip);
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setEditable(false);

            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(480, 420));
            JOptionPane.showMessageDialog(this, scroll, "Salary Payslip - " + emp.getName(), JOptionPane.PLAIN_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating payslip: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // populates form fields with employee data
    private void populateForm(PayrollEmployee emp) {
        if (emp == null) return;
        txtId.setText(String.valueOf(emp.getId()));
        txtName.setText(emp.getName());
        cbDepartment.setSelectedItem(emp.getDepartment());
        txtDesignation.setText(emp.getDesignation());
        txtBasicSalary.setText(String.format("%.2f", emp.getBasicSalary()));

        double[] allow = emp.getAllowances();
        txtHra.setText(String.format("%.2f", allow[0]));
        txtDa.setText(String.format("%.2f", allow[1]));
        txtTa.setText(String.format("%.2f", allow[2]));

        double[] deduct = emp.getDeductions();
        txtPf.setText(String.format("%.2f", deduct[0]));
        txtTax.setText(String.format("%.2f", deduct[1]));
    }

    // clears all form fields
    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        cbDepartment.setSelectedIndex(0);
        txtDesignation.setText("");
        txtBasicSalary.setText("");
        txtHra.setText("0");
        txtDa.setText("0");
        txtTa.setText("0");
        txtPf.setText("0");
        txtTax.setText("0");
        employeeTable.clearSelection();
    }

    // refreshes records table
    private void refreshTable(Collection<PayrollEmployee> employees) {
        tableModel.setRowCount(0);
        if (employees != null) {
            for (PayrollEmployee emp : employees) {
                if (emp != null) {
                    Object[] row = {
                            emp.getId(),
                            emp.getName(),
                            emp.getDepartment(),
                            emp.getDesignation(),
                            String.format("%.2f", emp.getBasicSalary()),
                            String.format("%.2f", emp.calculateTotalAllowances()),
                            String.format("%.2f", emp.calculateTotalDeductions()),
                            String.format("%.2f", emp.calculateNetSalary())
                    };
                    tableModel.addRow(row);
                }
            }
        }

        lblSummary.setText("Total Employees: " + manager.getEmployeeCount()
                + " | Total Payroll Payout: ₹ " + String.format("%.2f", manager.calculateTotalPayroll()));
    }

    // main launcher
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PayrollGUI().setVisible(true);
            }
        });
    }
}
