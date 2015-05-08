package net.teamclerks.em.api.handler;

import java.util.*;
import java.util.stream.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.api.entity.relation.*;
import net.teamclerks.em.api.validator.*;
import net.teamclerks.em.auth.entity.*;

import com.google.common.collect.*;
import com.techempower.gemini.input.*;
import com.techempower.gemini.input.validator.*;
import com.techempower.gemini.path.annotation.*;

public class MessageHandler extends EMHandler
{
  private final ValidatorSet postMessage = new ValidatorSet(
    new LengthValidator("message", 1, 4096),
    new PGPMessageValidator("message"),
    new MessageToUserValidator("recipient")
  );

  public MessageHandler(EMApplication app)
  {
    super(app);
  }

  @Path("")
  @Get
  public boolean getLatestMessages()
  {
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      return json(
        // Get my messages...
        store().getRelation(UserMessages.class).rightValueList(user).stream()
          // sorted from newest to oldest...
          .sorted(Message.NEWEST_TO_OLDEST)
          // gotten as json.
          .map(m -> m.view()).collect(Collectors.toList()));
    }
    
    return unauthorized("Must be logged in.");
  }
  
  @Path("{userId}")
  @Get
  public boolean getLatestMessagesFromUser(int userId)
  {
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      return json(
        // Get my messages...
        store().getRelation(UserMessages.class).rightValueList(user).stream()
          // from that user...
          .filter(m -> m.getSender() == userId)
          // sorted from newest to oldest...
          .sorted(Message.NEWEST_TO_OLDEST)
          // gotten as json...
          .map(m -> m.view()).collect(Collectors.toList()));
    }
    
    return unauthorized("Must be logged in.");
  }
  
  @Path("")
  @Post
  public boolean sendMessage()
  {
    final Map<String,Object> json = Maps.newHashMap();
    final User user = app().getSecurity().getUser(context());    
    
    if(user != null)
    {
      final Input input = this.postMessage.process(context());
      
      if(input.passed())
      {
        final Message message = new Message();
        message.setCreated(new Date());
        message.setRead(false);
        message.setMessage(input.values().get("message"));
        message.setSender(user.getId());
        
        store().put(message);
        store().getRelation(UserMessages.class)
          .add(input.values().getInt("recipient"), message);
        
        json.put("success", true);
        
        return json(json);
      }
      else
      {
        return validationFailure(input);
      }
    }
    
    return unauthorized("Must be logged in.");
  }
}
