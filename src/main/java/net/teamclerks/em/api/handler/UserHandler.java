/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.api.handler;

import java.util.Collection;
import java.util.Map;

import net.teamclerks.em.EMApplication;
import net.teamclerks.em.EMContext;
import net.teamclerks.em.api.entity.Friends;
import net.teamclerks.em.api.validator.PGPPublicKeyValidator;
import net.teamclerks.em.auth.EMSecurity;
import net.teamclerks.em.auth.entity.User;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.common.collect.Maps;
import com.techempower.gemini.input.Input;
import com.techempower.gemini.input.ValidatorSet;
import com.techempower.gemini.input.validator.AlphanumericValidator;
import com.techempower.gemini.input.validator.EmailValidator;
import com.techempower.gemini.input.validator.LengthValidator;
import com.techempower.gemini.input.validator.PasswordComplexityValidator;
import com.techempower.gemini.input.validator.UniquenessValidator;
import com.techempower.gemini.path.MethodUriHandler;
import com.techempower.gemini.path.annotation.Get;
import com.techempower.gemini.path.annotation.Path;
import com.techempower.gemini.path.annotation.Post;
import com.techempower.gemini.path.annotation.Put;
import com.techempower.js.JavaScriptWriter;
import com.techempower.js.legacy.LegacyJavaScriptWriter;

public class UserHandler
  extends MethodUriHandler<EMContext>
{
  private static final JavaScriptWriter UserWriter = LegacyJavaScriptWriter.custom()
      .addVisitorFactory(
          User.class, User.visitorFactory)
      .build();
  
  private final EMApplication application;
  private final EMSecurity security;
  
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
    new UniquenessValidator("email", User.class, "getUserEmail"),
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
    
    this.application = application;
    this.security = this.application.getSecurity();
  }
  
  ///
  /// Public methods.
  ///
  
  @Path("")
  @Get
  public boolean getUser()
  {    
    final User user = this.security.getUser(context());
    
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

    final User user = this.security.getUser(context());
    
    if(user != null)
    {
      final Input input = this.editUserValidator.process(context());
      if(input.passed())
      {
        // Success
        user.setPublicKey(StringEscapeUtils.unescapeHtml4(input.values().get("publicKey")));
        user.setFriendsOnlyMessaging(input.values().getBoolean("friendsOnly"));
        user.setUserFirstname(input.values().get("firstname").trim());
        user.setUserLastname(input.values().get("lastname").trim());
        user.setUserEmail(input.values().get("email").trim());
        
        this.application.getStore().put(user);
        
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
      User user = this.security.constructUser();
      user.initialize();
      user.setUserUsername(input.values().get("username"));
      user.setUserPassword(input.values().get("password"));

      application.getStore().put(user);
      application.getSecurity().login(context(), user, false);
      
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
    
    final User user = this.security.getUser(context());
    if(user != null)
    {
      final Collection<User> friends = 
          application.getStore().getRelation(Friends.class).rightValueList(user.getId());
      
      json.put("friends", friends);
    }
    
    return json(json);
  }
}
