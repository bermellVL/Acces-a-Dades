package evaluable;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.awt.Color;
import javax.swing.JScrollPane;

public class evaluable01 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtBuscarString;
    private JTextField textCanviarString;
    private JLabel lblResultat;
    private JTextField textBuscarDirectori;
    private JCheckBox chckbxMajusculesMinuscules;
    private JCheckBox chckbxAccents;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    evaluable01 frame = new evaluable01();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public evaluable01() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 804, 528);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 128, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        txtBuscarString = new JTextField();
        txtBuscarString.setBounds(76, 45, 459, 27);
        contentPane.add(txtBuscarString);
        txtBuscarString.setColumns(10);

        JButton btnBuscarDirectori = new JButton("Buscar directori");
        btnBuscarDirectori.setBackground(new Color(255, 255, 255));
        btnBuscarDirectori.setBounds(545, 18, 235, 27);
        contentPane.add(btnBuscarDirectori);

        JButton btnBuscarString = new JButton("Buscar String");
        btnBuscarString.setBounds(545, 59, 235, 36);
        contentPane.add(btnBuscarString);

        chckbxMajusculesMinuscules = new JCheckBox("Respectar Majúscules i Minúscules");
        chckbxMajusculesMinuscules.setBounds(76, 78, 230, 21);
        contentPane.add(chckbxMajusculesMinuscules);

        chckbxAccents = new JCheckBox("Respectar accents");
        chckbxAccents.setBounds(308, 78, 227, 21);
        contentPane.add(chckbxAccents);

        textCanviarString = new JTextField();
        textCanviarString.setColumns(10);
        textCanviarString.setBounds(76, 105, 459, 27);
        contentPane.add(textCanviarString);

        JButton btnCanviarString = new JButton("Canviar String");
        btnCanviarString.setBounds(545, 105, 235, 26);
        contentPane.add(btnCanviarString);
        
        textBuscarDirectori = new JTextField();
        textBuscarDirectori.setBounds(76, 18, 459, 23);
        contentPane.add(textBuscarDirectori);
        textBuscarDirectori.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 164, 760, 317);
        contentPane.add(scrollPane);
        
        lblResultat = new JLabel("Resultats del Directori");
        scrollPane.setViewportView(lblResultat);
        
        btnBuscarDirectori.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directoriPath = textBuscarDirectori.getText();
                File directori = new File(directoriPath);
                if (directori.exists() && directori.isDirectory()) {
                    StringBuilder contingut = new StringBuilder();
                    llistarDirectori(directori, contingut, 0);
                    lblResultat.setText("<html>" + contingut.toString().replace("\n", "<br>") + "</html>");
                } else {
                    lblResultat.setText("Directori no trobat.");
                }
            }
        });

        btnBuscarString.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directoriPath = textBuscarDirectori.getText();
                File directori = new File(directoriPath);
                String cadenaCerca = txtBuscarString.getText();
                if (directori.exists() && directori.isDirectory()) {
                    StringBuilder resultat = new StringBuilder();
                    buscarStringEnDirectori(directori, cadenaCerca, resultat);
                    lblResultat.setText("<html>" + resultat.toString().replace("\n", "<br>") + "</html>");
                } else {
                    lblResultat.setText("Directori no trobat.");
                }
            }
        });

        btnCanviarString.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String directoriPath = textBuscarDirectori.getText();
                File directori = new File(directoriPath);
                String cadenaCerca = txtBuscarString.getText();
                String cadenaReemplaç = textCanviarString.getText();
                if (directori.exists() && directori.isDirectory()) {
                    StringBuilder resultat = new StringBuilder();
                    reemplaçarStringEnDirectori(directori, cadenaCerca, cadenaReemplaç, resultat);
                    lblResultat.setText("<html>" + resultat.toString().replace("\n", "<br>") + "</html>");
                } else {
                    lblResultat.setText("Directori no trobat.");
                }
            }
        });
    }
    
    /**
     * Llista de manera recursiva el contingut d'un directori, incloent subdirectoris i fitxers,
     * i afegeix la informació al StringBuilder proporcionat amb format jeràrquic.
     *
     * @param dir El directori a llistar.
     * @param contingut L'StringBuilder on s'afegirà la llista del directori.
     * @param nivell El nivell de profunditat actual dins l'estructura del directori (per a formatació).
     */
    private void llistarDirectori(File dir, StringBuilder contingut, int nivell) {
        File[] arxius = dir.listFiles();
        if (arxius != null) {
            for (File arxiu : arxius) {
                for (int i = 0; i < nivell; i++) {
                    contingut.append("|   ");
                }
                if (arxiu.isDirectory()) {
                    contingut.append("|-- \\").append(arxiu.getName()).append("\n");
                    llistarDirectori(arxiu, contingut, nivell + 1);
                } else {
                    contingut.append("|-- ").append(arxiu.getName());
                    contingut.append(" (").append(formatSize(arxiu.length())).append(" - ");
                    contingut.append(formatDate(arxiu.lastModified())).append(")\n");
                }
            }
        }
    }

    /**
     * Converteix la mida d'un fitxer de bytes a kilobytes amb una precisió d'una xifra decimal.
     *
     * @param size La mida en bytes.
     * @return Una cadena que representa la mida en kilobytes (KB).
     */
    private String formatSize(long size) {
        double kbSize = size / 1024.0;
        return String.format("%.1f KB", kbSize);
    }

    /**
     * Formata una data en mil·lisegons a un format de data comprensible (dd/MM/yyyy HH:mm:ss).
     *
     * @param millis Temps en mil·lisegons des de l'època.
     * @return Una cadena que representa la data i hora formatades.
     */
    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(millis);
    }

    /**
     * Cerca de manera recursiva una cadena dins de tots els fitxers d'un directori.
     * Si la cadena es troba en un fitxer, es registra el nombre de coincidències.
     *
     * @param dir El directori on es farà la cerca.
     * @param cadenaCerca La cadena a cercar dins dels fitxers.
     * @param resultat L'StringBuilder on s'afegirà el nom dels fitxers i el nombre de coincidències.
     */
    private void buscarStringEnDirectori(File dir, String cadenaCerca, StringBuilder resultat) {
        File[] arxius = dir.listFiles();
        if (arxius != null) {
            for (File arxiu : arxius) {
                if (arxiu.isDirectory()) {
                    buscarStringEnDirectori(arxiu, cadenaCerca, resultat);
                } else {
                    int coincidencies = cercarEnArxiu(arxiu, cadenaCerca);
                    resultat.append(arxiu.getName()).append(" (").append(coincidencies).append(" coincidències)\n");
                }
            }
        }
    }

    /**
     * Cerca una cadena dins d'un fitxer i retorna el nombre de coincidències trobades.
     *
     * @param arxiu El fitxer on es farà la cerca.
     * @param cadenaCerca La cadena que es vol cercar.
     * @return El nombre de coincidències trobades.
     */
    private int cercarEnArxiu(File arxiu, String cadenaCerca) {
        int coincidencies = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(arxiu))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                String liniaAComparar = linia;
                String cadenaAComparar = cadenaCerca;
               
                if (!chckbxMajusculesMinuscules.isSelected()) {
                    liniaAComparar = liniaAComparar.toLowerCase();
                    cadenaAComparar = cadenaAComparar.toLowerCase();
                }
                
                if (!chckbxAccents.isSelected()) {
                    liniaAComparar = eliminarAccents(liniaAComparar);
                    cadenaAComparar = eliminarAccents(cadenaAComparar);
                }
               
                int index = 0;
                while ((index = liniaAComparar.indexOf(cadenaAComparar, index)) != -1) {
                    coincidencies++;
                    index += cadenaAComparar.length();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coincidencies;
    }

    /**
     * Elimina els accents d'una cadena de text utilitzant la normalització Unicode.
     *
     * @param text El text del qual es volen eliminar els accents.
     * @return El text sense accents.
     */
    private String eliminarAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                         .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    /**
     * Reemplaça una cadena dins de tots els fitxers d'un directori per una altra cadena.
     * El nombre de reemplaços es registra per a cada fitxer.
     *
     * @param dir El directori on es farà la substitució.
     * @param cadenaCerca La cadena a cercar dins dels fitxers.
     * @param cadenaReemplaç La cadena per la qual es vol reemplaçar.
     * @param resultat L'StringBuilder on s'afegirà el nom dels fitxers i el nombre de reemplaços.
     */
    private void reemplaçarStringEnDirectori(File dir, String cadenaCerca, String cadenaReemplaç, StringBuilder resultat) {
        File[] arxius = dir.listFiles();
        if (arxius != null) {
            for (File arxiu : arxius) {
                if (arxiu.isDirectory()) {
                    reemplaçarStringEnDirectori(arxiu, cadenaCerca, cadenaReemplaç, resultat);
                } else {
                    int reemplaços = reemplaçarCadenaEnArxiu(arxiu, cadenaCerca, cadenaReemplaç);
                    resultat.append(arxiu.getName()).append(" (").append(reemplaços).append(" reemplaços)\n");
                }
            }
        }
    }

    /**
     * Cerca i reemplaça una cadena dins d'un fitxer i retorna el nombre de reemplaços fets.
     *
     * @param arxiu El fitxer on es farà la substitució.
     * @param cadenaCerca La cadena a cercar.
     * @param cadenaReemplaç La cadena amb què es reemplaçarà la cadena cercada.
     * @return El nombre de reemplaços realitzats.
     */
    private int reemplaçarCadenaEnArxiu(File arxiu, String cadenaCerca, String cadenaReemplaç) {
        int reemplaços = 0;
        StringBuilder nouContingut = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arxiu))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                String liniaAComparar = linia;
                if (!chckbxMajusculesMinuscules.isSelected()) {
                    liniaAComparar = liniaAComparar.toLowerCase();
                    cadenaCerca = cadenaCerca.toLowerCase();
                }
                if (!chckbxAccents.isSelected()) {
                    liniaAComparar = eliminarAccents(liniaAComparar);
                    cadenaCerca = eliminarAccents(cadenaCerca);
                }
                int index = 0;
                while ((index = liniaAComparar.indexOf(cadenaCerca, index)) != -1) {
                    linia = linia.substring(0, index) + cadenaReemplaç + linia.substring(index + cadenaCerca.length());
                    reemplaços++;
                    index += cadenaReemplaç.length();
                }
                nouContingut.append(linia).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        File nouArxiu = new File(arxiu.getParent(), "MOD_" + arxiu.getName());
        try (FileWriter writer = new FileWriter(nouArxiu)) {
            writer.write(nouContingut.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reemplaços;
    }
}