package com.izone.globalweather.air_quality.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class City {
    private String country;

    public City(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return getCountry().equals(city.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry());
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    static public List<City> getCityList(){
        ArrayList<City> cityList = new ArrayList<>();
        cityList.add(new City("Chiang Mai, Thailand"));
        cityList.add(new City("Dubai, United Arab Emirates"));
        cityList.add(new City("Yangon, Myanmar"));
        cityList.add(new City("Bangkok, Thailand"));
        cityList.add(new City("Kathmandu, Nepal"));
        cityList.add(new City("Karachi, Pakistan"));
        cityList.add(new City("Seoul, South Korea"));
        cityList.add(new City("Singapore, Singapore"));
        cityList.add(new City("Milano, Italy"));
        cityList.add(new City("Rome, Italy"));
        cityList.add(new City("Zagreb, Croatia"));
        cityList.add(new City("Beijing, China"));
        cityList.add(new City("Accra, Ghana"));
        cityList.add(new City("Mexico City, Mexico"));
        cityList.add(new City("Toronto, Canada"));
        cityList.add(new City("Sofia, Bulgaria"));
        cityList.add(new City("Hong Kong, Hong Kong SAR"));
        cityList.add(new City("Pristina, Kosovo"));
        cityList.add(new City("Ulaanbaatar, Mongolia"));
        cityList.add(new City("Santiago, Chile"));
        cityList.add(new City("Skopje, North Macedonia"));
        cityList.add(new City("Taipei, Taiwan"));
        cityList.add(new City("Kaohsiung, Taiwan"));
        cityList.add(new City("Detroit, USA"));
        cityList.add(new City("Kyoto, Japan"));
        cityList.add(new City("Shenzhen, China"));
        cityList.add(new City("Algiers, Algeria"));
        cityList.add(new City("Nice, France"));
        cityList.add(new City("Wuhan, China"));
        cityList.add(new City("Seoul, South Korea"));
        cityList.add(new City("Singapore, Singapore"));
        cityList.add(new City("Milano, Italy"));
        cityList.add(new City("Rome, Italy"));
        cityList.add(new City("Zagreb, Croatia"));
        cityList.add(new City("Beijing, China"));
        cityList.add(new City("Accra, Ghana"));
        cityList.add(new City("Mexico City, Mexico"));
        cityList.add(new City("Toronto, Canada"));
        cityList.add(new City("Sofia, Bulgaria"));
        cityList.add(new City("Hong Kong, Hong Kong SAR"));
        cityList.add(new City("Pristina, Kosovo"));
        cityList.add(new City("Ulaanbaatar, Mongolia"));
        cityList.add(new City("Santiago, Chile"));
        cityList.add(new City("Skopje, North Macedonia"));
        cityList.add(new City("Taipei, Taiwan"));
        cityList.add(new City("Kaohsiung, Taiwan"));
        cityList.add(new City("Detroit, USA"));
        cityList.add(new City("Kyoto, Japan"));
        cityList.add(new City("Shenzhen, China"));
        cityList.add(new City("Algiers, Algeria"));
        cityList.add(new City("Nice, France"));
        cityList.add(new City("Madrid, Spain"));
        cityList.add(new City("Krakow, Poland"));
        cityList.add(new City("Tokyo, Japan"));
        cityList.add(new City("Riyadh, Saudi Arabia"));
        cityList.add(new City("Kyiv, Ukraine"));
        cityList.add(new City("Busan, South Korea"));
        cityList.add(new City("Jakarta, Indonesia"));
        cityList.add(new City("New York City, USA"));
        cityList.add(new City("Sydney, Australia"));
        cityList.add(new City("Melbourne, Australia"));
        cityList.add(new City("Brisbane, Australia"));
        cityList.add(new City("Perth, Australia"));
        cityList.add(new City("Canberra, Australia"));
        cityList.add(new City("Adelaide, Australia"));
        cityList.add(new City("Darwin, Australia"));
        cityList.add(new City("Melbourne, Australia"));
        cityList.add(new City("Colombo, Sri Lanka"));
        cityList.add(new City("Kandy, Sri Lanka"));
        cityList.add(new City("Galle, Sri Lanka"));
        cityList.add(new City("Trincomalee, Sri Lanka"));
        cityList.add(new City("Negombo, Sri Lanka"));
        cityList.add(new City("Unawatuna, Sri Lanka"));
        cityList.add(new City("Batticaloa, Sri Lanka"));
        cityList.add(new City("Mount Lavinia, Sri Lanka"));
        cityList.add(new City("Anuradhapura, Sri Lanka"));
        cityList.add(new City("New Delhi, India"));
        cityList.add(new City("Mumbai, India"));
        cityList.add(new City("Bengaluru, India"));
        cityList.add(new City("Kolkata, India"));
        cityList.add(new City("Chennai, India"));
        cityList.add(new City("Hyderabad, India"));
        cityList.add(new City("Bhopal, India"));
        cityList.add(new City("Jaipur, India"));

        cityList.add(new City("Sapporo,Japan"));
        cityList.add(new City("Manhattan, USA"));
        cityList.add(new City("Barcelona, Spain"));
        cityList.add(new City("Berlin, Germany"));
        cityList.add(new City("Marrakesh, Morocco"));
        cityList.add(new City("Wellington, New Zealand"));
        cityList.add(new City("Cairo, Egypt"));
        cityList.add(new City("Vancouver, Canada"));
        cityList.add(new City("Rio de Janeiro, Brazil"));
        cityList.add(new City("Istanbul, Turkey"));
        cityList.add(new City("Lisbon, Portugal"));
        cityList.add(new City("Mumbai, India"));
        cityList.add(new City("Stockholm, Sweden"));
        cityList.add(new City("Kuala Lumpur, Malaysia"));
        cityList.add(new City("Budapest, Hungary"));
        cityList.add(new City("Cape Town, South Africa"));
        cityList.add(new City("Oslo, Norway"));
        cityList.add(new City("Dublin, Ireland"));
        cityList.add(new City("Kyoto, Japan"));
        cityList.add(new City("Beirut, Lebanon"));
        cityList.add(new City("Helsinki, Finland"));
        cityList.add(new City("Munich, Germany"));
        cityList.add(new City("Zurich, Switzerland"));
        cityList.add(new City("Copenhagen, Denmark"));
        cityList.add(new City("Auckland, New Zealand"));
        cityList.add(new City("San Francisco, USA"));
        cityList.add(new City("Prague, Czech Republic"));
        cityList.add(new City("Amsterdam, Netherlands"));
        cityList.add(new City("Vienna, Austria"));
        cityList.add(new City("Athens, Greece"));
        cityList.add(new City("Seville, Spain"));
        cityList.add(new City("Krakow, Poland"));
        cityList.add(new City("Brussels, Belgium"));
        cityList.add(new City("Rome, Italy"));
        cityList.add(new City("Shanghai, China"));
        cityList.add(new City("Moscow, Russia"));
        cityList.add(new City("Bangalore, India"));
        cityList.add(new City("Kolkata, India"));
        cityList.add(new City("Prague, Czech Republic"));
        cityList.add(new City("Edinburgh, Scotland"));
        cityList.add(new City("Ljubljana, Slovenia"));
        cityList.add(new City("Krakow, Poland"));
        cityList.add(new City("Sofia, Bulgaria"));
        cityList.add(new City("Casablanca, Morocco"));
        cityList.add(new City("Nairobi, Kenya"));
        cityList.add(new City("Johannesburg, South Africa"));
        cityList.add(new City("Kigali, Rwanda"));
        cityList.add(new City("Reykjavik, Iceland"));
        cityList.add(new City("Bratislava, Slovakia"));
        cityList.add(new City("Helsinki, Finland"));
        return removeDuplicates(cityList);
    }

    public static List<City> removeDuplicates(List<City> cityList) {
        Set<City> uniqueCities = new HashSet<>(cityList);
        return new ArrayList<>(uniqueCities);
    }
}
