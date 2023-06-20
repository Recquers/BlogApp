package in.vasanth.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import in.vasanth.appconstants.AppConstants;
import in.vasanth.binding.Login;
import in.vasanth.binding.RegistrationPage;
import in.vasanth.entity.BlogsPage;
import in.vasanth.properties.AppProperties;
import in.vasanth.service.BlogService;
import in.vasanth.service.UserService;

@Controller
public class UserController {

	@Autowired
	private AppProperties appProps;
	@Autowired
	private BlogService blogService;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	@GetMapping("/")
	public String indexPage(Model model) {
		List<BlogsPage> blogs = blogService.getBlogs();
		model.addAttribute("blog", blogs);
		return "index";

	}

	@GetMapping("/logout")
	public String logoutmethod() {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {

		model.addAttribute("register", new RegistrationPage());
		return AppConstants.REGISTRATION;
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute("register") @RequestBody RegistrationPage form, Model model)
			throws Exception {

		boolean status = userService.registerUser(form);
		if (status) {
			model.addAttribute("Successmsg", appProps.getMessages().get(AppConstants.SUCCESS));
		} else {
			model.addAttribute("Errmsg", appProps.getMessages().get(AppConstants.ERROR));
		}

		return AppConstants.REGISTRATION;
	}

	@GetMapping("/login")
	public String getLoginPage(Model model) {
		model.addAttribute("login", new Login());
		return AppConstants.LOGIN;
	}

	@PostMapping("/enter")
	public String login(@ModelAttribute("login") @RequestBody Login lform, Model model) throws Exception {
		boolean status = userService.loginUser(lform);
		if (status) {
			List<BlogsPage> dashboardData = blogService.dashboardData();
			model.addAttribute("blogs", dashboardData);
			return "dashboard";
		}
		model.addAttribute("errMsg", appProps.getMessages().get("check"));

		return AppConstants.LOGIN;

	}

}
