package net.teamclerks.em.api.form;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.auth.EMSecurity;

import com.techempower.gemini.Context;
import com.techempower.gemini.form.BasicForm;
import com.techempower.gemini.form.Form;
import com.techempower.gemini.form.FormCheckBox;
import com.techempower.gemini.form.FormHidden;
import com.techempower.gemini.form.FormPasswordField;
import com.techempower.gemini.form.FormSingleValidation;
import com.techempower.gemini.form.FormSubmitButton;
import com.techempower.gemini.form.FormTextField;
import com.techempower.gemini.form.FormValidator;

public class LoginForm extends BasicForm
{   
  public final FormTextField lhuser;
  public final FormPasswordField lhpass;
  public final FormCheckBox lhremember;
  public final FormHidden lhredirect;
  public final FormSubmitButton submit;
  
  public LoginForm(EMContext context)
  {
    super(context.getApplication());
    
    final EMApplication app = context.getApplication();
    final EMSecurity security = app.getSecurity();
    
    this.setName("LoginForm");
    
    lhuser = new FormTextField("lhuser", true, 100, 100);
    lhuser.setDisplayName("User name");
    
    lhpass = new FormPasswordField("lhpass", "", true, 100, 100);
    lhpass.setDisplayName("Password");
    
    lhremember = new FormCheckBox("lhremember", "", false, false);
    lhremember.setDisplayName("Remember me"); 
    
    lhredirect = new FormHidden("r", "")
        .setValue(context);
    
    submit = new FormSubmitButton("lhlogin", "Login", true);

    this.add(new FormValidator() {
      @Override
      public void validate(Form form, Context context,
          FormSingleValidation val)
      {
        final String  username   = lhuser.getStringValue().toLowerCase();
        final String  password   = lhpass.getStringValue();
        final boolean saveCookie = lhremember.isChecked();
        
        // Determine if the current IP address can login right now. 
        boolean success = security.isLoginAttemptPermitted(context);
        
        // If the IP address is permitted to attempt a login, let's proceed.
        if (success)
        {
          success = security.login(context, username, password, saveCookie);
          // If we didn't succeed the login, then set a validation error
          if (!success)
          {
            val.setRawError("Username or password incorrect.", 
                "Username or password incorrect.", 
                "Username or password incorrect.");
          }
        }
        else
        {
          val.setRawError("You are attempting this too often; try again later.", 
              "You are attempting this too often; try again later.", 
              "You are attempting this too often; try again later.");
        }
        
      }
    });
    
    this.add(lhuser)
        .add(lhpass)
        .add(lhremember)
        .add(lhredirect)
        .add(submit);
  }
  
}