define(['ko', 'text!/html/components/footer.html'], function(ko, html) {
  function Footer() {
  }

  ko.components.register('app-footer', { viewModel: Footer, template: html });
});