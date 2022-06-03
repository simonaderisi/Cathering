package it.uniroma3.siw.catering.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.catering.controller.validator.users.CredentialsValidator;
import it.uniroma3.siw.catering.controller.validator.users.UserValidator;
import it.uniroma3.siw.catering.model.users.Credentials;
import it.uniroma3.siw.catering.model.users.User;
import it.uniroma3.siw.catering.service.users.CredentialsService;

@Controller
public class AuthController {

	@Autowired
    private CredentialsService credentialsService;


    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegisterForm (Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "authentication/registerUser.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginForm (Model model) {
        return "authentication/loginForm.html";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {
        return "index.html";
    }

    @RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            //return "admin/home";
        	return "index.html";
        }
        //return "home";
        return "index.html";
    }

    /*@RequestMapping(value = "/defaultGoogle", method = RequestMethod.GET)
    public String defaultAfterLoginGoogle(Model model, @AuthenticationPrincipal OidcUser principal) {
        GoogleUser googleUser = this.googleUserService.getGoogleUser(principal.getEmail());
        if (googleUser == null) {
            saveGoogleUser(model, principal);
        }
        return "home";
    }

    @RequestMapping(value = "/saveGoogleUser", method = RequestMethod.POST)
    public String saveGoogleUser(Model model, OidcUser principal) {
        GoogleUser googleUser = this.googleUserService.getGoogleUser(principal.getEmail());
        if (googleUser == null) {
            googleUser = new GoogleUser();
            googleUser.setEmail(principal.getEmail());
            googleUser.setFullName(principal.getFullName());
            this.googleUserService.saveGoogleUser(googleUser);
        }
        return "home";
    }*/


    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user, BindingResult userBindingResult,
                               @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                               Model model) {

        // validate user and credentials fields
        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        // if neither of them had invalid contents, store the User and the Credentials into the DB
        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            // set the user and store the credentials;
            // this also stores the User, thanks to Cascade.ALL policy
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "authentication/successfulRegistration.html";
        }
        return "authentication/registerUser.html";
    }
}
