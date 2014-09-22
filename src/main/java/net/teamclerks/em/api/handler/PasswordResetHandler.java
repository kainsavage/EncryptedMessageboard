/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.*;

import com.google.common.collect.*;
import com.techempower.gemini.*;
import com.techempower.gemini.email.*;
import com.techempower.gemini.email.outbound.*;
import com.techempower.gemini.form.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;
import com.techempower.gemini.pyxis.*;
import com.techempower.util.*;

public class PasswordResetHandler
  extends MethodPathHandler<EMContext>
  implements Configurable
{
  public static final int    DEFAULT_EXPIRATION_DAYS = 5;
  public static final String DEFAULT_BASE_URI        = "/api/user/password/";
  public static final String EMAIL_TEMPLATE_NAME     = "E-PasswordResetAuthorization"; 
  
  private final EMSecurity security;
  
  private String fromAddress = "";
  private int    expirationDays = DEFAULT_EXPIRATION_DAYS;
  
  public PasswordResetHandler(EMApplication application)
  {
    super(application);
    
    this.security = application.getSecurity();
    
    // Ask the EmailTemplater to load our template when it configures.
    EmailTemplater templater = application.getEmailTemplater();
    templater.addTemplateToLoad(EMAIL_TEMPLATE_NAME);
    
    application.getConfigurator().addConfigurable(this);
  }
  
  @Override
  public void configure(EnhancedProperties props)
  {
    EnhancedProperties.Focus focus = props.focus("PasswordReset.");
    
    this.fromAddress = focus.get("FromAddress", this.fromAddress);
    this.expirationDays = focus.getInt("ExpirationDays", DEFAULT_EXPIRATION_DAYS);
  }
  
  @PathSegment
  public boolean reset()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    // Send the e-mail and notify the user that we've done so.
    final String username = context().query().get("un");
    final BasicWebUser user = (BasicWebUser)this.security.getUser(username);
    
    // Did we find the user?
    if (user != null)
    {
      // Update the user.
      final String ticket = user.generateNewPasswordResetTicket(this.expirationDays);
      this.app().getStore().put(user);
      
      // Send the email.

      final EmailTemplater templater = app().getEmailTemplater();
      final Map<String,Object> macros = new HashMap<>();
      macros.put("$UN", user.getUserUsername());
      macros.put("$FN", user.getUserFirstname());
      macros.put("$LN", user.getUserLastname());
      macros.put("$EM", user.getUserEmail());
      macros.put("$VT", ticket);
      macros.put("$ED", "" + this.expirationDays);

      // Construct the URL.
      String url = app().getInfrastructure().getSecureUrl()
        + "password-reset-process?"
        + "un=" + user.getUserUsername() 
        + "&vt=" + ticket;
      macros.put("$URL", url);

      // Get a suitable author address.
      final String authorAddress = this.fromAddress;
      
      // Send the mail.
      final EmailPackage email = templater.process(EMAIL_TEMPLATE_NAME, 
          macros, authorAddress, user.getUserEmail());
      
      if (email != null)
      {
        app().getEmailServicer().sendMail(email);
      }

      json.put("success", true);
    }
    else
    {
      json.put("error", "Sorry, a user with that user name was not found.");
    }
    
    return json(json);
  }
  
  @PathSegment
  public boolean process()
  {
    final Map<String,Object> json = Maps.newHashMap();

    final String username = context().query().get("un");
    final String ticket = context().query().get("vt");
    
    // Get a reference to the user.
    final BasicWebUser user = (BasicWebUser)this.security.getUser(username);
    
    // Build the password reset form.
    final Form resetForm = new PostOnlyForm(app(), "resetpw", context().getUrl());
    resetForm.add(new FormHidden("un", username, true));
    resetForm.add(new FormHidden("vt", ticket, true));
    final FormPasswordField newPwField = new FormPasswordField("Password", "pw", "", true, BasicUser.MAX_PASSWORD_LENGTH, BasicUser.MAX_PASSWORD_LENGTH);
    newPwField.setConfirm(true, true);
//    newPwField.addValidator(
//        new PasswordComplexityValidator(5, BasicUser.MAX_USERNAME_LENGTH, 1, 1, 0, 0));
    resetForm.add(newPwField);
    
    // Add the user validator
    resetForm.add(new FormValidator()
    {
      @Override
      public void validate(Form form, Context context, FormSingleValidation val)
      {
        // Is the authorization ticket correct and not expired?
        if (user == null)
        {
          val.setRawError("User not found.",
              "User not found.",
              "User not found.");
        }
      }
    });
    
    // Add the password reset authorized ticket validator
    resetForm.add(new FormValidator()
    {
      @Override
      public void validate(Form form, Context context, FormSingleValidation val)
      {
        // Is the authorization ticket correct and not expired?
        if (user != null && !user.isPasswordResetAuthorized(ticket))
        {
          val.setRawError("Invalid password-reset ticket.",
              "Invalid password-reset ticket.",
              "Invalid password-reset ticket.");
        }
      }
    });
    
    resetForm.setValues(context());
    
    FormValidation validation = resetForm.validate(context());
    if (validation.isGood())
    {
      // Change the password.
      user.setUserPassword(newPwField.getStringValue());
      user.setPasswordResetTicket("");
      user.setPasswordResetExpiration(null);
      // Save user
      context().getApplication().getStore().put(user);
      
      // Go to the success/complete page.
      json.put("success", true);
    }
    else
    {
      json.put("validation", validation);
    }
    
    return json(json);
  }
}
