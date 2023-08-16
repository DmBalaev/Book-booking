package dm.balaev.Bookbooking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Book Booking API",
				version = "1.0",
				description = "API for managing book reservations and borrowings"
		)
)
public class BookBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookBookingApplication.class, args);
	}

}
