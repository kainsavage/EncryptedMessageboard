define(['jquery'], function($) {

  /**
   * Gets all of the currently logged in user's
   * friends.
   *
   * @return {Promse<Object>}
   */
  function getFriends() {
    return $.ajax({
      type: 'GET',
      url: '/api/friend/'
    });
  }
  
  /**
   * Gets all pending friend requests for the
   * currently logged in user.
   * 
   * @return {Promise<Object>}
   */
  function getFriendRequests() {
    return $.ajax({
      type: 'GET',
      url: '/api/friend/request'
    });
  }

  /**
   * Requests that the given user accept a
   * friendship from the currently logged in
   * user.
   *
   * @param {Number} userId of new friend.
   * @return {Promise<Object>} { success: Boolean }
   */
  function requestFriendship(userId) {
    return $.ajax({
      type: 'POST',
      url: '/api/friend/request/' + userId,
      dataType: 'json'
    });
  }

  /**
   * Accepts the friendship request from the
   * user given by userId.
   *
   * @param {Number} userId of new friend.
   * @return {Promise<Object>} { success: Boolean }
   */
  function acceptFriendship(userId) {
    return $.ajax({
      type: 'PUT',
      url: '/api/friend/request/' + userId,
      dataType: 'json'
    });
  }

  /**
   * Removes the user given by userId from
   * the currently logged in user's friends.
   *
   * @param {Number} userId of the ex-friend.
   * @return {Promise<Object>}
   */
  function removeFriend(userId) {
    return $.ajax({
      type: 'DELETE',
      url: '/api/friend/' + userId,
      dataType: 'json'
    });
  }

  return {
    getFriends: getFriends,
    requestFriendship: requestFriendship,
    acceptFriendship: acceptFriendship,
    removeFriend: removeFriend
  };
});