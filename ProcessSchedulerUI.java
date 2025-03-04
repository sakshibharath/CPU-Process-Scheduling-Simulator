import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;

class Process {
    int pid, arrival, burst, completion, turnaround, waiting;

    public Process(int pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
    }
}

public class ProcessSchedulerUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField arrivalField, burstField;
    private ArrayList<Process> processes = new ArrayList<>();
    private int pidCounter = 1;

    public ProcessSchedulerUI() {
        frame = new JFrame("CPU Process Scheduler");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table Setup
        String[] columnNames = {"PID", "Arrival Time", "Burst Time", "Completion", "Turnaround", "Waiting"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalField = new JTextField();
        inputPanel.add(arrivalField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstField = new JTextField();
        inputPanel.add(burstField);

        JButton addButton = new JButton("Add Process");
        inputPanel.add(addButton);
        frame.add(inputPanel, BorderLayout.NORTH);

        // Bottom Panel (Run Button + Clear Button)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton runFCFS = new JButton("Run FCFS");
        JButton clearButton = new JButton("Clear Table");
        bottomPanel.add(runFCFS);
        bottomPanel.add(clearButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener((ActionEvent e) -> addProcess());
        runFCFS.addActionListener((ActionEvent e) -> runFCFSAlgorithm());
        clearButton.addActionListener((ActionEvent e) -> clearTable());

        frame.setVisible(true);
    }

    private void addProcess() {
        try {
            int arrival = Integer.parseInt(arrivalField.getText().trim());
            int burst = Integer.parseInt(burstField.getText().trim());

            if (arrival < 0 || burst <= 0) {
                JOptionPane.showMessageDialog(frame, "Invalid input! Arrival and Burst time must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            processes.add(new Process(pidCounter++, arrival, burst));
            arrivalField.setText("");
            burstField.setText("");
            updateTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runFCFSAlgorithm() {
        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No processes added! Please add processes first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        processes.sort(Comparator.comparingInt(p -> p.arrival));
        int time = 0;

        for (Process p : processes) {
            if (time < p.arrival) {
                time = p.arrival;
            }
            p.completion = time + p.burst;
            p.turnaround = p.completion - p.arrival;
            p.waiting = p.turnaround - p.burst;
            time = p.completion;
        }
        updateTable();
    }

    private void clearTable() {
        processes.clear();
        pidCounter = 1;
        updateTable();
    }

    private void updateTable() {
        model.setRowCount(0);
        for (Process p : processes) {
            model.addRow(new Object[]{p.pid, p.arrival, p.burst, p.completion, p.turnaround, p.waiting});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessSchedulerUI::new);
    }
}
