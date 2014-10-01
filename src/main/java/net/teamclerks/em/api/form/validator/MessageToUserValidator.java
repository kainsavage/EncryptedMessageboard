package net.teamclerks.em.api.form.validator;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.gemini.form.*;

public class MessageToUserValidator implements FormElementValidator
{
  private final EMStore store;
  private final User sender;
  
  public MessageToUserValidator(User sender)
  {
    this.store = (EMStore)EMApplication.getInstance().getStore();
    this.sender = sender;
  }

  @Override
  public void validate(FormElement element, FormSingleValidation val)
  {
    final User recipient = store.get(User.class, element.getIntegerValue());
    
    if(recipient == null)
    {
      val.setRawError("User invalid.", 
          "User invalid", 
          "User invalid.");
    }
    
    final Collection<User> friends = this.store.friends.rightValueList(recipient);
    
    if(this.sender.isFriendsOnlyMessaging() && !friends.contains(this.sender))
    {
      val.setRawError("User only allows messaging from friends.",
          "User only allows messaging from friends.",
          "User only allows messaging from friends.");
    }
  }
}
