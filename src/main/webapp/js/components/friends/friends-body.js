define(['ko', 'services/user-service', 'services/friend-service', 'text!/html/components/friends/friends-body.html'], 
  function(ko, userService, friendService, html) {
    function FriendsBody() {
      var self = this;
      this.user = ko.observable(null);
      this.friends = ko.observableArray();

      // Set the user via the user-service
      userService.getUser().done(function(user) {
        if (user.id) {
          self.user(user);
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