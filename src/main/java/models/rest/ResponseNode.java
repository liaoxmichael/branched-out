package models.rest;

import com.fasterxml.jackson.databind.JsonNode;

public record ResponseNode(String request, boolean successful, String message, JsonNode data) {

}
