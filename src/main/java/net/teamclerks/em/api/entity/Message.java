package net.teamclerks.em.api.entity;

import java.util.*;

import com.techempower.collection.*;
import com.techempower.data.annotation.*;
import com.techempower.helper.*;
import com.techempower.util.*;

@CachedEntity
public class Message 
  implements Identifiable
{
  public static final Comparator<Message> NEWEST_TO_OLDEST = new Comparator<Message>()
  {
    @Override
    public int compare(Message o1, Message o2)
    {
      return o2.getCreated().compareTo(o1.getCreated());
    }
  };
  
  private int     id;
  private boolean read;
  private String  message;
  private Date    created;
  private int     sender;

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

  /**
   * Simple public view map.
   */
  public final Map<String,Object> view()
  {
    return new MutableNamedObjects()
      .put("id", this.getId())
      .put("read", this.isRead())
      .put("message", this.getMessage())
      .put("created", DateHelper.STANDARD_TECH_FORMAT.format(this.getCreated()))
      .put("sender", this.getSender())
      .asMap();
  }
}
