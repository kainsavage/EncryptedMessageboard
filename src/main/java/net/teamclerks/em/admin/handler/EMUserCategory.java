/**
 * Copyright (c) 2013
 * TeamClerks
 * All Rights Reserved.
 *
 * EncryptedMessageboard
 */

package net.teamclerks.em.admin.handler;

import com.techempower.gemini.*;
import com.techempower.gemini.admin.standard.users.*;
import com.techempower.gemini.form.*;
import com.techempower.gemini.pyxis.*;
import net.teamclerks.em.auth.entity.*;

/**
 * EncryptedMessageboard-specific implementation of the
 * UserCategory of administrative functionality.
 *
 * Development history:
 *   2013-10-07 - ms - Created
 *
 * @author (username)
 */
public class EMUserCategory
     extends UserCategory
{

  /**
   * Apply a submitted and validated form's edits to a User object.
   */
  @Override
  public boolean updateUser(BasicWebUser bWuser, Form form, Context context)
  {
    boolean success = super.updateUser(bWuser, form, context);
    if (success)
    {
      // Set custom fields of the user from the provided Form object.
      // Note that the Form object is already assumed to be validated.

      //User user = (User)bWuser;
      //user.setFavoriteColor(form.getStringValue("favoritecolor"));
    }

    return success;
  }

  /**
   * Save a just-edited User object.
   */
  @Override
  public boolean saveUser(GeminiApplication app, BasicWebUser user, Form form, Context context)
  {
    boolean success = super.saveUser(app, user, form, context);
    if (success)
    {
      // Do any custom processing after the update of the data entity.

      // Example: Save the public key to a separate table.
      //String publicKey = form.getStringValue("public-key");
      //((User)user).setPublicKey(publicKey);
    }

    return success;
  }

  /**
   * Creates the Form for adding and editing users.
   */
  @Override
  protected Form buildAddEditUserForm(GeminiApplication app, Context context, BasicWebUser bWuser)
  {
    User user = (User)bWuser;

    Form form = super.buildAddEditUserForm(app, context, user);

    /*
    form.addElement(new FormTextField("favoritecolor", user.getFavoriteColor(), true, 30, 50));

    // A custom validator that does something such as validate the favorite
    // color selection.  Note that you'd typically do that sort of validation
    // on the form element, but we're just showing an example of adding
    // form-level validation.
    form.addValidator(new FormValidator(user)
    {
      public void validate(Form form, Context context, FormSingleValidation val)
      {
        String favoriteColor = form.getStringValue("favoritecolor");
        if (... some check fails ...)
        {
          val.setError("Not a valid color.", "Please change your favorite color.", "This is not a valid color.");
        }
      }
    });
    */

    return form;
  }

}
