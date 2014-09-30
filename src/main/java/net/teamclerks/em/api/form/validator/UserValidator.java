package net.teamclerks.em.api.form.validator;

import net.teamclerks.em.*;

import com.techempower.cache.*;
import com.techempower.gemini.form.*;

public class UserValidator implements FormElementValidator
{
  private final EntityStore store;
  
  public UserValidator()
  {
    this.store = EMApplication.getInstance().getStore();
  }

  @Override
  public void validate(FormElement element, FormSingleValidation val)
  {    
    if(element.getIntegerValue() < 1)
    {
      val.setRawError("User invalid.", 
          "User invalid", 
          "User invalid.");
    }
    
    // TODO - check if user is friends-only messaging.
  }
}
