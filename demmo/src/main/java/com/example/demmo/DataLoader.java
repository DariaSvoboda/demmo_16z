package com.example.demmo;

import com.example.demmo.dto.Message;
import com.example.demmo.dto.Person;
import com.example.demmo.repository.PersonRepository;
import com.example.demmo.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader {

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private MessageRepository messageRepo;

    @PostConstruct
    public void init() {
        if (personRepo.count() == 0) {
            System.out.println("Инициализация");

            Person person1 = new Person("Ульяна", "Александровна", "Кабунина", LocalDate.of(2007, 7, 11));
            Person person2 = new Person("Дарья", "Станиславовна", "Свобода", LocalDate.of(2007, 11, 20));
            Person person3 = new Person("Владимир", "Владимирович", "Петров", LocalDate.of(1995, 3, 8));

            person1 = personRepo.save(person1);
            person2 = personRepo.save(person2);
            person3 = personRepo.save(person3);

            Message message1 = new Message("Здравствуйте", "Хорошего вам дня", LocalDateTime.now());
            message1.setPerson(person1);

            Message message2 = new Message("Анекдот!", "- Блин! – сказал слон, наступив на Колобка.", LocalDateTime.now());
            message2.setPerson(person2);

            person1.addMessage(message1);
            person2.addMessage(message2);

            personRepo.save(person1);
            personRepo.save(person2);

            System.out.println("Создано 3 человека");
            System.out.println("Создано 2 сообщения");
        }
    }
}