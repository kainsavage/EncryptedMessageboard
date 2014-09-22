package net.teamclerks.em.api.entity;

import java.util.*;

import com.google.common.collect.*;
import com.techempower.js.*;
import com.techempower.util.*;

public class Message implements Identifiable, Comparable<Message>
{
  public static final VisitorFactory<Message> visitorFactory =
      new VisitorFactory<Message>()
      {
        @Override
        public Visitor visitor(Message object)
        {
          final Map<String,Object> json = Maps.newHashMap();
          
          json.put("id", object.getId());
          json.put("message", object.getMessage());
          json.put("signature", object.getSignature());
          json.put("created", object.getCreated());
          
          return Visitors.forMaps().visitor(json);
        }
      };
  
  private int    id;
  private String message;
  private String signature;
  private Date   created;
  private String sender;
  
  @Override
  public int compareTo(Message m)
  {
    return this.getCreated().compareTo(m.getCreated());
  }

  @Override
  public int getId()
  {
    return this.id;
  }

  @Override
  public void setId(int identity)
  {
    this.id = identity;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public String getSignature()
  {
    return signature;
  }

  public void setSignature(String signature)
  {
    this.signature = signature;
  }

  public Date getCreated()
  {
    return created;
  }

  public void setCreated(Date created)
  {
    this.created = created;
  }

  public String getSender()
  {
    return sender;
  }

  public void setSender(String sender)
  {
    this.sender = sender;
  }

}
