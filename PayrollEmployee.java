public class PayrollEmployee {
    private int id; // employee id
    private String name; // employee name
    private String department; // department name
    private String designation; // job role
    private double basicSalary; // basic pay
    private double[] allowances; // allowances array: hra, da, ta
    private double[] deductions; // deductions array: pf, tax

    // constructor
    public PayrollEmployee(int id, String name, String department, String designation,
                           double basicSalary, double[] allowances, double[] deductions) {
        this.id = id;
        this.name = (name != null) ? name.trim() : "Unknown";
        this.department = (department != null) ? department.trim() : "General";
        this.designation = (designation != null) ? designation.trim() : "Staff";
        this.basicSalary = Math.max(0.0, basicSalary);

        // store allowances
        this.allowances = new double[3];
        if (allowances != null) {
            for (int i = 0; i < Math.min(allowances.length, 3); i++) {
                this.allowances[i] = Math.max(0.0, allowances[i]);
            }
        }

        // store deductions
        this.deductions = new double[2];
        if (deductions != null) {
            for (int i = 0; i < Math.min(deductions.length, 2); i++) {
                this.deductions[i] = Math.max(0.0, deductions[i]);
            }
        }
    }

    // calculates total allowances
    public double calculateTotalAllowances() {
        double total = 0.0;
        if (allowances != null) {
            for (int i = 0; i < allowances.length; i++) {
                total += allowances[i];
            }
        }
        return total;
    }

    // calculates total deductions
    public double calculateTotalDeductions() {
        double total = 0.0;
        if (deductions != null) {
            for (int i = 0; i < deductions.length; i++) {
                total += deductions[i];
            }
        }
        return total;
    }

    // calculates gross salary
    public double calculateGrossSalary() {
        return basicSalary + calculateTotalAllowances();
    }

    // calculates net salary
    public double calculateNetSalary() {
        double net = calculateGrossSalary() - calculateTotalDeductions();
        return Math.max(0.0, net);
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : "";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = (department != null) ? department.trim() : "General";
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = (designation != null) ? designation.trim() : "Staff";
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = Math.max(0.0, basicSalary);
    }

    public double[] getAllowances() {
        return allowances;
    }

    public void setAllowances(double[] allowances) {
        this.allowances = new double[3];
        if (allowances != null) {
            for (int i = 0; i < Math.min(allowances.length, 3); i++) {
                this.allowances[i] = Math.max(0.0, allowances[i]);
            }
        }
    }

    public double[] getDeductions() {
        return deductions;
    }

    public void setDeductions(double[] deductions) {
        this.deductions = new double[2];
        if (deductions != null) {
            for (int i = 0; i < Math.min(deductions.length, 2); i++) {
                this.deductions[i] = Math.max(0.0, deductions[i]);
            }
        }
    }
}
