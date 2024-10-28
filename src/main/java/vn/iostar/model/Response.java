package vn.iostar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	private Boolean status;
	    private String message;
	    private Object body;
	    public Response(boolean status, String message, Object body) {
	        this.status = status;
	        this.message = message;
	        this.body = body;
	    }
}
