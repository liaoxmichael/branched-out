package models.rest;

import java.util.ArrayList;

public record ResponseListData(String request, boolean successful, String message, ArrayList<ResponseData> data) {
	
}
