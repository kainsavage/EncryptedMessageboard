package net.teamclerks.em.api.handler;

import java.util.*;
import java.util.stream.*;

import javax.servlet.http.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.collector.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.auth.entity.*;

import com.google.common.collect.*;
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
      return json(
          // People who I have as a friend...
          store().getRelation(Friends.class).rightValueList(user).stream()
          // and also have me as a friend.
          .filter(u -> store().getRelation(Friends.class).leftValueList(user).contains(u))
          // get the list of JSON.
          .map(u -> u.view()).collect(Collectors.toList()));
    }
    return json();
  }
  
  @Path("request")
  @Get
  public boolean getFriendRequests()
  {
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      return json(
          // People who have me as a friend...
          store().getRelation(Friends.class).leftValueList(user).stream()
          // but whom I do not have as a friend.
          .filter(u -> !store().getRelation(Friends.class).rightValueList(user).contains(u))
          // get the list of JSON.
          .map(u -> u.view()).collect(Collectors.toList()));
    }
    
    return json();
  }
  
  @Path("{userId}")
  @Post
  public boolean makeFriendshipRequest(int userId)
  {
    final Map<String,Object> json = Maps.newHashMap();
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      final User friend =
          // People who are my friends...
          store().getRelation(Friends.class).rightValueList(user).stream()
          // and have the given userId...
          .filter(u -> u.getId() == userId)
          // get that user.
          .collect(EMCollectors.firstOrNull());
      if (friend != null)
      {
        if (store().getRelation(Friends.class).leftValueList(friend).contains(user))
        {
          // He is already my friend.
          return error(HttpServletResponse.SC_CONFLICT, "User is already a friend.");
        }
        
        // He is already pending friendship.
        return error(HttpServletResponse.SC_CONFLICT, "User is pending friendship.");
      }
      
      // Add him as a friend of mine; he will need to accept this at some point.
      store().getRelation(Friends.class).add(user, friend);
      
      json.put("success", true);
    }
    
    return json(json);
  }
  
  @Path("request/{userId}")
  @Put
  public boolean acceptFriendshipRequest(int userId)
  {
    final Map<String,Object> json = Maps.newHashMap();
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      final User friend = 
          // People who have me as a friend...
          store().getRelation(Friends.class).leftValueList(user).stream()
          // but whom I do not have as a friend...
          .filter(u -> !store().getRelation(Friends.class).rightValueList(user).contains(u))
          // and finally, the user in question.
          .filter(u -> u.getId() == userId)
          // get that user.
          .collect(EMCollectors.firstOrNull());
      
      if (friend == null)
      {
        // If friend is null, then this is probably a bogus request.
        return error(HttpServletResponse.SC_CONFLICT, "User did not make friend request.");
      }
      
      // He's already got me as a friend; add him as my friend.
      store().getRelation(Friends.class).add(user, friend);
      
      json.put("success", true);
    }
    
    return json(json);
  }
  
  @Path("{userId}")
  @Delete
  public boolean deleteFriend(int userId)
  {
    final Map<String,Object> json = Maps.newHashMap();
    final User user = app().getSecurity().getUser(context());
    
    if (user != null)
    {
      // Remove him from my friends...
      store().getRelation(Friends.class).remove(user, userId);
      // and remove me from his friends.
      store().getRelation(Friends.class).remove(userId, user);
      
      json.put("success", true);
    }
    
    return json(json);
  }
}
