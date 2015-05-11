define(['ko', 'services/user-service', 'services/message-service', 'services/crypto', 'text!/html/components/home/home-body.html'], 
  function(ko, userService, messageService, crypto, html) {
    function HomeBody() {
      var self = this;
      this.user = ko.observable(null);
      this.latestMessages = ko.observableArray();
  
      // Set the user via the user-service
      userService.getUser().done(function(user) {
        if (user.id) {
          self.user(user);
          crypto.init(self.user().username);
          
          // Get latest messages.
          messageService.getLatestMessages()
            .done(function(messages) {
              for (var i = 0; i < messages.length; i ++) {
                crypto.decryptMessageWithPrivateKeyArmored(crypto.getKey().privateKeyArmored, messages[i])
                  .then(function(message) {
                    self.latestMessages.push(message);
                  })
                  .catch(function(data){
                    // If this isn't present, errors get logged noisily.
                  });
              }
          });
        }
        else {
          self.user(null);
        }
      });
    }
  
    ko.components.register('home-body', { viewModel: HomeBody, template: html });
  });