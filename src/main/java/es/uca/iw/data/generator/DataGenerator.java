package es.uca.iw.data.generator;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;

import es.uca.iw.data.entity.SamplePerson;
import es.uca.iw.data.service.SamplePersonRepository;
import es.uca.iw.ejemplo.user.User;
import es.uca.iw.ejemplo.user.UserService;

@SpringComponent
public class DataGenerator {

	@Bean
	public CommandLineRunner loadData(SamplePersonRepository samplePersonRepository, UserService userService) {
		return args -> {
			
			
			Logger logger = LoggerFactory.getLogger(getClass());

			logger.info("info");
			logger.warn("warning");
			logger.debug("debug");
			logger.error("error");
			
			
			
			
			
			
			try {
				userService.loadUserByUsername("admin");
			} catch (UsernameNotFoundException ex) {
				User oneUser = new User();
				oneUser.setFirstName("Administrador");
				oneUser.setLastName("del Sistema");
				oneUser.setUsername("admin");
				oneUser.setPassword("password");
				oneUser.setEmail("admin@myapp.com");
				userService.registerUser(oneUser);
				logger.info("Creating admin user...");
				userService.activateUser("admin@myapp.com", "key");
			}

			if (samplePersonRepository.count() != 0L) {
				logger.info("Using existing database");
				return;
			}
			int seed = 123;

			logger.info("Generating demo data");

			logger.info("... generating 100 Sample Person entities...");
			ExampleDataGenerator<SamplePerson> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
					SamplePerson.class, LocalDateTime.of(2021, 11, 10, 0, 0, 0));
			samplePersonRepositoryGenerator.setData(SamplePerson::setId, DataType.ID);
			samplePersonRepositoryGenerator.setData(SamplePerson::setFirstName, DataType.FIRST_NAME);
			samplePersonRepositoryGenerator.setData(SamplePerson::setLastName, DataType.LAST_NAME);
			samplePersonRepositoryGenerator.setData(SamplePerson::setEmail, DataType.EMAIL);
			samplePersonRepositoryGenerator.setData(SamplePerson::setPhone, DataType.PHONE_NUMBER);
			samplePersonRepositoryGenerator.setData(SamplePerson::setDateOfBirth, DataType.DATE_OF_BIRTH);
			samplePersonRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
			samplePersonRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
			samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

			logger.info("Generated demo data");

		};
	}

}