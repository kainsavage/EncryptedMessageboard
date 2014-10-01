em.provide("em.crypto");

em.crypto = (function() {
  'use strict';

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///                                              PRIVATE                                                   ///
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  var _key          = {}, // Your openpgp-generated key
      _keyName      = 'em.crypto.key.',
      _workerUri    = '/js/openpgp.worker.js',
      _localStorage = 'localStorage',
      _crypto       = 'crypto';
  
  /**
   * @return {boolean} Whether the VM running this script has access to localstorage.
   */
  function _supportsHTML5LocalStorage() {
    try {
      return _localStorage in window && window[_localStorage] !== null;
    }
    catch (err) {
      return false;
    }
  }
  
  /**
   * @return {boolean} Whether the VM running this script has access to crypto.
   */
  function _supportsCrypto() {
    try {
      return _crypto in window && window[_crypto] !== null;
    }
    catch (err) {
      return false;
    }
  }


  /**
   * Attempts to sign the given plaintextMessage with the given privateKey, encrypt the message+signature with
   * the given publicKey, and call the given callback once completed.
   * @param {module:key~Key} publicKey The receipient's openpgp public key object
   * @param {module:key~Key} privateKey The sender's openpgp private key object
   * @param {string} plaintextMessage The plaintext message
   * @param {function} callback callback(error, result) to execute upon completion.
   */
  function _encryptMessage(publicKey, privateKey, plaintextMessage, callback) {
    openpgp.signAndEncryptMessage(publicKey, privateKey, plaintextMessage, function(err, data) {
      if(err) {
        callback({
          message: plaintextMessage,
          success: false
        });        
      }
      else {
        callback({
          message : openpgp.message.readArmored(data),
          success : true
        });
      }
    });
  }

  /**
   * Attempts to decrypt the given encryptedMessage with the given privateKey, and if publicKey is
   * not null verify the signature of the encryptedMessage with the publicKey.
   * @param {module:key~Key} privateKey The recipient's openpgp private key object
   * @param {module:key~Key} publicKey (optional) The sender's openpgp public key object
   * @param {module:message~Message} encryptedMessage The message object with encrypted data
   * @param {function} callback(err, result) The callback function 
   */
  function _decryptMessage(privateKey, publicKey, encryptedMessage, callback) {
    if(privateKey === null || privateKey === undefined) {
      privateKey = openpgp.key.readArmored(_key.privateKeyArmored).keys[0];
    }

    if(encryptedMessage === null || encryptedMessage === undefined || encryptedMessage === '') {
      callback({
        message: encryptedMessage,
        success: false
      });
    }

    if(publicKey === null || publicKey == undefined) {
      openpgp.decryptMessage(privateKey, encryptedMessage, function(err, data) {
        if(err) {
          callback({
            message: {
              text: encryptedMessage
            },
            success: false
          });
        }
        else {
          callback({
            message: {
              text: data
            },
            validSignature: false,
            success: true
          });
        }
      });
    }
    else {
      // Attempt to decrypt and verify.
      openpgp.decryptAndVerifyMessage(privateKey, publicKey, encryptedMessage, function(err, data) {        
        if(err) {
          // Maybe we can decrypt but NOT verify?
          openpgp.decryptMessage(privateKey, encryptedMessage, function(err, data) {
            if(err) {
              callback({
                message: { 
                  text: encryptedMessage
                },
                success: false
              });
            }
            else {
              callback({
                message: { 
                  text: data
                },
                validSignature: false,
                success: true
              });
            }
          });
        }
        else {
          callback({
            message: data,
            validSignature: true,
            success: true
          });
        }
      });
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////
  ///                                              PUBLIC                                                    ///
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Initializes the gathers/sets values stored in localStorage.
   * @param {string} username The user to whom the key belongs.
   */
  function init(username) {
    _keyName += username;
    
    // Initialize openpgp
    openpgp.initWorker(_workerUri);
    
    // Initialize key or leave as-is for first-visit.
    if(localStorage[_keyName]) {
      _key = JSON.parse(localStorage[_keyName]);
    }
  }
  
  /**
   * Attempts to sign the given plaintextMessage with the given privateKeyArmored, encrypt the message+signature with
   * the given publicKeyArmored, and call the given callback once completed.
   * @param {string} publicKeyArmored The recipient's openpgp armored public key
   * @param {string} privateKeyArmored The sender's openpgp armored private key
   * @param {string} plaintextMessage The plaintext message to encrypt
   * @param {function} callback(err, result) The callback function
   */
  function encryptMessageWithPublicKeyArmored(publicKeyArmored, privateKeyArmored, plaintextMessage, callback) {
    var publicKey = openpgp.key.readArmored(publicKeyArmored).keys[0],
        privateKey = openpgp.key.readArmored(privateKeyArmored).keys[0];
    
    return _encryptMessage(publicKey, privateKey, plaintextMessage, callback);
  }
  
  /**
   * Attempts to decrypt the given encryptedMessageArmored with the given privateKeyArmored, and if 
   * publicKeyArmored is not null verify the signature of the encryptedMessage with the publicKey.
   * @param {string} privateKeyArmored The recipient's openpgp armored private key
   * @param {string} publicKeyarmored The sender's openpgp armored public key
   * @param {string} encryptedMessageArmored The armored encrypted message
   * @param {function} callback(err, result) The callback function
   */
  function decryptMessageWithPrivateKeyArmored(privateKeyArmored, publicKeyArmored, encryptedMessageArmored, callback) {
    var privateKey = openpgp.key.readArmored(privateKeyArmored).keys[0],
        publicKey = null,
        encryptedMessage = openpgp.message.readArmored(encryptedMessageArmored);
    
    if(publicKeyArmored !== null && publicKeyArmored !== undefined) {
      publicKey = openpgp.key.readArmored(publicKeyArmored).keys[0];
    }
    
    return _decryptMessage(privateKey, publicKey, encryptedMessage, callback);
  }
  
  /**
   * Determines whether the key object, upon which this entire 
   * library relies, exists in a valid state. Generally, this
   * should be queried before calling generateKeypair because
   * generateKeypair will override said key, if it exists. 
   */
  function keyExists() {
    return _key !== null && _key !== undefined &&
      _key.privateKeyArmored !== undefined && _key.privateKeyArmored !== null &&
      _key.publicKeyArmored !== undefined && _key.publicKeyArmored !== null
  }
  
  /**
   * Gets a copy of the key for this user.
   * 
   * NOTE: this only generates a copy; any subsequent changes
   * to the returned value will NOT be propagated forward.
   */
  function getKey() {    
    return JSON.parse(JSON.stringify(_key));
  }
  
  /**
   * A helper function for saving the key whenever it is updated.
   */
  function saveKey() {
    localStorage[_keyName] = JSON.stringify(_key);
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
   * @param {int} numBits The number of bits to use when generating the keypair
   * @param {String} userId The identifier of the user
   * @param {function} callback(err, result) The callback function 
   */
  function generateKeypair(numBits, userId, callback) {
    openpgp.generateKeyPair({numBits: numBits, userId: userId}, function(err, data) {
      _key.privateKeyArmored = data.privateKeyArmored;
      _key.publicKeyArmored = data.publicKeyArmored;
      
      callback(_key);
    });
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
    _key.publicKeyArmored = null;
    _key.privateKeyArmored = null;

    _saveKey();
  }

  // Run the initialization function and return.
  return (_supportsHTML5LocalStorage() && _supportsCrypto) ? {
    init : init,
    keyExists : keyExists,
    getKey : getKey,
    saveKey : saveKey,
    deleteKey : deleteKey,
    generateKeypair : generateKeypair,
    encryptMessageWithPublicKeyArmored : encryptMessageWithPublicKeyArmored,
    decryptMessageWithPrivateKeyArmored : decryptMessageWithPrivateKeyArmored
  } : null;

}());