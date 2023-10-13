package com.lcwd.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    //extra properties you want to mange
    //constructor
    public ResourceNotFoundException(){
        super("Resource not found on server !!");

    }
    //parameterised constructor
    public ResourceNotFoundException(String message){
        super(message);

    }
}
