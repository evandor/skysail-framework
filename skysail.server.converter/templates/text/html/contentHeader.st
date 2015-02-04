contentHeader(converter, messages) ::= <<
  $if(converter.edit)$
    <a href='/i18n/messages/$resource.classname$.message/'><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a>
$messages.("content.header")$
  $else$
$messages.("content.header")$
  $endif$
  <br>
>>