em.provide("em.crypto")

em.crypto = (function() {
  'use strict';

  var key = {}; // Your openpgp-generated key 

  /**
   * Initializes the gathers/sets values stored in localStorage.
   */
  function init() {
    // Ensure that localStorage and crypto are supported
    if(!supportsHTML5LocalStorage() ||
       !supportsCrypto) {
      return false;
    }
    
    // Initialize openpgp
    openpgp.init();
    
    // Initialize key or leave as-is for first-visit.
    if(localStorage["em.crypto.key"]) {
      key = JSON.parse(localStorage["em.crypto.key"]);
    }
    
    return true;
  }
  
  /**
   * Returns whether the VM running this script has access to localstorage.
   */
  function supportsHTML5LocalStorage() {
    try {
      return 'localStorage' in window && window['localStorage'] !== null;
    }
    catch (err) {
      return false;
    }
  }
  
  /**
   * Returns whether the VM running this script has access to crypto.
   */
  function supportsCrypto() {
    try {
      return 'crypto' in window && window['crypto'] !== null;
    }
    catch (err) {
      return false;
    }
  }
  
  /**
   * A private helper function for saving the key whenever it is updated.
   */
  function saveKey() {
    localStorage["em.crypto.key"] = JSON.stringify(key);
  }


  /**
   * Returns an object with the encrypted message for the public key.
   */
  function encryptMessage(publicKey, plaintextMessage) {
    var encrypted = null;

    try {
      encrypted = openpgp.write_encrypted_message(publicKey, plaintextMessage);

      return {
        message : encrypted,
        success : true
      };
    }
    catch (err) {
      // Well this is bad; we have a public key for this user but it's corrupted...
    }

    return {
      message: plaintextMessage,
      success: false
    };
  }
  
  /**
   * 
   */
  function encryptMessageWithPublicKeyArmored(publicKeyArmored, plaintextMessage) {
    var publicKey = openpgp.read_publicKey(publicKeyArmored);
    
    return encryptMessage(publicKey, plaintextMessage);
  }

  /**
   * Returns an object with the decrypted message for this user.
   */
  function decryptMessage(privateKey, encryptedMessage) {
    var message = null;
    
    if(privateKey === null || privateKey === undefined) {
      privateKey = openpgp.read_privateKey(key.privateKeyArmored)[0];
    }

    if(encryptedMessage === null || encryptedMessage === undefined || encryptedMessage === '') {
      return {
        message: encryptedMessage,
        success: false
      };
    }

    message = openpgp.read_message(encryptedMessage);

    if(message == null || message == undefined) {
      return {
        message: encryptedMessage,
        success: false
      };
    }

    return {
      message: message[0].decrypt({key: privateKey, keymaterial: privateKey.privateKeyPacket}, message[0].sessionKeys[0]),
      success: true
    };
  }
  
  /**
   * 
   */
  function decryptMessageWithPrivateKeyArmored(privateKeyArmored, encryptedMessage) {
    var privateKey = openpgp.read_privateKey(privateKeyArmored)[0];
    
    return decryptMessage(privateKey, encryptedMessage);
  }
  
  /**
   * Determines whether the key object, upon which this entire 
   * library relies, exists in a valid state. Generally, this
   * should be queried before calling generateKeypair because
   * generateKeypair will override said key, if it exists. 
   */
  function keyExists() {
    return key !== null && key !== undefined &&
      key.privateKeyArmored !== undefined && key.privateKeyArmored !== null &&
      key.publicKeyArmored !== undefined && key.publicKeyArmored !== null
  }
  
  /**
   * Gets a copy of the key for this user.
   * 
   * NOTE: this only generates a copy; any subsequent changes
   * to the returned value will NOT be propagated forward.
   */
  function getKey() {
    return JSON.parse(JSON.stringify(key));
  }
  
  /**
   * Generates a key to be used by the application for this user.
   * 
   * Note: this is a VERY slow operation and with sufficient bit-
   * length, has the potential to take a very very long time. So
   * long, in fact, that we use a web worker to do the work.
   * 
   * IMPORTANT: Make sure that the key does indeed not exist
   * before calling this function. This function will overwrite
   * a potentially existing key, upon which this library relies.
   * This means that if you call generateKeypair and the user
   * has a key already, any messages that were decryptable before
   * will suddenly be non-decryptable since a new private key is
   * going to be generated and set here.
   */
  function generateKeypair(numBits) {
    key = openpgp.generate_key_pair(1, numBits);
    key.friends = [];

    saveKey();
    
    return JSON.parse(JSON.stringify(key));
  }
  
  /**
   * Adds a copy of the given friend to this keyring.
   * 
   * Note: This is only going to add a copy of the passed friend
   * to the keyring, so subsequent changes to the passed friend will
   * not impact the actual friend in the keyring.
   */
  function addFriend(friend) {
    key.friends[key.friends.length] = JSON.parse(JSON.stringify(friend));

    saveKey();
  }
  
  /**
   * Returns a copy of the friends associated with this keyring.
   * 
   * Note: This is ONLY a copy of this data and therefore considered
   * to be "read-only" in so far as any updates made to the data
   * returned from this function will have zero effect on the actual
   * friends data and subsequent calls to the friends array will not
   * have the aforementioned changes.
   */
  function listFriends() {
    return JSON.parse(JSON.stringify(key.friends));
  }
  
  /**
   * Applies our signature to the given plaintextMessage for the
   * purpose of the recipient being able to verify that we are
   * the actual originator of the message.
   */
  function signMessage(plaintextMessage) {
    // TODO
  }
  
  /**
   * Deletes the known key for this user. Mostly, this is for testing
   * and/or receiving "burn notes" that will be lost to the annals of
   * time once the private key is destroyed.
   * 
   * Note: In practice, this needs to be EXCEEDINGLY difficult for a
   * real user to invoke, as calling this function with a long-time
   * user would ostensibly destroy their message history unto their
   * self. There is no feasible way to recreate a key for any given
   * user (which is why this site exists; that's the security point).
   */
  function deleteKey() {
    key.publicKeyArmored = null;
    key.privateKeyArmored = null;
    key.friends = [];

    saveKey();
  }

  // Run the initialization function and return.
  return init() ? {
    keyExists : keyExists,
    getKey : getKey,
    deleteKey : deleteKey,
    addFriend : addFriend,
    listFriends : listFriends,
    generateKeypair : generateKeypair,
    encryptMessage : encryptMessage,
    encryptMessageWithPublicKeyArmored : encryptMessageWithPublicKeyArmored,
    decryptMessage : decryptMessage,
    decryptMessageWithPrivateKeyArmored : decryptMessageWithPrivateKeyArmored,
    signMessage : signMessage
  } : null;

}());