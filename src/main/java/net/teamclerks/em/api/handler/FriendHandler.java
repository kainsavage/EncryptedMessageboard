package net.teamclerks.em.api.handler;

import java.util.*;
import java.util.stream.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.gemini.path.annotation.*;

public class FriendHandler extends EMHandler
{
  /**
   * Public constructor.
   */
  public FriendHandler(EMApplication app)
  {
    super(app);
  }

  @Path("")
  @Get
  public boolean getFriends()
  {
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      return json(store().getRelation(Friends.class).rightValueList(user)
          .stream().map(u -> u.view()).collect(Collectors.toList()));      
    }
    return json();
  }
  
  @Path("{userId}")
  @Post
  public boolean makeFriendshipRequest(int userId)
  {
    return json();
  }
  
  @Path("{userId}")
  @Put
  public boolean acceptFriendshipRequest(int userId)
  {
    return json();
  }
  
  @Path("{userId}")
  @Delete
  public boolean deleteFriend(int userId)
  {
    return json();
  }
}
