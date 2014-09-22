package net.teamclerks.em.api.form.validator;

import org.apache.commons.lang3.*;

import com.techempower.gemini.form.*;

/**
 * Private validator that aims to determine whether the message being sent
 * is actually a PGP message (or someone trying to mess with our service).
 */
public class PGPMessageValidator implements FormElementValidator
{
  private static final String BEGIN_PGP_MESSAGE = "-----BEGIN PGP MESSAGE-----";
  private static final String VERSION           = "Version: OpenPGP.js v.1.20130825";
  private static final String COMMENT           = "Comment: http://openpgpjs.org";
  private static final String END_PGP_MESSAGE   = "-----END PGP MESSAGE-----";
  
  @Override
  public void validate(FormElement element, FormSingleValidation val)
  {
    String encrypted = StringEscapeUtils.unescapeHtml4(element.getStringValue());
    
    // This isn't the best way to verify that we've gotten a PGP message,
    // but it's pretty good for now.
    String[] lines = encrypted.replaceAll("\r","").split("\n");
    if(!lines[0].equals(BEGIN_PGP_MESSAGE) ||
       !lines[1].equals(VERSION) ||
       !lines[2].equals(COMMENT) ||
       !lines[lines.length - 1].equals(END_PGP_MESSAGE))
    {
      val.setRawError("Message invalid.", 
          "Message invalid.", 
          "Message invalid.");
    }
  }
}
