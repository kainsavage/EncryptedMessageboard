define(['ko', 'services/user-service', 'text!/html/components/login/login-body.html'], function(ko, userService, html) {
  function LoginBody() {
    var self = this;
    this.lhuser = ko.observable();
    this.lhpass = ko.observable();
    this.showValidation = ko.observable(false);
    this.validationError = ko.observable();

    this.login = function() {
      userService.login(self.lhuser(), self.lhpass())
      .done(function(json) {
        self.showValidation(false);
        // Just redirect to home for now.
        window.location = '/';
      })
      .fail(function(jqXHR, textStatus){
        self.validationError(jqXHR.responseJSON.message);
        self.showValidation(true);
      });
    }
  }

  ko.components.register('login-body', { viewModel: LoginBody, template: html });
});