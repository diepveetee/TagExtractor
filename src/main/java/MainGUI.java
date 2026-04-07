import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class MainGUI extends JFrame {

    private JLabel fileLabel;
    private JTextArea outputArea;

    private JButton loadTextButton;
    private JButton loadStopWordsButton;
    private JButton extractButton;
    private JButton saveButton;
    private JButton quitButton;

    private Path textFilePath;
    private Set<String> stopWords = new TreeSet<>();
    private Map<String, Integer> tagCounts = new HashMap<>();

    public MainGUI() {
        super("Tag / Keyword Extractor");

        // Top: file label
        fileLabel = new JLabel("No text file selected");

        // Center: output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Bottom: buttons
        loadTextButton = new JButton("Load Text File");
        loadStopWordsButton = new JButton("Load Stop Words");
        extractButton = new JButton("Run Extraction");
        saveButton = new JButton("Save Results");
        quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadTextButton);
        buttonPanel.add(loadStopWordsButton);
        buttonPanel.add(extractButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        setLayout(new BorderLayout());
        add(fileLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Wire up actions
        loadTextButton.addActionListener(e -> loadTextFile());
        loadStopWordsButton.addActionListener(e -> loadStopWordsFile());
        extractButton.addActionListener(e -> runExtraction());
        saveButton.addActionListener(e -> saveResults());
        quitButton.addActionListener(e -> System.exit(0));

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadTextFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            textFilePath = chooser.getSelectedFile().toPath();
            fileLabel.setText("Text file: " + textFilePath.getFileName());
        }
    }

    private void loadStopWordsFile{

    }

    private void runExtraction{

    }


    private void saveResults() {
        if (tagCounts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No results to save. Run extraction first.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File outFile = chooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(new FileWriter(outFile))) {
                for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                    out.println(entry.getKey() + " : " + entry.getValue());
                }
                JOptionPane.showMessageDialog(this, "Results saved.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
