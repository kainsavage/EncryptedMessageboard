define(['jquery'], function($) {

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
   * Gets the user's information from the server.
   *
   * @param {Number} userId to fetch or null for logged in user.
   * @return {Promise<Object>} { id: Number, username: String,
   *    firstname: String, lastname: String, email: String,
   *    friendsOnly: Boolean, publicKey: String }
   */
  function getUser(userId) {
    var url = '/api/user';
    if (userId) {
      url = url + '/' + userId;
    }
    return $.ajax({
      type: 'GET',
      url: url,
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
      data: user,
      dataType: 'json'
    });
  }

  return {
    login: login,
    logout: logout,
    getUser: getUser,
    editUser: editUser
  };
});