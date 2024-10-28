package vn.iostar.Exception;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import vn.iostar.Config.StorageProperties;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public StorageException(String message) {
	super(message);
	}
	public StorageException(String message, Exception e) {
	super(message,e);
	}
}
