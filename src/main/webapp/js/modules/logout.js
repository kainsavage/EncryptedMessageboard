define(['services/user-service'], function(userService) {
  userService.logout().done(function(){
    // Try and redirect to the referrer
    window.location = document.referrer;
  });
});