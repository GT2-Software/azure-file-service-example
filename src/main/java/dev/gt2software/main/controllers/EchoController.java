package dev.gt2software.main.controllers;

import javax.ws.rs.Path;

import dev.gt2software.main.models.ResponseModel;

@Path("/echo")
public class EchoController {
    private final ResponseModel responseModel = ResponseModel.getInstance();
}
