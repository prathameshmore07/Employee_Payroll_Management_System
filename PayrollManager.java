import java.util.LinkedList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collection;

public class PayrollManager {
    private LinkedList<PayrollEmployee> employeeList; // maintains records in insertion order
    private HashMap<Integer, PayrollEmployee> employeeMap; // fast O(1) search by employee id
    private TreeMap<Integer, PayrollEmployee> sortedEmployeeMap; // maintains records sorted by id

    // constructor
    public PayrollManager() {
        employeeList = new LinkedList<>();
        employeeMap = new HashMap<>();
        sortedEmployeeMap = new TreeMap<>();
    }

    // adds employee to list and maps
    public boolean addEmployee(PayrollEmployee emp) {
        if (emp == null || employeeMap.containsKey(emp.getId())) {
            return false;
        }
        employeeList.add(emp);
        employeeMap.put(emp.getId(), emp);
        sortedEmployeeMap.put(emp.getId(), emp);
        return true;
    }

    // search employee by id
    public PayrollEmployee searchById(int id) {
        return employeeMap.get(id);
    }

    // updates employee details
    public boolean updateEmployee(PayrollEmployee updatedEmp) {
        if (updatedEmp == null || !employeeMap.containsKey(updatedEmp.getId())) {
            return false;
        }

        int id = updatedEmp.getId();
        employeeMap.put(id, updatedEmp);
        sortedEmployeeMap.put(id, updatedEmp);

        for (int i = 0; i < employeeList.size(); i++) {
            PayrollEmployee current = employeeList.get(i);
            if (current != null && current.getId() == id) {
                employeeList.set(i, updatedEmp);
                break;
            }
        }
        return true;
    }

    // deletes employee by id
    public boolean deleteEmployee(int id) {
        if (!employeeMap.containsKey(id)) {
            return false;
        }

        employeeMap.remove(id);
        sortedEmployeeMap.remove(id);

        for (int i = 0; i < employeeList.size(); i++) {
            PayrollEmployee current = employeeList.get(i);
            if (current != null && current.getId() == id) {
                employeeList.remove(i);
                break;
            }
        }
        return true;
    }

    // returns all employees from linkedlist
    public LinkedList<PayrollEmployee> getAllEmployees() {
        return employeeList;
    }

    // returns sorted employees from treemap
    public Collection<PayrollEmployee> getSortedEmployees() {
        return sortedEmployeeMap.values();
    }

    // calculates total payroll amount
    public double calculateTotalPayroll() {
        double total = 0.0;
        for (PayrollEmployee emp : employeeList) {
            if (emp != null) {
                total += emp.calculateNetSalary();
            }
        }
        return total;
    }

    // returns total employee count
    public int getEmployeeCount() {
        return employeeList.size();
    }
}
