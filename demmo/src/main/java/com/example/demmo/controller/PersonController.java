package com.example.demmo.controller;

import com.example.demmo.dto.Message;
import com.example.demmo.dto.Person;
import com.example.demmo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    @Autowired
    private com.example.demmo.repository.PersonRepository repository;

    @Autowired
    private PersonService personService;

    @GetMapping("/person")
    public Iterable<Person> getPersons() {
        return repository.findAll();
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable int id) {
        Optional<Person> person = repository.findById(id);
        return person.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person savedPerson = repository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @PutMapping("/person/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person updatedPerson) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        updatedPerson.setId(id);
        Person savedPerson = repository.save(updatedPerson);
        return new ResponseEntity<>(savedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/person/{p_id}/message")
    public ResponseEntity<Person> addMessageToPerson(@PathVariable("p_id") int personId,
                                                     @RequestBody Message message) {
        try {
            Person person = personService.addMessageToPerson(personId, message);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/person/{p_id}/message/{m_id}")
    public ResponseEntity<Void> deleteMessageFromPerson(@PathVariable("p_id") int personId,
                                                        @PathVariable("m_id") int messageId) {
        try {
            personService.deleteMessageFromPerson(personId, messageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/person/{p_id}/message")
    public ResponseEntity<List<Message>> getPersonMessages(@PathVariable("p_id") int personId) {
        try {
            List<Message> messages = personService.getPersonMessages(personId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/person/{p_id}/message/{m_id}")
    public ResponseEntity<Message> getPersonMessageById(@PathVariable("p_id") int personId,
                                                        @PathVariable("m_id") int messageId) {
        try {
            Message message = personService.getPersonMessageById(personId, messageId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }
}