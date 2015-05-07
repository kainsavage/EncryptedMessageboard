define(['ko', 'services/user-service', 'text!/html/components/friends/friends-body.html'], function(ko, userService, html) {
  function FriendsBody() {
    var self = this;
    this.user = ko.observable(null);

    // Set the user via the user-service
    userService.getUser().done(function(user) {
      if (user.id) {
        self.user(user);
      }
      else {
        self.user(null);
      }
    });
  }

  ko.components.register('friends-body', { viewModel: FriendsBody, template: html });
});