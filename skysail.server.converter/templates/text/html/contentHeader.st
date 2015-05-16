contentHeader(converter, messages) ::= <<
  $if(converter.edit)$
    <a href='/i18n/messages/$resource.classname$.message/'>$messages.("content.header")$</a>
  $else$
$messages.("content.header")$
  $endif$
  <br>
>>