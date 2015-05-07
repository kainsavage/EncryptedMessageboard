/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em;

import net.teamclerks.em.admin.handler.*;
import net.teamclerks.em.api.handler.*;
import net.teamclerks.em.auth.*;

import com.techempower.*;
import com.techempower.gemini.*;
import com.techempower.gemini.email.outbound.*;
import com.techempower.gemini.exceptionhandler.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;
import com.techempower.gemini.pyxis.*;
import com.techempower.gemini.pyxis.handler.*;

/**
 * EMApplication.  As a subclass of GeminiApplication, this
 * class acts as the central "hub" of references to components such as
 * the Dispatcher, Security, and EntityStore.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMApplication
     extends ResinGeminiApplication
//  implements PyxisApplication
{

  //
  // Static variables.
  //

  private static final EMApplication INSTANCE = new EMApplication();

  //
  // Member methods.
  //

  /**
   * Constructor.  This method can be extended to construct references
   * to custom components for the application.
   */
  public EMApplication()
  {
    super();
  }

  /**
   * Constructs an application-specific Context, for an inbound request.
   * This is overloaded to return a custom EMContext.
   */
  @Override
  public EMContext getContext(Request request)
  {
    return new EMContext(
        request,
        this
    );
  }

  /**
   * Return and application-specific reference to the Security manager.
   */
  @Override
  public EMSecurity getSecurity()
  {
    return (EMSecurity)super.getSecurity();
  }

  //
  // Protected component constructors.
  //

  /**
   * Constructs a Version reference.  This is overloaded to return a
   * custom EMVersion object.
   */
  @Override
  protected Version constructVersion()
  {
    return new EMVersion();
  }

  /**
   * Constructs a Security reference.  This is overloaded to return a
   * custom EMSecurity object.
   */
  @Override
  protected PyxisSecurity constructSecurity()
  {
    return new EMSecurity(this);
  }

  /**
   * Constructs a Dispatcher.
   */
  @Override
  protected Dispatcher constructDispatcher()
  {
    final PathDispatcher.Configuration<EMContext> config =
        new PathDispatcher.Configuration<>();
        
    config
          .add("api",new DispatchSegment<EMContext>()
            // Gemini built-in handlers
            .add("login", new LoginHandler<EMContext>(this))
            .add("logout", new LogoutHandler<EMContext>(this))
            .add("password-reset", new PasswordResetHandler<EMContext>(this))
            .add("admin", new EMGeminiAdminHandler(this))
            // Normal handlers
            .add("user", new UserHandler(this))
            .add("message", new MessageHandler(this))
            .add("friend", new FriendHandler(this))
          )
          // Gemini built-in listeners
          .add(getMonitor().getListener())
          // Gemini built-in handlers
          .add(new BasicExceptionHandler(this))
          .add(new NotificationExceptionHandler(this))
          // Default handler
          .setDefault(new MethodUriHandler<EMContext>(this) {
            @Path("*")
            public boolean everything()
            {
              return notFound("File not found.");
            }
          });

    return new PathDispatcher<>(this, config);
  }

  /**
   * Constructs an EmailTemplater reference.  This is overloaded to return
   * a custom EMEmailTemplater object.
   */
  @Override
  protected EmailTemplater constructEmailTemplater()
  {
    return new EMEmailTemplater(this);
  }

  //
  // Static methods.
  //

  /**
   * Get the single running instance of this application.
   */
  public static EMApplication getInstance()
  {
    return INSTANCE;
  }

}   // End EMApplication.

