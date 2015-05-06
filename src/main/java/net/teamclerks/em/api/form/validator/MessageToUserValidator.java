package net.teamclerks.em.api.form.validator;

import java.util.Collection;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.api.entity.Friends;
import net.teamclerks.em.auth.entity.User;

import com.techempower.cache.EntityStore;
import com.techempower.gemini.form.FormElement;
import com.techempower.gemini.form.FormElementValidator;
import com.techempower.gemini.form.FormSingleValidation;

public class MessageToUserValidator implements FormElementValidator
{
  private final EntityStore store;
  private final User sender;
  
  public MessageToUserValidator(User sender)
  {
    this.store = EMApplication.getInstance().getStore();
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
    
    final Collection<User> friends = (Collection<User>)this.store.getRelation(Friends.class).rightValueList(recipient.getId());
    
    if(this.sender.isFriendsOnlyMessaging() && !friends.contains(this.sender))
    {
      val.setRawError("User only allows messaging from friends.",
          "User only allows messaging from friends.",
          "User only allows messaging from friends.");
    }
  }
}
