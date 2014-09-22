package net.teamclerks.em.api.form;

import net.teamclerks.em.api.form.validator.*;

import com.techempower.gemini.*;
import com.techempower.gemini.form.*;

public class ProfileForm extends BasicForm
{
  // Completely chosen at random
  public static final int MAX_MESSAGE_LENGTH   = 4096;
  public static final int MAX_SIGNATURE_LENGTH = 1024; 
  
  public final FormTextField         firstName;
  public final FormTextField         lastName;
  public final FormEmailAddressField email;
  public final FormTextField         publicKey;

  public ProfileForm(GeminiApplication application)
  {
    super(application);
    
    this.firstName = new FormTextField("firstname");
    this.firstName.setRequired(true);
    this.firstName.setMaxLength(45);
    this.add(this.firstName);
    
    this.lastName = new FormTextField("lastname");
    this.lastName.setMaxLength(45);
    this.add(this.lastName);
    
    this.email = new FormEmailAddressField("email");
    this.email.setMaxLength(254);
    this.add(this.email);
    
    this.publicKey = new FormTextField("publicKey");
    this.publicKey.setRequired(true);
    this.publicKey.setMaxLength(1024);
    this.publicKey.addValidator(new PGPPublicKeyValidator());
    this.add(this.publicKey);
  }
}
