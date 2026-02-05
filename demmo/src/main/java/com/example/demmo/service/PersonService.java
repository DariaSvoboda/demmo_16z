package com.example.demmo.service;

import com.example.demmo.dto.Message;
import com.example.demmo.dto.Person;
import com.example.demmo.repository.PersonRepository;
import com.example.demmo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Person addMessageToPerson(int personId, Message message) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пользователь с id=" + personId + " не найден"
                ));

        if (message.getTime() == null) {
            message.setTime(LocalDateTime.now());
        }

        message.setPerson(person);
        person.addMessage(message);

        return personRepository.save(person);
    }

    public void deleteMessageFromPerson(int personId, int messageId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пользователь с id=" + personId + " не найден"
                ));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Сообщение с id=" + messageId + " не найдено"
                ));

        if (message.getPerson() == null || message.getPerson().getId() != personId) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Сообщение с id=" + messageId + " не принадлежит пользователю с id=" + personId
            );
        }

        person.removeMessage(message);
        messageRepository.delete(message);
    }

    public List<Message> getPersonMessages(int personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пользователь с id=" + personId + " не найден"
                ));

        return person.getMessages();
    }

    public Message getPersonMessageById(int personId, int messageId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пользователь с id=" + personId + " не найден"
                ));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Сообщение с id=" + messageId + " не найдено"
                ));

        if (message.getPerson() == null || message.getPerson().getId() != personId) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Сообщение с id=" + messageId + " не принадлежит пользователю с id=" + personId
            );
        }

        return message;
    }

    public Optional<Person> getPersonWithMessages(int id) {
        return personRepository.findById(id);
    }
}