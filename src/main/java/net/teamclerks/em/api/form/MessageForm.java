package net.teamclerks.em.api.form;

import net.teamclerks.em.api.form.validator.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.gemini.*;
import com.techempower.gemini.form.*;

public class MessageForm extends BasicForm
{
  // Completely chosen at random
  public static final int MAX_MESSAGE_LENGTH   = 4096;
  
  public final FormTextArea message;
  public final FormHidden   recipient;

  public MessageForm(GeminiApplication application, User sender)
  {
    super(application);
    
    this.message = new FormTextArea("message");
    this.message.setRequired(true);
    this.message.setMaxLength(MAX_MESSAGE_LENGTH);
    this.message.addValidator(new PGPMessageValidator());
    this.add(this.message);
    
    this.recipient = new FormHidden("recipient");
    this.recipient.setRequired(true);
    this.recipient.addValidator(new MessageToUserValidator(sender));
    this.add(this.recipient);    
  }
}
