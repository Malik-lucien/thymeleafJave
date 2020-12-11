package com.java_avancer.javanAvancer.controller;

import java.util.ArrayList;
import java.util.List;

import com.java_avancer.javanAvancer.form.PersonForm;
import com.java_avancer.javanAvancer.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    private static final List<Person> persons = new ArrayList<Person>();


    static {
        persons.add(new Person(1, "notoriouss", "warrior"));
        persons.add(new Person(2, "theEagle", "warrior"));
        persons.add(new Person(3, "cowboyBepop", "warrior"));
        persons.add(new Person(4, "adesagna", "warrior"));
    }

    //permet d'injecter des valeurs dans les variables du dessous
    @Value("${welcome.message}")
    private String message;
    @Value("${error.message}")
    private String errorMessage;

    // @getMapping == RequestMapping(value =....., method get)
    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("message", message);
        System.out.println("toto");
        return "index";
    }

    @GetMapping(value = "/personList")
    public String personList(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        Person[] person = restTemplate.getForObject("http://localhost:8081/players", Person[].class);
        //cree obj rest templ + apel api app de droite + passer resultat a add attribut
        model.addAttribute("persons", person);
//        System.out.println("toto");
        return "personList";
    }

    @GetMapping(value = "/addPerson")
    public String showAddPersonPage(Model model) {

        PersonForm personForm = new PersonForm();
        model.addAttribute("personForm", personForm);

        return "addPerson";
    }

    @PostMapping(value = "/addPerson")
    public String savePerson(Model model, @ModelAttribute("personForm") PersonForm personForm) {

        int id = personForm.getId();
        String nom = personForm.getName();
        String type = personForm.getType();

        if (nom != null && nom.length() > 0 //
                && type != null && type.length() > 0) {
            Person newPerson = new Person(id, nom, type);
            persons.add(newPerson);

            return "redirect:/personList";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addPerson";
    }


}
