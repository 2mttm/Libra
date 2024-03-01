package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.City;
import org.vaadin.example.repository.CityRepository;

@Component
public class CitiesInitializer implements ApplicationRunner {
    private final CityRepository cityRepository;

    public CitiesInitializer(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (cityRepository.findByName("Chisinau") == null) {
            City city = new City();
            city.setName("Chisinau");
            cityRepository.save(city);
        }
        if (cityRepository.findByName("Balti") == null) {
            City city = new City();
            city.setName("Balti");
            cityRepository.save(city);
        }
        if (cityRepository.findByName("Tighina") == null) {
            City city = new City();
            city.setName("Tighina");
            cityRepository.save(city);
        }
        if (cityRepository.findByName("Tiraspol") == null) {
            City city = new City();
            city.setName("Tiraspol");
            cityRepository.save(city);
        }
        if (cityRepository.findByName("Comrat") == null) {
            City city = new City();
            city.setName("Comrat");
            cityRepository.save(city);
        }

    }
}
