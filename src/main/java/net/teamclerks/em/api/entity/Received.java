package net.teamclerks.em.api.entity;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.collection.*;
import com.techempower.data.annotation.*;
import com.techempower.helper.*;

@CachedEntity
public class Received extends Message
{  
  private int     sender;

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
  @Override
  public final Map<String,Object> view()
  {
    return new MutableNamedObjects()
      .put("id", this.getId())
      .put("read", this.isRead())
      .put("message", this.getMessage())
      .put("created", DateHelper.STANDARD_TECH_FORMAT.format(this.getCreated()))
      .putObject("sender", EMApplication.getInstance().getStore() 
          .get(User.class, this.getSender()).restrictedView())
      .asMap();
  }
}
