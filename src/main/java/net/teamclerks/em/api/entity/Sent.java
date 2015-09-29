package net.teamclerks.em.api.entity;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.collection.*;
import com.techempower.data.annotation.*;
import com.techempower.helper.*;

@CachedEntity
public class Sent extends Message
{
  private int     recipient;

  public int getRecipient()
  {
    return recipient;
  }

  public void setRecipient(int recipient)
  {
    this.recipient = recipient;
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
      .putObject("recipient", EMApplication.getInstance().getStore() 
          .get(User.class, this.getRecipient()).restrictedView())
      .asMap();
  }

}
