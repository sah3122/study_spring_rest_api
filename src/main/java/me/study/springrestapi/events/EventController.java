package me.study.springrestapi.events;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    @PostMapping
    public ResponseEntity createEvent(@RequestBody Event event) {
        //URI createdUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
        URI createdUri = linkTo(EventController.class).slash("{id}").toUri(); // class에 uri를 지정해서 이렇게 사용 가능
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
