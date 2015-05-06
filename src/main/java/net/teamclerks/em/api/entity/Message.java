package net.teamclerks.em.api.entity;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;
import com.techempower.data.annotation.CachedEntity;
import com.techempower.js.legacy.Visitor;
import com.techempower.js.legacy.VisitorFactory;
import com.techempower.js.legacy.Visitors;
import com.techempower.util.Identifiable;

@CachedEntity
public class Message 
  implements Identifiable, Comparable<Message>
{
  public static final VisitorFactory<Message> visitorFactory =
      new VisitorFactory<Message>()
      {
        @Override
        public Visitor visitor(Message object)
        {
          final Map<String,Object> json = Maps.newHashMap();
          
          json.put("id", object.getId());
          json.put("read", object.isRead());
          json.put("message", object.getMessage());
          json.put("created", object.getCreated());
          json.put("sender", object.getSender());
          json.put("recipient", object.getRecipient());
          
          return Visitors.forMaps().visitor(json);
        }
      };
  
  private int     id;
  private boolean read;
  private String  message;
  private Date    created;
  private int     sender;
  private int     recipient;
  
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
  
  public boolean isRead()
  {
    return this.read;
  }
  
  public void setRead(boolean read)
  {
    this.read = read;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public Date getCreated()
  {
    return created;
  }

  public void setCreated(Date created)
  {
    this.created = created;
  }

  public int getSender()
  {
    return sender;
  }

  public void setSender(int sender)
  {
    this.sender = sender;
  }

  public int getRecipient()
  {
    return recipient;
  }

  public void setRecipient(int recipient)
  {
    this.recipient = recipient;
  }

}
