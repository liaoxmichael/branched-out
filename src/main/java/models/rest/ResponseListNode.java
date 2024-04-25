package models.rest;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public record ResponseListNode(String request, boolean successful, String message, ArrayList<JsonNode> data) {

}
