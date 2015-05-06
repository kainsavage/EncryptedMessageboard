define(['ko', 'services/user-service', 'text!/html/components/home/home-body.html'], function(ko, userService, html) {
  function HomeBody() {
    var self = this;
    this.user = ko.observable(null);
    this.latestMessages = ko.observableArray();

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

  ko.components.register('home-body', { viewModel: HomeBody, template: html });
});