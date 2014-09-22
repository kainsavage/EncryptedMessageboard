package net.teamclerks.em.api.form;

import net.teamclerks.em.api.form.validator.*;

import com.techempower.gemini.*;
import com.techempower.gemini.form.*;

public class MessageForm extends BasicForm
{
  // Completely chosen at random
  public static final int MAX_MESSAGE_LENGTH   = 4096;
  public static final int MAX_SIGNATURE_LENGTH = 1024; 
  
  public final FormTextArea message;
  public final FormTextArea signature;

  public MessageForm(GeminiApplication application)
  {
    super(application);
    
    this.message = new FormTextArea("message");
    this.message.setRequired(true);
    this.message.setMaxLength(MAX_MESSAGE_LENGTH);
    this.message.addValidator(new PGPMessageValidator());
    this.add(this.message);
    
    this.signature = new FormTextArea("signature");
    this.signature.setRequired(false);
    this.signature.setMaxLength(MAX_SIGNATURE_LENGTH);
    this.signature.addValidator(new PGPSignatureValidator());
    this.add(this.signature);
  }
}
