import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    private JLabel fileLabel;
    private JTextArea outputArea;

    private JButton loadTextButton;
    private JButton loadStopWordsButton;
    private JButton extractButton;
    private JButton saveButton;
    private JButton quitButton;

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

        // Wire up actions (currently empty placeholders)
        loadTextButton.addActionListener(e -> {});
        loadStopWordsButton.addActionListener(e -> {});
        extractButton.addActionListener(e -> {});
        saveButton.addActionListener(e -> {});
        quitButton.addActionListener(e -> System.exit(0));

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}