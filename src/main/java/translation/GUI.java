package translation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            Translator translator = new JSONTranslator();

            Map<String, String> nameToAlpha3 = new HashMap<>();
            Map<String, String> langNameToCode = new HashMap<>();

            DefaultListModel<String> countryModel = new DefaultListModel<>();
            for (String a3 : translator.getCountryCodes()) {
                String name = countryConverter.fromCountryCode(a3);
                if (name == null || name.isEmpty()) name = a3;
                if (!nameToAlpha3.containsKey(name)) {
                    countryModel.addElement(name);
                    nameToAlpha3.put(name, a3);
                }
            }
            JList<String> countryList = new JList<>(countryModel);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countryList.setVisibleRowCount(10);
            JScrollPane countryScroll = new JScrollPane(countryList);

            JPanel countryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryScroll);

            DefaultComboBoxModel<String> langModel = new DefaultComboBoxModel<>();
            for (String code : translator.getLanguageCodes()) {
                String langName = languageConverter.fromLanguageCode(code);
                if (langName == null || langName.isEmpty()) langName = code;
                langModel.addElement(langName);
                langNameToCode.put(langName, code);
            }
            JComboBox<String> languageCombo = new JComboBox<>(langModel);

            JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageCombo);

            JLabel resultLabel = new JLabel(" ");
            resultLabel.setPreferredSize(new Dimension(300, 20));

            JButton submit = new JButton("Submit");
            submit.addActionListener(e -> {
                String countryName = countryList.getSelectedValue();
                String languageName = (String) languageCombo.getSelectedItem();

                if (countryName == null || languageName == null) {
                    resultLabel.setText("Pick a country & a language");
                    return;
                }

                String alpha3   = nameToAlpha3.get(countryName);
                String langCode = langNameToCode.get(languageName);

                String res = null;
                if (alpha3 != null && langCode != null) {
                    res = translator.translate(alpha3, langCode);
                }
                resultLabel.setText((res != null && !res.isEmpty()) ? res : "no translation found!");
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(new JLabel("Translation:"));
            buttonPanel.add(resultLabel);
            buttonPanel.add(submit);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
