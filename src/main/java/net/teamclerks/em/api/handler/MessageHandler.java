package net.teamclerks.em.api.handler;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.Message;
import net.teamclerks.em.api.form.MessageForm;
import net.teamclerks.em.auth.entity.User;

import com.google.common.collect.Maps;
import com.techempower.cache.EntityStore;
import com.techempower.gemini.form.FormValidation;
import com.techempower.gemini.path.MethodSegmentHandler;
import com.techempower.gemini.path.annotation.PathSegment;

public class MessageHandler
     extends MethodSegmentHandler<EMContext>
{
  private final EMApplication application;
  private final EntityStore   store;

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
      final MessageForm form = new MessageForm(this.application, user);
      form.setValues(context());
      final FormValidation validation = form.validate(context());
      
      if(validation.hasErrors())
      {
        json.put("success", false);
        json.put("validation", validation);
      }
      else
      {
        Message message = new Message();
        message.setCreated(new Date());
        message.setRead(false);
        message.setMessage(form.message.getStringValue());
        message.setRecipient(form.recipient.getIntegerValue());
        message.setSender(user.getId());
        
        store.put(message);
        
        json.put("success", true);
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
