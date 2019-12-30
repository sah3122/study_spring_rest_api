package me.study.springrestapi.common;

import me.study.springrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends EntityModel<Errors> {
    public ErrorResource(Errors content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
