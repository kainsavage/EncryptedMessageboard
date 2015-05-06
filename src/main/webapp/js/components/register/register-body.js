define(['ko', 'services/user-service', 'text!/html/components/register/register-body.html'], function(ko, userService, html) {
  function RegisterBody() {
    var self = this;
    this.username = ko.observable();
    this.password = ko.observable();

    this.register = function() {
      userService.register(self.username(), self.password(), 
        function(json) {
          self.showValidation(false);
          // Just redirect to home for now.
          window.location = '/profile';
        },
        function(jqXHR, textStatus){
          self.validationError(jqXHR.responseJSON.message);
          self.showValidation(true);
        });
    }
  }

  ko.components.register('register-body', { viewModel: RegisterBody, template: html });
});