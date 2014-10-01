/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em;

import net.teamclerks.em.api.handler.*;
import net.teamclerks.em.auth.*;

import com.techempower.*;
import com.techempower.cache.*;
import com.techempower.gemini.*;
import com.techempower.gemini.email.outbound.*;
import com.techempower.gemini.exceptionhandler.*;
import com.techempower.gemini.path.*;
import com.techempower.gemini.path.annotation.*;
import com.techempower.gemini.pyxis.*;

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
     extends GeminiApplication
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
  
  @Override
  protected EntityStore constructEntityStore()
  {    
    return new EMStore(this, getConnectorFactory());
  }

  /**
   * Constructs a Dispatcher.
   */
  @Override
  protected Dispatcher constructDispatcher()
  {
    final PathDispatcher.Configuration<EMContext> config =
        new PathDispatcher.Configuration<>();

    final UserHandler          userHandler = new UserHandler(this);
    final ProfileHandler       profileHandler = new ProfileHandler(this);
    final PasswordResetHandler pwHandler = new PasswordResetHandler(this);
    final MessageHandler       messageHandler = new MessageHandler(this);
        
    config
          .add("api",new DispatchSegment<EMContext>()
            .add("user", userHandler)
            .add("password", pwHandler)
            .add("profile", profileHandler)
            .add("message", messageHandler)
          )
          .add(getMonitor().getListener())
          .add(new BasicExceptionHandler(this))
          .add(new NotificationExceptionHandler(this))
          .setDefault(new MethodPathHandler<EMContext>(this) {
            @PathDefault
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

