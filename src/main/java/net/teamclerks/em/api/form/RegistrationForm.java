package net.teamclerks.em.api.form;

import net.teamclerks.em.*;
import net.teamclerks.em.api.form.validator.*;

import com.techempower.gemini.form.*;

public class RegistrationForm extends BasicForm
{
  public final FormTextField     username;
  public final FormPasswordField password;
  
  public RegistrationForm(EMApplication application)
  {
    super(application);
    
    this.username = new FormTextField("username");
    this.username.setRequired(true);
    this.username.setMaxLength(45);
    this.username.addValidator(new UsernameUniqueValidator());
    this.add(this.username);
    
    this.password = new FormPasswordField("password");
    this.password.setRequired(true);
//    this.password.addValidator(new PasswordComplexityValidator());
    this.add(this.password);
  }
}
