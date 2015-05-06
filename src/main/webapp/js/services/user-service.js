define(['jquery', 'ko'], function($, ko) {

  /**
   * Attempts to log in the user with the given username and password.
   *
   * @param  {String} username
   * @param  {String} password
   * @return {Promise} The promise wrapped around the login call.
   * @static
   */
  function login(username, password) {
    return $.ajax({
      type: 'POST',
      url: '/api/login',
      data: 'lhuser=' + username + '&lhpass=' + password,
      dataType: 'json'
    });
  }

  /**
   * Logs the user out.
   *
   * @return {Promise} The promise wrapped around the logout body.
   */
  function logout() {
    return $.ajax({
      type: 'POST',
      url: '/api/logout',
      dataType: 'json'
    });
  }

  /**
   * Gets the currently logged in user information from the server.
   *
   * @return {Promise<Object>} { id: Number, username: String }
   */
  function getUser() {
    return $.ajax({
      type: 'GET',
      url: '/api/user',
      dataType: 'json'
    });
  }

  /**
   * Saves the given user data for the currently logged in user.
   *
   * @param {Object} the user's data to save
   * @return {Promise<Object>}
   */
  function editUser(user) {
    return $.ajax({
      type: 'PUT',
      url: '/api/user',
      data: user
    });
  }

  return {
    login: login,
    logout: logout,
    getUser: getUser,
    editUser: editUser
  };
});