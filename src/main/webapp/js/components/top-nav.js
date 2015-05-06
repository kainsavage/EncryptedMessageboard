define(['ko', 'services/user-service', 'text!/html/components/top-nav.html'], function(ko, userService, html) {
	function TopNav() {
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

    this.homeClass = ko.computed(function(){
      if(document.location.pathname === '/') {
        return "active";
      }
    });
    this.profileClass = ko.computed(function(){
      if(document.location.pathname === '/profile/') {
        return "active";
      }
    });
    this.friendsClass = ko.computed(function(){
      if(document.location.pathname === '/friends/') {
        return "active";
      }
    });
    this.loginClass = ko.computed(function(){
      if(document.location.pathname === '/login/') {
        return "active";
      }
    });
	}

	ko.components.register('top-nav', { viewModel: TopNav, template: html });
});