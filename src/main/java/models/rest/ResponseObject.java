package models.rest;

public record ResponseObject(String request, boolean successful, String message, Object data) {

}
