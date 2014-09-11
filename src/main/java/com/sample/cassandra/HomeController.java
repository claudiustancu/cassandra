package com.sample.cassandra;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		CassandraSimpleClient client = new CassandraSimpleClient();
		
		client.connect("127.0.0.1");
		client.createSchema();
		client.getSession().execute("INSERT INTO simplex.users (id, name, role) "
		        + "VALUES (" + UUID.randomUUID() + ", 'Cezar', 'Developer');");
		System.out.print(client.getSession().execute(
				"SELECT * from simplex.users"));
		client.close();

		return "home";
	}
}