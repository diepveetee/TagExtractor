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

    private void loadStopWordsFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            Path stopPath = chooser.getSelectedFile().toPath();
            stopWords.clear();
            try {
                List<String> lines = Files.readAllLines(stopPath);
                for (String line : lines) {
                    String word = line.trim().toLowerCase();
                    if (!word.isEmpty()) {
                        stopWords.add(word);
                    }
                }
                JOptionPane.showMessageDialog(this, "Loaded " + stopWords.size() + " stop words.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading stop words: " + ex.getMessage());
            }
        }
    }

    private void runExtraction() {
        if (textFilePath == null) {
            JOptionPane.showMessageDialog(this, "Please load a text file first.");
            return;
        }
        if (stopWords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please load a stop words file first.");
            return;
        }

        tagCounts.clear();

        try {
            List<String> lines = Files.readAllLines(textFilePath);
            for (String line : lines) {
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {
                    String word = token.replaceAll("[^a-zA-Z]", "").toLowerCase();
                    if (word.isEmpty()) continue;
                    if (stopWords.contains(word)) continue;

                    tagCounts.merge(word, 1, Integer::sum);
                }
            }

            // Build output
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                sb.append(entry.getKey())
                        .append(" : ")
                        .append(entry.getValue())
                        .append("\n");
            }
            outputArea.setText(sb.toString());

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading text file: " + ex.getMessage());
        }
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
