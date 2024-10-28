package vn.iostar.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reponse {
	 private Boolean status;
	    private String message;
	    private Object body;
}
