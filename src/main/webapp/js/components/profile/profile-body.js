define(['ko', 'jquery', 'services/user-service', 'services/crypto', 'text!/html/components/profile/profile-body.html', 'browser', 'tools', 'simplemodal'], 
  function(ko, $, userService, crypto, html) {
    function ProfileBody() {
      var self = this;
      this.user = ko.observable(null);
      this.publicKeyArmored = ko.observable();
      this.privateKeyArmored = ko.observable();
      this.openModal = null;
      this.success = ko.observableArray();
      this.errors = ko.observableArray();

      // Set the user via the user-service
      userService.getUser().done(function(user) {
        if (user.id) {
          self.user(user);
          crypto.init(self.user().username);
          self.publicKeyArmored(self.user().publicKey);
          self.privateKeyArmored(crypto.getKey().privateKeyArmored);
        }
        else {
          // Shouldnt' even be here if there's no user
          self.user(null);
          window.location = '/';
        }
      });

      this.save = function() {
        var publicKeyArmored = self.publicKeyArmored().replace(/\r\n/g,"\n").replace(/\n/g,"\r\n"),
            privateKeyArmored = self.privateKeyArmored().replace(/\r\n/g,"\n").replace(/\n/g,"\r\n");
        
        self.success.removeAll();
        self.errors.removeAll();

        if(publicKeyArmored !== '' && privateKeyArmored !== '') {
          // Check that the keys are valid and match.
          try {
            crypto.encryptMessageWithPublicKeyArmored(publicKeyArmored, privateKeyArmored, "test")
            .then(function(message) {
              crypto.decryptMessageWithPrivateKeyArmored(privateKeyArmored, publicKeyArmored, message)
              .then(function(decrypted) {
                if(decrypted.text === "test" &&
                   decrypted.signatures[0].valid) {
                  userService.editUser(self.user())
                  .then(function(data) {
                    if (data.validation) {
                      for(var i = 0; i < data.validation.instructions.length; i++) {
                        self.errors.push({instruction: data.validation.instructions[i]});
                      }
                    }
                    else {
                      self.success.push({instruction: "Profile has been successfully saved."});
                      crypto.saveKey();
                    }
                  })
                  .fail(function(jqXHR) {
                    for(var i = 0; i < jqXHR.responseJSON.validation.errors.length; i++) {
                      self.errors.push({instruction: jqXHR.responseJSON.validation.errors[i]});
                    }
                  });
                }
                else {
                  self.errors.push({instruction: "Your public and private keys do not match."});
                }
              });
            });
          }
          catch(err) {
            self.errors.push({instruction: "Your public and private keys do not match."});
          }
        }
        else {
          self.errors.push({instruction: "You must have a private and public keypair."});
        }
      };

      this.confirmGenerateKeypair = function(e) {
        var error,
            firstname = $("#firstname").val().trim(),
            lastname = $("#lastname").val().trim(),
            email = $("#email").val().trim();

        self.success.removeAll();
        self.errors.removeAll();
        
        if(firstname === null || firstname === undefined || firstname === '' ||
           lastname === null || lastname === undefined || lastname === '' ||
           email === null || email === undefined || email === '') {
          self.errors.push({instruction : "Your firstname, lastname, and email are required to create a keypair."});
          return;
        };
        
        if(crypto.keyExists()) {
          self.openModal = $("#confirmOld").modal({});
        }
        else {
          self.openModal = $("#confirmNew").modal({});
        }
      };

      this.generateKeypair = function() {
        var error,
            firstname = $("#firstname").val().trim(),
            lastname = $("#lastname").val().trim(),
            email = $("#email").val().trim();
        crypto.generateKeypair(2048, (firstname + ' ' + lastname + ' <' + email + '>'), function(key) {
          var user = self.user();
          self.publicKeyArmored(key.publicKeyArmored);
          self.privateKeyArmored(key.privateKeyArmored);
          user.publicKey = key.publicKeyArmored;
          self.user(user);

          if(self.openModal) {
            self.openModal.close();
          }
          $("#newKey").modal({});
        });
        
        if(self.openModal) {
          self.openModal.close();
        }
        $("#workingNewKey").modal({});
      }

      ko.bindingHandlers.attachFirstnameTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {
          $("#firstname").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [-15, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#firstnametip"
          });
        },
        update: function(){}
      };

      ko.bindingHandlers.attachLastnameTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {
          $("#lastname").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [-15, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#lastnametip"
          });
        },
        update: function(){}
      };

      ko.bindingHandlers.attachEmailTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {
          $("#email").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [0, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#emailtip"
          });
        },
        update: function(){}
      };

      ko.bindingHandlers.attachFriendsOnlyTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {        
          $("#friendsOnly").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [0, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#friendsOnlytip"
          });
        },
        update: function(){}
      };

      ko.bindingHandlers.attachPublicKeyTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {
          $("#publicKey").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [0, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#publicKeytip"
          });
        },
        update: function(){}
      };

      ko.bindingHandlers.attachPrivateKeyTooltip = {
        init : function(element, valueAccessor, allBindings, viewModel, bindingContext) {
          $("#privateKey").tooltip({
            // place tooltip on the right edge
            position: "center right",
            // a little tweaking of the position
            offset: [0, -230],
            // use the built-in fadeIn/fadeOut effect
            effect: "fade",
            // custom opacity setting
            //opacity: 0.8,
            // use this single tooltip element
            tip: "#privateKeytip"
          });
        },
        update: function(){}
      };
    }

    ko.components.register('profile-body', { viewModel: ProfileBody, template: html });
});