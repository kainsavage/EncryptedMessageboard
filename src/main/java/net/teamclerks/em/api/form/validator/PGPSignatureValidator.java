package net.teamclerks.em.api.form.validator;

import com.techempower.gemini.form.*;

/**
* Private validator that aims to determine whether the signature being sent
* is actually a PGP signature.
*/
public class PGPSignatureValidator implements FormElementValidator
{
  @Override
  public void validate(FormElement element, FormSingleValidation val)
  {
    // TODO: figure out how to sign a message, then worry about this.
  }
}
