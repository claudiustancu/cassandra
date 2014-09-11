package com.sample.cassandra;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/users")
public class UsersController {

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(Locale locale, Model model) {

		CassandraSimpleClient client = new CassandraSimpleClient();

		client.connect("127.0.0.1");
		model.addAttribute("serverTime",
				client.getSession().execute("SELECT * from simplex.users"));
		client.close();

		return "home";
	}

	@ResponseStatus(CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public void post(@RequestBody User user) {
		CassandraSimpleClient client = new CassandraSimpleClient();
		client.connect("127.0.0.1");

		client.getSession().execute(
				"INSERT INTO simplex.users (id, name, role) VALUES ("
						+ UUID.randomUUID() + ", " + user.getName() + ", "
						+ user.getRole() + ");");
	}
}