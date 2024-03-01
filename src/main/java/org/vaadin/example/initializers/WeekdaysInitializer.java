package org.vaadin.example.initializers;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.vaadin.example.entity.Weekday;
import org.vaadin.example.repository.WeekdayRepository;

@Component
public class WeekdaysInitializer implements ApplicationRunner {
    private final WeekdayRepository weekdayRepository;

    public WeekdaysInitializer(WeekdayRepository weekdayRepository) {
        this.weekdayRepository = weekdayRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (weekdayRepository.findByName("Monday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Monday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Tuesday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Tuesday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Wednesday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Wednesday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Thursday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Thursday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Friday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Friday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Saturday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Saturday");
            weekdayRepository.save(weekday);
        }
        if (weekdayRepository.findByName("Sunday") == null) {
            Weekday weekday = new Weekday();
            weekday.setName("Sunday");
            weekdayRepository.save(weekday);
        }
    }
}
