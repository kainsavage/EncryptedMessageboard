define(['jquery'], function($) {

  /**
   * Gets the latest messages for the currently logged in user.
   * 
   * @return {Promise<Object>} message array
   */
  function getLatestMessages() {
    return $.ajax({
      type: 'GET',
      url: '/api/message',
      dataType: 'json'
    });
  }
  
  /**
   * Gets the latest messages for the currently logged in user
   * sent from the given user id.
   * 
   * @return {Promise<Object>} message array
   */
  function getLatestMessagesFromUser(userId) {
    return $.ajax({
      type: 'GET',
      url: '/api/message/' + userId,
      dataType: 'json'
    });
  }
  
  /**
   * Attempts to send the given encrypted message to the given
   * user identified by userId.
   * 
   * @return {Promse<Object>} success or validation
   */
  function sendMessage(userId, encryptedMessage) {
    return $.ajax({
      type: 'POST',
      url: '/api/message',
      dataType: 'json'
    });
  }
  
  return {
    getLatestMessages: getLatestMessages
  };
});