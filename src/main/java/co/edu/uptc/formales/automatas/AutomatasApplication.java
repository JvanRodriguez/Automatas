package co.edu.uptc.formales.automatas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co.edu.uptc.formales.automatas.presenter.Presenter;

@SpringBootApplication
public class AutomatasApplication {

	public static void main(String[] args) {
		new Presenter();
		SpringApplication.run(AutomatasApplication.class, args);
	}

}
