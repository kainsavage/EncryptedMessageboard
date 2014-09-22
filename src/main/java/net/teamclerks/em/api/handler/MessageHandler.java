package net.teamclerks.em.api.handler;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.api.form.*;

import com.google.common.collect.*;
import com.techempower.cache.*;
import com.techempower.gemini.form.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;

public class MessageHandler extends MethodPathHandler<EMContext>
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
    
    final MessageForm form = new MessageForm(this.application);
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
      message.setMessage(form.message.getStringValue());
      message.setSignature(form.signature.getStringValue());
      message.setSender(context().getClientID());
      
      store.put(message);
      
      json.put("success", true);
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
