package net.teamclerks.em.api.handler;

import java.util.*;
import java.util.stream.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.api.entity.relation.*;
import net.teamclerks.em.api.validator.*;
import net.teamclerks.em.auth.entity.*;

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
        store().getRelation(UserReceivedMessages.class).rightValueList(user).stream()
          // sorted from newest to oldest...
          .sorted(Message.NEWEST_TO_OLDEST)
          // gotten as json.
          .map(m -> m.view()).collect(Collectors.toList()));
    }
    
    return unauthorized("Must be logged in.");
  }
  
  @Path("{userId}")
  @Get
  public boolean getMessageTranscriptForUser(int userId)
  {
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      List<Message> received = 
          store().getRelation(UserReceivedMessages.class).rightValueList(user).stream()
          .filter(m -> m.getSender() == userId)
          .collect(Collectors.toList());
      List<Message> sent =
          store().getRelation(UserSentMessages.class).rightValueList(user).stream()
          .filter(m -> m.getRecipient() == userId)
          .collect(Collectors.toList());
      
      received.addAll(sent);
      
      return json(
        // Get my messages...
        received.stream()
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
    final User user = app().getSecurity().getUser(context());    
    
    if(user != null)
    {
      final Input input = this.postMessage.process(context());
      
      if(input.passed())
      {
        final Date created = new Date();
        final Received received = new Received();
        received.setCreated(created);
        received.setRead(false);
        received.setMessage(input.values().get("message"));
        received.setSender(user.getId());
        // Save the message
        store().put(received);
        // Add the message to the recipient's message list.
        store().getRelation(UserReceivedMessages.class)
          .add(input.values().getInt("recipient"), received);
        
        final Sent sent = new Sent();
        sent.setCreated(created);
        // You wrote this, so you've read it.
        sent.setRead(true);
        sent.setMessage(input.values().get("sent"));
        sent.setRecipient(input.values().getInt("recipient"));
        // Save the message
        store().put(sent);
        // Add the message to your message list.
        store().getRelation(UserSentMessages.class).add(user, sent);
        
        return json();
      }
      else
      {
        return validationFailure(input);
      }
    }
    
    return unauthorized("Must be logged in.");
  }
}
