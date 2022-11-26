package dto;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {
    private Long cityId;
    private Long countryId;
    private String name;
    private Integer population;

    public City(Long cityId, String name, Integer population, Long countryId) {
        this.countryId = countryId;
        this.cityId = cityId;
        this.name = name;
        this.population = population;
    }

    public City() { }

    public static City findByName(String name) {
        return null;
    }

    public static boolean update(City city) {
        return true;
    }

    public static List<City> findAll() {
        return null;
    }

    public static boolean delete(City city) {
        return true;
    }

    public static boolean insert(City city) {
        return true;
    }

    public static List<City> findByCountryId(Long id) {
        return null;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long CountryId) {
        this.countryId = CountryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer releaseYear) {
        this.population = releaseYear;
    }
}