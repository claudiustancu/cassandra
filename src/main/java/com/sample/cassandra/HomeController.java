package com.sample.cassandra;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

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
		
		ResultSet resultSet = client.getSession().execute(
                "SELECT * from simplex.users");
		List<Row> rows = resultSet.all();
		if (!rows.isEmpty()) {
		    System.out.println("\t\tid\t\t\t\tname\t\trole");
		    for (Row row : rows) {
		        System.out.println(row.getUUID("id") + "\t|\t" + row.getString("name") + "\t|\t" + row.getString("role"));
		    }
		} else {
		    System.out.println("The result set contains no records");
		}
		client.close();

		return "home";
	}
}