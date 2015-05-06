package net.teamclerks.em.api.handler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.Message;
import net.teamclerks.em.api.validator.MessageToUserValidator;
import net.teamclerks.em.api.validator.PGPMessageValidator;
import net.teamclerks.em.auth.entity.User;

import com.google.common.collect.Maps;
import com.techempower.cache.EntityStore;
import com.techempower.gemini.input.Input;
import com.techempower.gemini.input.ValidatorSet;
import com.techempower.gemini.input.validator.LengthValidator;
import com.techempower.gemini.path.MethodSegmentHandler;
import com.techempower.gemini.path.annotation.PathSegment;

public class MessageHandler
     extends MethodSegmentHandler<EMContext>
{
  private final EMApplication application;
  private final EntityStore   store;
  
  private final ValidatorSet postMessage = new ValidatorSet(
    new LengthValidator("message", 1, 4096),
    new PGPMessageValidator("message"),
    new MessageToUserValidator("recipient")
  );

  public MessageHandler(EMApplication app)
  {
    super(app);
    this.application = app;
    this.store = this.application.getStore();
  }

  @PathSegment
  public boolean latest()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    List<Message> latest = store.list(Message.class);
    Collections.sort(latest);
    
    json.put("latest", latest);
    
    return json(json);
  }
  
  @PathSegment
  public boolean post()
  {
    final Map<String,Object> json = Maps.newHashMap();
    final User user = this.application.getSecurity().getUser(context());    
    
    if(user != null)
    {
      Input input = this.postMessage.process(context());
      
      if(input.passed())
      {
        Message message = new Message();
        message.setCreated(new Date());
        message.setRead(false);
        message.setMessage(input.values().get("message"));
        message.setRecipient(input.values().getInt("recipient"));
        message.setSender(user.getId());
        
        store.put(message);
        
        json.put("success", true);
      }
      else
      {
        return validationFailure(input);
      }
    }
    
    return json(json);
  }
  
  @PathSegment
  public boolean list()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    return json(json);
  }
  
  @PathSegment
  public boolean get()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    return json(json);
  }
}
