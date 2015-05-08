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
   * Gets the message transcript between the user given by userId
   * and the currently logged in user.
   * 
   * @param {Number} the userId of the user for whom you would like
   *                 the message transcript.
   * @return {Promise<Object>} message array
   */
  function getMessageTranscriptForUser(userId) {
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
   * @param {Number} the userId to whom you are sending a message.
   * @param {String} the message encrypted with the public key belonging
   *                 to the user given by userId.
   * @param {String} the message encrypted with the public key belonging
   *                 to the currently logged in user.
   * @param {String}
   * @return {Promse<Object>} success or validation
   */
  function sendMessage(userId, encryptedMessage, encryptedTranscript) {
    return $.ajax({
      type: 'POST',
      url: '/api/message',
      dataType: 'json'
    });
  }
  
  return {
    getLatestMessages: getLatestMessages,
    getMessageTranscriptForUser: getMessageTranscriptForUser,
    sendMessage: sendMessage
  };
});