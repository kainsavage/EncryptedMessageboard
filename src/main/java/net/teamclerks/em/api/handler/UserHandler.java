/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.*;

import net.teamclerks.em.*;
import net.teamclerks.em.api.entity.*;
import net.teamclerks.em.api.validator.*;
import net.teamclerks.em.auth.entity.*;

import org.apache.commons.lang3.*;

import com.google.common.collect.*;
import com.techempower.gemini.input.*;
import com.techempower.gemini.input.validator.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;
import com.techempower.js.*;
import com.techempower.js.legacy.*;

public class UserHandler
  extends MethodUriHandler<EMContext>
{
  private static final JavaScriptWriter UserWriter = LegacyJavaScriptWriter.custom()
      .addVisitorFactory(
          User.class, User.visitorFactory)
      .build();
  
  /**
   * Validator for User edit.
   * @param application
   */
  private final ValidatorSet editUserValidator = new ValidatorSet(
    new LengthValidator("firstname", 1, 45),
    new AlphanumericValidator("firstname"),
    new LengthValidator("lastname", 1, 45),
    new AlphanumericValidator("lastname"),
    new EmailValidator("email"),
    new LengthValidator("email", 5, 255),
    new LengthValidator("publicKey", 512, 2048),
    new PGPPublicKeyValidator("publicKey")
  );
  
  /**
   * Validator for User registration.
   * @param application
   */
  private final ValidatorSet registerValidator = new ValidatorSet(
    new LengthValidator("username", 5, 45),
    new UniquenessValidator("username", User.class, "getUserUsername"),
    new PasswordComplexityValidator("password", app().getSecurity(), "username")
  );
  
  /**
   * Public constructor.
   * @param application
   */
  public UserHandler(EMApplication application)
  {
    super(application, "UserHandler", UserWriter);
  }
  
  ///
  /// Public methods.
  ///
  
  @Path("")
  @Get
  public boolean getUser()
  {    
    final User user = (User) app().getSecurity().getUser(context());
    
    if(user != null)
    {
      return json(user.view());
    }
    
    return json(Maps.newHashMap());
  }
  
  @Path("{userId}")
  @Get
  public boolean getUser(int userId)
  {
    final User user = app().getStore().get(User.class, userId);
    
    if(user != null)
    {
      return json(user.view());
    }
    
    return json(Maps.newHashMap());
  }
  
  @Path("")
  @Put
  public boolean editUser()
  {
    final Map<String,Object> json = Maps.newHashMap();

    final User user = (User) app().getSecurity().getUser(context());
    
    if(user != null)
    {
      final ValidatorSet editValidator = new ValidatorSet(
          this.editUserValidator,
          new UniquenessValidator("email", User.class, "getUserEmail", user)
      );
      final Input input = editValidator.process(context());
      if(input.passed())
      {
        // Success
        user.setPublicKey(StringEscapeUtils.unescapeHtml4(input.values().get("publicKey")));
        user.setFriendsOnlyMessaging(input.values().getBoolean("friendsOnly"));
        user.setUserFirstname(input.values().get("firstname").trim());
        user.setUserLastname(input.values().get("lastname").trim());
        user.setUserEmail(input.values().get("email").trim());
        
        app().getStore().put(user);
        
        json.put("success", true);
      }
      else
      {
        return validationFailure(input);
      }
    }
    
    return json(json);
  }
  
  @Path("")
  @Post
  public boolean registerUser()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final Input input = this.registerValidator.process(context());
    
    if (input.passed())
    {
      // Success!
      User user = (User) app().getSecurity().constructUser();
      user.initialize();
      user.setUserUsername(input.values().get("username"));
      user.setUserPassword(input.values().get("password"));

      app().getStore().put(user);
      app().getSecurity().login(context(), user, false);
      
      json.put("success", true);
      json.put("redirect", "/profile");
    }
    else
    {
      return validationFailure(input);
    }
    
    return json(json);
  }
  
  @Path("friends")
  @Get
  public boolean getFriends()
  {
    final Map<String,Object> json = Maps.newHashMap();
    
    final User user = (User) app().getSecurity().getUser(context());
    if(user != null)
    {
      final Collection<User> friends = 
          app().getStore().getRelation(Friends.class).rightValueList(user.getId());
      
      json.put("friends", friends);
    }
    
    return json(json);
  }
}
