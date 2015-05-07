package net.teamclerks.em.api.handler;

import net.teamclerks.em.*;

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
