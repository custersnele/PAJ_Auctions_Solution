package be.pxl.auctions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AuctionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionsApplication.class, args);
	}

}
