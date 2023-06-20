package in.vasanth.service;



import in.vasanth.binding.Login;
import in.vasanth.binding.RegistrationPage;

public interface UserService {
	
  public boolean registerUser(RegistrationPage form)  throws Exception;	
  
  public boolean loginUser(Login lform) throws Exception;
}
