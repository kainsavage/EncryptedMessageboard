package net.teamclerks.em.api.validator;

import java.util.Collection;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.Friends;
import net.teamclerks.em.auth.entity.User;

import com.techempower.cache.EntityStore;
import com.techempower.gemini.input.Input;
import com.techempower.gemini.input.validator.ElementValidator;

public class MessageToUserValidator extends ElementValidator
{
  private final EntityStore store;
  private final User sender;
  
  public MessageToUserValidator(String elementName)
  {
    super(elementName);
    this.store = EMApplication.getInstance().getStore();
    this.sender = EMApplication.getInstance().getSecurity()
        .getUser(EMContext.get());
  }

  @Override
  public void process(Input input)
  {
    final User recipient = store.get(User.class, this.getUserValue(input));
    
    if(recipient == null)
    {
      input.addError("User invalid.");
    }
    
    final Collection<User> friends = 
        this.store.getRelation(Friends.class).rightValueList(recipient.getId());
    
    if(this.sender.isFriendsOnlyMessaging() && !friends.contains(this.sender))
    {
      input.addError("User only allows messaging from friends.");
    }
  }
}
