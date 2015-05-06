define(['ko', 'services/user-service', 'text!/html/components/reset-password/password-reset-body.html'], function(ko, userService, html) {
  function PasswordResetBody() {
    var self = this;
    this.username = ko.observable();

    this.resetPassword = function() {
    }
  }

  ko.components.register('password-reset-body', { viewModel: PasswordResetBody, template: html });
});