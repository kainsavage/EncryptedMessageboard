package net.teamclerks.em;

import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.cache.*;
import com.techempower.data.*;

public class EMStore extends EntityStore
{
  public final CachedRelation<User, User> friends;
  
  public EMStore(EMApplication application, ConnectorFactory factory)
  {
    super(application, factory);
    
    this.registerGroups();

    this.friends = register(CachedRelation
        .of(User.class, User.class)
        .table("friends")
        .leftColumn("usera")
        .rightColumn("userb"));
  }
  
  /**
   * Registers all the Groups.
   */
  private void registerGroups()
  {
    this.register(CacheGroup.of(Message.class));
  }
}
