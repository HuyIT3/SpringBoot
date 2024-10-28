package vn.iostar.Config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;
@Data
@ConfigurationProperties("storage")
public class StorageProperties {
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
