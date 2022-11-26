import dto.Country;
import dto.City;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class App extends JFrame {
    private static JFrame frame;

    private static Client client;

    private static Country currentCountry = null;
    private static City currentCity = null;

    private static boolean editMode = false;
    private static boolean countryMode = true;

    private static final JButton btnAddCountry = new JButton("Add Country");
    private static final JButton btnAddCity = new JButton("Add City");
    private static final JButton btnEdit = new JButton("Edit City");
    private static final JButton btnBack = new JButton("Back");
    private static final JButton btnSave = new JButton("Save");
    private static final JButton btnDelete = new JButton("Delete");

    private static final Box menuPanel = Box.createVerticalBox();
    private static final Box actionPanel = Box.createVerticalBox();
    private static final Box comboPanel = Box.createVerticalBox();
    private static final Box cityPanel = Box.createVerticalBox();
    private static final Box countryPanel = Box.createVerticalBox();

    private static final JComboBox comboCountry = new JComboBox();
    private static final JComboBox comboCity = new JComboBox();

    private static final JTextField textCountryName = new JTextField(30);
    private static final JTextField textCityName = new JTextField(30);
    private static final JTextField textCityCapitalNem = new JTextField(30);
    private static final JTextField textCityPopulation = new JTextField(30);

    private App() {
        super("Library");
        frame = this;
        frame.setPreferredSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                client.disconnect();
                System.exit(0);
            }
        });
        Box box = Box.createVerticalBox();
        sizeAllElements();
        frame.setLayout(new FlowLayout());

        client.cleanMessages();

        // Menu
        menuPanel.add(btnAddCountry);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddCountry.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                countryMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                countryPanel.setVisible(true);
                cityPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnAddCity);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddCity.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                countryMode = false;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                countryPanel.setVisible(false);
                cityPanel.setVisible(true);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnEdit);
        menuPanel.add(Box.createVerticalStrut(20));
        btnEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(true);
                countryPanel.setVisible(false);
                cityPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });

        // ComboBoxes
        comboPanel.add(new JLabel("Country:"));
        comboPanel.add(comboCountry);
        comboPanel.add(Box.createVerticalStrut(20));
        comboCountry.addActionListener(e -> {
            String name = (String) comboCountry.getSelectedItem();
            currentCountry = client.capitalFindByName(name);
            countryMode = true;
            countryPanel.setVisible(true);
            cityPanel.setVisible(false);
            fillCountriesFields();
            pack();
        });
        comboPanel.add(new JLabel("City:"));
        comboPanel.add(comboCity);
        comboPanel.add(Box.createVerticalStrut(20));
        fillComboBoxes();
        comboPanel.setVisible(false);

        cityPanel.add(new JLabel("Name:"));
        cityPanel.add(textCityName);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Country Name:"));
        cityPanel.add(textCityCapitalNem);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.add(new JLabel("Release year:"));
        cityPanel.add(textCityPopulation);
        cityPanel.add(Box.createVerticalStrut(20));
        cityPanel.setVisible(false);

        countryPanel.add(new JLabel("Name:"));
        countryPanel.add(textCountryName);
        countryPanel.add(Box.createVerticalStrut(20));
        countryPanel.setVisible(false);

        actionPanel.add(btnSave);
        actionPanel.add(Box.createVerticalStrut(20));
        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                save();
            }
        });
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalStrut(20));
        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                delete();
            }
        });
        actionPanel.add(btnBack);
        actionPanel.add(Box.createVerticalStrut(20));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                clearFields();
                menuPanel.setVisible(true);
                comboPanel.setVisible(false);
                countryPanel.setVisible(false);
                cityPanel.setVisible(false);
                actionPanel.setVisible(false);
                pack();
            }
        });
        actionPanel.setVisible(false);

        clearFields();
        box.setPreferredSize(new Dimension(300, 500));
        box.add(menuPanel);
        box.add(comboPanel);
        box.add(countryPanel);
        box.add(cityPanel);
        box.add(actionPanel);
        setContentPane(box);
        pack();
    }

    private static void sizeAllElements() {
        Dimension dimension = new Dimension(300, 50);
        btnAddCountry.setMaximumSize(dimension);
        btnAddCity.setMaximumSize(dimension);
        btnEdit.setMaximumSize(dimension);
        btnBack.setMaximumSize(dimension);
        btnSave.setMaximumSize(dimension);
        btnDelete.setMaximumSize(dimension);

        btnAddCountry.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddCity.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension panelDimension = new Dimension(300, 300);
        menuPanel.setMaximumSize(panelDimension);
        comboPanel.setPreferredSize(panelDimension);
        actionPanel.setPreferredSize(panelDimension);
        cityPanel.setPreferredSize(panelDimension);
        countryPanel.setPreferredSize(panelDimension);

        comboCountry.setPreferredSize(dimension);
        comboCity.setPreferredSize(dimension);

        textCityCapitalNem.setPreferredSize(dimension);
        textCityName.setPreferredSize(dimension);
        textCityPopulation.setPreferredSize(dimension);
        textCountryName.setPreferredSize(dimension);
    }

    private static void save() {
        if (editMode) {
            if (countryMode) {
                currentCountry.setName(textCountryName.getText());
                if (client.CountryUpdate(currentCountry)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            } else {
                currentCity.setName(textCityName.getText());
                currentCity.setPopulation(Integer.parseInt(textCityPopulation.getText()));

                Country country;
                country = client.capitalFindByName(textCityCapitalNem.getText());
                if (country == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such country!");
                    return;
                }
                currentCity.setCountryId(country.getId());

            }
        } else {
            if (countryMode) {
                Country country = new Country();
                country.setName(textCountryName.getText());
                if (!client.countryInsert(country)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }
                comboCountry.addItem(country.getName());
            } else {
                City city = new City();
                city.setName(textCityName.getText());
                city.setPopulation(Integer.parseInt(textCityPopulation.getText()));

                Country country = null;
                country = client.capitalFindByName(textCityCapitalNem.getText());
                if (country == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such country!");
                    return;
                }
                city.setCountryId(country.getId());

                comboCity.addItem(city.getName());
            }
        }
    }

    private static void delete(){
        if (editMode) {
                client.coutryDelete(currentCountry);
                comboCountry.removeItem(currentCountry.getName());
            } else {
                comboCity.removeItem(currentCity.getName());
            }
        }


    private void fillComboBoxes() {
        comboCountry.removeAllItems();
        comboCity.removeAllItems();
        List<Country> countries = client.countryAll();
        assert countries != null;
        for (Country c : countries) {
            comboCountry.addItem(c.getName());
        }
    }

    private static void clearFields() {
        textCountryName.setText("");
        textCityName.setText("");
        textCityCapitalNem.setText("");
        textCityPopulation.setText("");
        currentCountry = null;
        currentCity = null;
    }

    private static void fillCountriesFields() {
        if (currentCountry == null)
            return;
        textCountryName.setText(currentCountry.getName());
    }


    public static void main(String[] args){
        client = new Client();
        JFrame myWindow = new App();
        myWindow.setVisible(true);
    }
}