package net.teamclerks.em.api.validator;

import java.util.Collection;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.relation.*;
import net.teamclerks.em.auth.entity.User;

import com.techempower.cache.EntityStore;
import com.techempower.gemini.input.Input;
import com.techempower.gemini.input.validator.ElementValidator;

public class MessageToUserValidator extends ElementValidator
{  
  public MessageToUserValidator(String elementName)
  {
    super(elementName);
  }

  @Override
  public void process(Input input)
  {
    final EntityStore store = EMApplication.getInstance().getStore();
    final User sender = EMApplication.getInstance().getSecurity()
        .getUser(EMContext.get());
    final User recipient = store.get(User.class, this.getUserValue(input));
    
    if(recipient == null)
    {
      input.addError("User invalid.");
    }
    
    final Collection<User> friends = 
        store.getRelation(Friends.class).rightValueList(recipient.getId());
    
    if(sender.isFriendsOnlyMessaging() && !friends.contains(sender))
    {
      input.addError("User only allows messaging from friends.");
    }
  }
}
