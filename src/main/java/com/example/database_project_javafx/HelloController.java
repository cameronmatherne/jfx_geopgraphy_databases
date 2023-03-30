// Cameron Matherne
// C00432219
// CMPS 360
// project #4

package com.example.database_project_javafx;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    public String getCountryName(String countryCode) {
        // URL for local database
        String url = "jdbc:h2:./world";

        // make a connection and statement objects linked to the DBMS
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            // ******************************************************
            // ****************  Querying datatbase  ****************
            // ******************************************************

            ResultSet result = null;
            try {
                result = statement.executeQuery("" +
                        "SELECT COUNTRY.NAME " +
                        "FROM COUNTRY " +
                        "WHERE COUNTRY.CODE ='" + countryCode + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String countryName = "";
            while (result.next()) {
                countryName += String.format("%s \n",
                        result.getString(1));

            }
            return countryName;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showCountriesSpeaking(String language) {
        resultBox.clear();
        // connect to db
        String url = "jdbc:h2:./world";

        // query database
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            ResultSet result = null;
            try {
                result = statement.executeQuery("" +
                        "SELECT COUNTRY.NAME " +
                        "FROM COUNTRY, COUNTRYLANGUAGE " +
                        "WHERE COUNTRY.CODE = COUNTRYLANGUAGE.COUNTRYCODE " +
                        "AND COUNTRYLANGUAGE.LANGUAGE = '" + language + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String countries = "";
            while (result.next()) {
               countries += result.getString(1) + "\n";
            }
            resultBox.setText(countries);
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showCityInfo(String cityName) {
        resultBox.clear();
        // connect to db
        String url = "jdbc:h2:./world";

        // query database
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            ResultSet result = null;
            try {
                result = statement.executeQuery("" +
                        "SELECT CITY.NAME, CITY.POPULATION, CITY.DISTRICT, CITY.COUNTRYCODE " +
                        "FROM CITY " +
                        "WHERE CITY.NAME = '" + cityName + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String cityInfo = "";

            while (result.next()) {
                String countryCode = result.getString("COUNTRYCODE");
                String countryName = getCountryName(countryCode);
                cityInfo += String.format("%s%s%s%s%s\n",
                        "Name: " + result.getString(1) + "\n",
                        "Population: " + result.getString(2) + "\n",
                        "District: " + result.getString(3) + "\n",
                        "Country-code: " + countryCode + "\n",
                        // FIX THIS
                        "Country name: " + countryName + "\n");


            }
            System.out.println(cityInfo);
            resultBox.appendText(String.valueOf(cityInfo));
            return null;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showCitiesInCountry(String country) {
        resultBox.clear();
        // ******************************************************
        // ****************  Connecting to datatbase  ***********
        // ******************************************************

        // URL for local database
        String url = "jdbc:h2:./world";

        // make a connection and statement objects linked to the DBMS
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {


            // ******************************************************
            // ****************  Querying datatbase  ****************
            // ******************************************************

            ResultSet result = null;
            try {
                result = statement.executeQuery("" +
                        "SELECT CITY.NAME " +
                        "FROM CITY, COUNTRY " +
                        "WHERE COUNTRY.NAME='" + country + "' AND COUNTRY.CODE=CITY.COUNTRYCODE");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //List<String> cities = new ArrayList<>();
            String cities = "";
            while (result.next()) {
                cities += String.format("%s \n",
                        result.getString(1));

            }
            resultBox.appendText(String.valueOf(cities));
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> showCountriesStartingWithLetter(String letter) {
        resultBox.clear();
        // connect to db
        String url = "jdbc:h2:./world";

        // query database
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            ResultSet result = null;
            try {
                result = statement.executeQuery("" +
                        "SELECT NAME " +
                        "FROM COUNTRY " +
                        "WHERE LEFT (NAME, 1) = '" + letter + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String countries = "";
            while (result.next()) {
                countries += result.getString(1) + "\n";
            }
            resultBox.setText(countries);
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField textInput;

    @FXML
    private TextArea resultBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.setItems(FXCollections.observableArrayList("Country by language", "City information", "Country by Letter", "Cities in Country"));
    }

    @FXML
    void getComboBoxInfo(ActionEvent event) {
        // get user input
        String input = textInput.getText();

        // find out which question the user selects
        String temp = comboBox.getValue();

        if (temp == "Country by language") {
            showCountriesSpeaking(input);
        }
        else if (temp == "City information") {
            showCityInfo(input);

        } else if (temp == "Country by Letter") {
            showCountriesStartingWithLetter(input);

        } else if (temp =="Cities in Country") {
           showCitiesInCountry(input);
        }
    }
}