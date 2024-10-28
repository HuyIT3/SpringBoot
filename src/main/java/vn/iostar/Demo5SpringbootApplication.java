package vn.iostar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import vn.iostar.Config.MySiteMeshFilter;
import vn.iostar.Config.StorageProperties;
import vn.iostar.Service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Demo5SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(Demo5SpringbootApplication.class, args);
	}
	@Bean
	 public FilterRegistrationBean<MySiteMeshFilter> siteMeshFilter() {
		 FilterRegistrationBean<MySiteMeshFilter> filterRegistrationBean = new FilterRegistrationBean<MySiteMeshFilter>();
		 filterRegistrationBean.setFilter(new MySiteMeshFilter()); // adding sitemesh filter ??
		 filterRegistrationBean.addUrlPatterns("/*");
		 return filterRegistrationBean;
	 }
	// thêm cấu hình storage
	@Bean
	CommandLineRunner init(IStorageService storageService) {
	return (args -> {
	storageService.init();
	});
	}
}
