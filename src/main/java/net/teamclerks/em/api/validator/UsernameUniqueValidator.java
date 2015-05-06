package net.teamclerks.em.api.validator;

import net.teamclerks.em.*;
import net.teamclerks.em.auth.entity.*;

import com.techempower.cache.*;
import com.techempower.gemini.form.*;
import com.techempower.helper.*;

public class UsernameUniqueValidator implements FormElementValidator
{
  private final EntityStore store;
  
  public UsernameUniqueValidator()
  {
    this.store = EMApplication.getInstance().getStore();
  }

  @Override
  public void validate(FormElement element, FormSingleValidation val)
  {    
    if(StringHelper.isEmptyTrimmed(element.getStringValue()))
    {
      val.setRawError("Username invalid.", 
          "Username invalid", 
          "Username invalid.");
    }
    else if (!store.list(User.class, "getUserUsername", StringHelper.trim(element.getStringValue())).isEmpty())
    {
      val.setRawError("Username already taken.", 
          "Username already taken.", 
          "Username already taken.");
    }
  }
}
