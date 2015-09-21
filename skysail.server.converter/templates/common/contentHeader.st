contentHeader(converter, messages, model) ::= <<
  $if(converter.edit)$
    <a href='/i18n/messages/$model.classname$.message/'>$messages.("content.header").translated$</a>
  $else$
$messages.("content.header").translated$
  $endif$
>>