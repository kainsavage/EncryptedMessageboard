/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em;

import com.techempower.gemini.*;
import com.techempower.gemini.email.outbound.*;
import com.techempower.util.*;

/**
 * An EmailTemplater for EncryptedMessageboard.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMEmailTemplater
     extends SimpleEmailTemplater
{

  //
  // Member variables.
  //

  private String fromEmailAddress = "kain@teamclerks.net";

  //
  // Member methods.
  //

  /**
   * Constructor.
   */
  public EMEmailTemplater(GeminiApplication application)
  {
    super(application);
  }

  /**
   * Configures this component.  Overload this method to load emails.
   */
  @Override
  public void configure(EnhancedProperties props)
  {
    super.configure(props);

    this.fromEmailAddress = props.get("FromEmailAddress", this.fromEmailAddress);
    getLog().log("E-mail author: " + this.fromEmailAddress);
  }

  /**
   * Gets the author e-mail address.
   */
  public String getEmailAuthor()
  {
    return this.fromEmailAddress;
  }

}   // End EMEmailTemplater.
