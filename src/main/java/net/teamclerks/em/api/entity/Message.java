package net.teamclerks.em.api.entity;

import java.util.*;

import com.techempower.util.*;

public abstract class Message implements Identifiable
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
    return read;
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

  /**
   * Simple public view map.
   */
  public abstract Map<String,Object> view();
}
