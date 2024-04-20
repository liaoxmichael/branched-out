package models.rest;

import java.util.ArrayList;

public record Response(String request, boolean successful, String message, ArrayList<ResponseData> data) {
	
}
