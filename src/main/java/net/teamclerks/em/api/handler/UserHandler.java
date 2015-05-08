/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import net.teamclerks.em.*;
import net.teamclerks.em.api.validator.*;
import net.teamclerks.em.auth.entity.*;

import org.apache.commons.lang3.*;

import com.techempower.gemini.input.*;
import com.techempower.gemini.input.validator.*;
import com.techempower.gemini.path.annotation.*;

public class UserHandler extends EMHandler
{  
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
    super(application);
  }
  
  ///
  /// Public methods.
  ///
  
  @Path("")
  @Get
  public boolean getUser()
  {    
    final User user = app().getSecurity().getUser(context());
    
    if(user != null)
    {
      return json(user.view());
    }
    
    return unauthorized("Must be logged in.");
  }
  
  @Path("{userId}")
  @Get
  public boolean getUser(int userId)
  {
    final User user = app().getStore().get(User.class, userId);
    
    if(user != null)
    {
      return json(user.restrictedView());
    }
    
    return notFound("No such user.");
  }
  
  @Path("")
  @Put
  public boolean editUser()
  {
    final User user = app().getSecurity().getUser(context());
    
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
        
        return json();
      }
      else
      {
        return validationFailure(input);
      }
    }
    
    return unauthorized("Must be logged in.");
  }
  
  @Path("")
  @Post
  public boolean registerUser()
  {
    final Input input = this.registerValidator.process(context());
    
    if (input.passed())
    {
      // Success!
      User user = app().getSecurity().constructUser();
      user.initialize();
      user.setUserUsername(input.values().get("username"));
      user.setUserPassword(input.values().get("password"));

      app().getStore().put(user);
      app().getSecurity().login(context(), user, false);
      
      return json();
    }
    
    return validationFailure(input);
  }
}
