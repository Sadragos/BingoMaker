import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.catalog.Catalog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.*;
import java.util.List;

public class Start {
    private JButton selectFilesButton;
    private JLabel selectedFilesLabel;
    private JButton createButton;
    private JPanel MainPanel;
    private JTextField fields;
    private JTextField rows;
    private JTextField cols;
    private JTextField pictureSize;
    private JCheckBox cubed;

    ArrayList<File> files = new ArrayList<File>();

    public Start() {
        selectFilesButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase(Locale.ROOT).endsWith("png") || f.getName().toLowerCase(Locale.ROOT).endsWith(".jpg");
                    }

                    @Override
                    public String getDescription() {
                        return "Bilder";
                    }
                });
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        filesSelected(Arrays.asList(fileChooser.getSelectedFiles()));
                    }
                });
                fileChooser.showDialog(null, "Auswählen");
            }
        });


        createButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {

                    HashSet<String> usedPics = new HashSet<>();
                    File outputFile = new File("bingo.html");
                    URI outputPath = outputFile.toURI();

                    ArrayList<File> images = new ArrayList<>(files);

                    StringBuilder sb = new StringBuilder();
                    sb.append("<html><head><title>Bingo Bingo</title></head><body>");
                    for (int i = 0; i < getCopies(); i++) {
                        int img = 0;
                        Collections.shuffle(images);
                        sb.append("<table style=\"border-collapse: collapse; page-break-after: always;\">");
                        for (int row = 0; row < getRows(); row++) {
                            sb.append("<tr>");
                            for (int col = 0; col < getCols(); col++) {
                                File pic = images.get(img);
                                usedPics.add(pic.getName().substring(0, pic.getName().lastIndexOf(".")));
                                URI picPath = pic.toURI();

                                sb.append("<td style=\"border: 2px solid black; padding: 2mm;\">");
                                sb.append("<img src=\"");
                                sb.append(outputPath.relativize(picPath).getPath());
                                sb.append("\" style=\"width: ");
                                sb.append(getImageSize());
                                sb.append("cm;");
                                if(cubed.isSelected()) {
                                    sb.append(" height: ");
                                    sb.append(getImageSize());
                                    sb.append("cm;");
                                }
                                sb.append("\">");
                                sb.append("</td>");

                                if(++img >= images.size()) {
                                    img = 0;
                                    Collections.shuffle(images);
                                }
                            }
                            sb.append("</tr>");
                        }

                        sb.append("</table><br><br>");
                    }
                    sb.append("<b>Verwendete Bilder</b>");
                    sb.append("<ol>");
                    for(String s : usedPics) {
                        sb.append("<li>");
                        sb.append(s);
                        sb.append("</li>");
                    }
                    sb.append("</ol>");
                    sb.append("</body></html>");

                    FileWriter fileWriter = new FileWriter(outputFile);
                    fileWriter.write(sb.toString());
                    fileWriter.flush();
                    fileWriter.close();

                    Desktop.getDesktop().open(outputFile);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });
    }

    private void filesSelected(List<File> files) {
        this.files.clear();
        this.files.addAll(files);
        this.selectedFilesLabel.setText(files.size() + " Bilder ausgewählt.");
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public int getRows() {
        try {
            return Integer.parseInt(this.rows.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public int getCols() {
        try {
            return Integer.parseInt(this.cols.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public int getCopies() {
        try {
            return Integer.parseInt(this.fields.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public int getImageSize() {
        try {
            return Integer.parseInt(this.pictureSize.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
