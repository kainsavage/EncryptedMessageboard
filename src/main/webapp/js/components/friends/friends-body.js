define(['ko', 'services/user-service', 'services/friend-service', 'services/message-service', 'services/crypto', 'text!/html/components/friends/friends-body.html', 'simplemodal'], 
  function(ko, userService, friendService, messageService, crypto, html) {
    function FriendsBody() {
      var self = this;
      this.user = ko.observable(null);
      this.friends = ko.observableArray();
      this.username = ko.observable(null);
      this.userId = ko.observable(null);
      this.publicKey = ko.observable(null);
      this.message = ko.observable(null);
      this.transcript = ko.observable(null);
      this.openModal = null;
      
      this.openMessageDialog = function(username, userId, publicKey) {
        self.userId(userId);
        self.publicKey(publicKey);
        self.openModal = $("#messageDialog").modal({});
      };
      
      this.sendMessage = function() {
        console.log(self.message());
        crypto.encryptMessageWithPublicKeyArmored(self.publicKey(), crypto.getKey().privateKeyArmored, self.message())
          .then(function(encryptedMessage) {
            // Great, we need to encrypt our transcript
            crypto.encryptMessageWithPublicKeyArmored(crypto.getKey().publicKeyArmored, crypto.getKey().privateKeyArmored, self.message())
              .then(function(encryptedTranscript) {
                messageService.sendMessage(self.userId(), encryptedMessage, encryptedTranscript)
                .then(function() {
                  self.openModal.close();
                })
                .fail(function(err) {
                  console.log(err);
                });
              });
          });
      };

      // Set the user via the user-service
      userService.getUser().done(function(user) {
        if (user.id) {
          self.user(user);
          crypto.init(self.user().username);
        }
        else {
          // Shouldnt' even be here if there's no user
          self.user(null);
          window.location = '/';
        }
      });

      // Set friends via the friends-service
      friendService.getFriends().done(function(friends) {
        for (var i = 0; i < friends.length; i++) {
          self.friends.push(friends[i]);
        }
      });
    }

    ko.components.register('friends-body', { viewModel: FriendsBody, template: html });
  });