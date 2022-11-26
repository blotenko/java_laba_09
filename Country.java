package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Country implements Serializable {
    private Long countryId;
    private String name;
    private List<City> cities = new ArrayList<>();

    public Country() { }

    public Country(Long countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }

    public static Country findById(Long id) {
        return null;
    }

    public static Country findByName(String name) {
        return null;
    }

    public static boolean update(Country country) {
        return true;
    }

    public static List<Country> findAll() {
        return null;
    }

    public static boolean insert(Country country) {
        return true;
    }

    public static boolean delete(Country country) {
        return true;
    }

    public Long getId() {
        return countryId;
    }

    public void setId(Long countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}